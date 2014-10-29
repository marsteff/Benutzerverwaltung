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
public class EditUserMenu extends Menu {
    public static final String FIELDNAME = "Benutzer bearbeiten";
    public static final int priority = 20;

    public EditUserMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu(Ansi.Color color, String message) {
        buildMenu(color, message, -1);
    }

    private void buildMenu(Ansi.Color color, String message, int kunden_id){
        Helper.clean();
        boolean errorshown = false;
        Helper.writeHeader("Kunden bearbeiten");
        if(kunden_id < 0) {
            if (message.length() > 0) {
                Helper.println(color, message);
                errorshown = true;
            }
            kunden_id = Helper.printGetUserId();
        }

        if (kunden_id == -1) {
            builder.setActualState(new MainMenu(builder));
            return;
        }
        User user = getConcept().getUser(kunden_id);
        if (user == null) {
            buildMenu(RED, "Kein Benutzer mit der ID gefunden");
            return;
        }

        Helper.printUserOptions(user, entrys);
        if (message.length() > 0 && !errorshown) {
            Helper.println(color, message);
        }
        int input = 0;
        do {
            Helper.println("");
            Helper.print("Wählen Sie die zuändernde Eigenschaft aus (0=zurück | 1-" + entrys.length + "): ");

            //einlesen des Input´s
            input = Helper.readInt();

            //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
            if (input > -1 && input <= entrys.length) {
                break;
            }
            Helper.println(RED, "Falsche Eingabe");
        } while (true);

        if(input == 0){
            builder.setActualState(new MainMenu(builder));
            return;
        }

        String value = null;
        //Prüfung, welches Menue aufgerufen werden soll
        switch (input) {
            //Vorname
            case 1:
                Helper.print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = Helper.readString();
                user.setFirstname(value);
                break;

            //Nachname
            case 2:
                Helper.print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = Helper.readString();
                user.setLastname(value);
                break;

            //Geburtstag
            case 3:
                LocalDate date = null;
                do {
                    Helper.println("Neuen Geburtstag eingeben ");
                    int day = Helper.determineBirthday("Tag", 1, 31);
                    int month = Helper.determineBirthday("Monat", 1, 12);
                    int year = Helper.determineBirthday("Jahr", 1900, LocalDate.now().getYear());
                    try {
                        date = LocalDate.of(year, month, day);
                    } catch (DateTimeException e) {
                    }
                    if (date != null && date.isBefore(LocalDate.now())) {
                        break;
                    }
                    Helper.println(RED, "Das Datum ist nicht gültig");
                } while (true);

                user.setBirthday(date);
                break;

            //Stadt
            case 4:
                Helper.print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = Helper.readString();
                user.setCity(value);
                break;

            //PLZ
            case 5:
                Helper.print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = Helper.readString();
                user.setPLZ(Integer.parseInt(value));
                break;

            //Strasse
            case 6:
                Helper.print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = Helper.readString();
                user.setStreet(value);
                break;

            //Stassen-Nummer
            case 7:
                Helper.print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = Helper.readString();
                user.setStreetnr(value);
                break;

            //Abteilung
            case 8:
                List<Department> departmentList = concept.getAllDepartments();
                String[] departmentArray = new String[departmentList.size()];

                for (int i = 0; i < departmentArray.length; i++) {
                    departmentArray[i] = Helper.toAscii(departmentList.get(i).getName());
                }

                int departmentValue = Helper.buildDepartmentView(departmentArray, input,false, entrys);

                user.setDepartment(departmentList.get(departmentValue - 1));
                break;

        }
        concept.upsertUser(user);
        buildMenu(GREEN,"Änderung erfolgreich übernommen",user.getId());
    }

}
