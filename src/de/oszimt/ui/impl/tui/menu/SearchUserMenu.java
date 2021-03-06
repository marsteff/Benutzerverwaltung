package de.oszimt.ui.impl.tui.menu;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class SearchUserMenu extends Menu {
    public static final String FIELDNAME = "Benutzer suchen";
    public static final int priority = 40;

    private User user;
    public SearchUserMenu(MenuBuilder builder, User dummyUser) {
        super(builder);
        this.user = dummyUser;
    }

    public SearchUserMenu(MenuBuilder builder) {
        super(builder);
        this.user = Helper.createDummyUser();
    }

    @Override
    protected void buildMenu() {
        // TODO Mal sehen was ich mit dem im Konstruktor gesetzten User mache
        buildMenu(Helper.createDummyUser());
    }

    protected void buildMenu(User user){
        Ansi.Color color = getColor();
        String message = getMessage();

        Helper.clean();
        Helper.writeHeader("Benutzer Suchen");

        Helper.printUserOptions(user, entrys);

        int input;
        String inputString;

        if(message.length() > 0){
            Helper.println(color, message);
        }

        Helper.print("Welches Attribut zur Suche hinzufuegen ? (1-" + entrys.length + ", s = suchen, 0 = zum Hauptmenü): ");
        //einlesen des Input´s
        inputString = Helper.readString();
        if(inputString.trim().compareTo("0") == 0){
            builder.setActualState(new MainMenu(builder));
            return;
        }else if(inputString.trim().compareTo("s") == 0){

        }else {
            try {
                input = Integer.parseInt(inputString);

            } catch (NumberFormatException e) {
                input = 0;
            }

            if (input < 1 || input > entrys.length) {
                setColor(RED);
                setMessage("Falsche Eingabe");
                buildMenu(user);
                return;
            }

            String value;
            //Prüfung, welches Menue aufgerufen werden soll
            User tmp = Helper.createDummyUser();
            switch (input) {

                //Vorname
                case 1:
                    Helper.print("Wert für " + entrys[input - 1] + " eingeben (\"-\" = leeren): ");
                    value = Helper.readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setFirstname(tmp.getFirstname());
                    }else{
                        user.setFirstname(value);
                    }
                    break;

                //Nachname
                case 2:
                    Helper.print("Wert für " + entrys[input - 1] + " eingeben  (\"-\" = leeren): ");
                    value = Helper.readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setLastname(tmp.getLastname());
                    }else{
                        user.setLastname(value);
                    }
                    break;

                //Geburtstag
                case 3:
                    LocalDate date = null;
                    do {
                        Helper.println("Geburtstag eingeben (zum leeren alle Werte = 0 setzen) ");
                        int day = Helper.determineBirthday("Tag", 0, 31);
                        int month = Helper.determineBirthday("Monat", 0, 12);
                        int year = Helper.determineBirthday("Jahr", 1900, LocalDate.now().getYear());


                        if(day + month + year == 0){
                            user.setBirthday(tmp.getBirthday());
                            break;
                        }


                        try {
                            date = LocalDate.of(year, month, day);
                        } catch (DateTimeException e) {
                        }
                        if (date != null) {
                            break;
                        }
                        Helper.println(RED, "Das Datum ist nicht gueltig");
                    } while (true);

                    user.setBirthday(date);
                    break;

                //Stadt
                case 4:
                    Helper.print("Wert fuer " + entrys[input - 1] + " eingeben (\"-\" = leeren):  ");
                    value = Helper.readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setCity(tmp.getCity());
                    }else{
                        user.setCity(value);
                    }
                    break;

                //PLZ
                case 5:
                    Helper.print("Wert fuer " + entrys[input - 1] + " eingeben  eingeben (\"-\" = leeren): ");
                    value = Helper.readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setZipcode(tmp.getZipcode());
                    }else{
                        user.setZipcode(Integer.parseInt(value));
                    }
                    break;

                //Strasse
                case 6:
                    Helper.print("Wert fuer " + entrys[input - 1] + " eingeben (\"-\" = leeren):  ");
                    value = Helper.readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setStreet(tmp.getStreet());
                    }else{
                        user.setStreet(value);
                    }
                    break;

                //Stassen-Nummer
                case 7:
                    Helper.print("Wert fuer " + entrys[input - 1] + " eingeben (\"-\" = leeren): ");
                    value = Helper.readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setStreetnr(tmp.getStreetnr());
                    }else{
                        user.setStreetnr(value);
                    }
                    break;

                //Abteilung
                case 8:
                    List<Department> departmentList = concept.getAllDepartments();
                    String[] departmentArray = new String[departmentList.size()];

                    for (int i = 0; i < departmentArray.length; i++) {
                        departmentArray[i] = Helper.toAscii(departmentList.get(i).getName());
                    }

                    int departmentValue = Helper.buildDepartmentView(departmentArray, input, true, entrys);
                    if(departmentValue == 0){
                        user.setDepartment(tmp.getDepartment());
                    }else{
                        user.setDepartment(departmentList.get(departmentValue - 1));
                    }
                    break;

            }

            setColor(GREEN);
            setMessage("Suche erweitert");
            buildMenu(user);
            return;
        }

        List<User> userList = Helper.searchUsers(user, this.getConcept().getAllUser());
        String[] labels = Helper.ArrayMerge(new String[]{"id"},entrys);
        Helper.buildTable(userList, userList.size(), 0, labels, new Helper.entryToTableRow<User>() {
            @Override
            public String[] toArray(User entry) {
                return Helper.ArrayMerge(
                        new String[]{entry.getId() + ""},
                        Helper.userParameterToArray(entry)
                );
            }
        });
        Helper.println("Filter ändern (1), Neue Suche(2), zum Hauptmenü (3)");
        int option;
        boolean first = true;
        do {
            if(first){
                first = false;
            }else{
                Helper.println(RED, "Falsche Eingabe");
            }
            option = Helper.readInt();
        }while (option < 1 || option > 3);
        switch (option){
            case 1:
                buildMenu(user);
                return;
            case 2:
                buildMenu(Helper.createDummyUser());
                return;
            default:
                builder.setActualState(new MainMenu(builder));
                return;
        }
    }
}
