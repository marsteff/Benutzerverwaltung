package de.oszimt.ui.impl.tui.menu;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import de.oszimt.util.Validation;
import org.fusesource.jansi.Ansi;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.YELLOW;

/**
 * Created by m588 on 24.10.2014.
 */
public class CreateUserMenu extends Menu {
    public static final String FIELDNAME = "Neuen Benutzer anlegen";
    public static final int priority = 0;
    public static final char abort = 'z';


    public CreateUserMenu(MenuBuilder builder) {
        super(builder);
    }


    private boolean checkAbort(String inp){
        if(inp.trim().compareTo(abort+"") == 0){
            builder.setActualState(new MainMenu(builder));
            return true;
        }
        return false;
    }

    private LocalDate readDate(){
        LocalDate date = null;
        do {
            Helper.print("Geburtstag eingeben (");
            Helper.print(YELLOW,"'0' zum abbrechen eingeben");
            Helper.println("):");
            int day = Helper.determineBirthday("Tag", 1, 31);
            if(day == 0) return null;
            int month = Helper.determineBirthday("Monat", 1, 12);
            if(month == 0) return null;
            int year = Helper.determineBirthday("Jahr", 1900, LocalDate.now().getYear());
            if(month == 0) return null;
            try {
                date = LocalDate.of(year, month, day);
            } catch (DateTimeException e) {}
            if (date != null && date.isBefore(LocalDate.now())) {
                break;
            }
            Helper.println(RED, "Das Datum ist nicht gültig");
        } while (true);
        return date;
    }

    private int readZipCode(){
        do {
            String zipCode = Helper.toShortUgly(4, entrys);
            if(zipCode.trim().compareTo(abort+"") == 0){
                return -1;
            }
            if(Validation.checkIfZipCode(zipCode)){
                return Integer.parseInt(zipCode.trim());
            }else{
                Helper.println(RED,"PLZ kann nur aus 5 Zahlen bestehen!");
            }
        }while (true);
    }

    private Department readDepartment(){
        Helper.print(entrys[7]);
        Helper.printWhitespace(entrys, 7, 2);
        Helper.println(": ");
        List<Department> departmentList = concept.getAllDepartments();
        String[] departmentArray = new String[departmentList.size()];

        for (int i = 0; i < departmentArray.length; i++) {
            departmentArray[i] = Helper.toAscii(departmentList.get(i).getName());
        }
        for (int i = 0; i < departmentArray.length; i++) {
            Helper.print(departmentArray[i]);
            Helper.printWhitespace(departmentArray, i, 2);
            Helper.println("(" + (i + 1) + ")");
        }
        int departmentValue;
        do {
            Helper.print("Zahl für Department eingeben: ");
            departmentValue = Helper.readInt();
            if (departmentValue > 0 && departmentValue <= departmentArray.length) {
                break;
            }
            Helper.println(RED, "Eingabe nicht gueltig. Wert muss zwischen 1 und " + departmentArray.length + " liegen");
        } while (true);
        return new Department(departmentValue, departmentArray[departmentValue - 1]);
    }

    @Override
    protected void buildMenu() {
        Helper.writeHeader(FIELDNAME);
        Helper.println(YELLOW,"Zum Abbrechen '"+abort+"' eingeben!");
        String firstname = Helper.toShortUgly(0, entrys);
        if(checkAbort(firstname)) return;
        String lastname = Helper.toShortUgly(1, entrys);
        if(checkAbort(lastname)) return;
        LocalDate date = readDate();
        if(checkAbort(date == null ? abort + "" : "")) return;
        String city = Helper.toShortUgly(3, entrys);
        if(checkAbort(city)) return;
        int zipCode = readZipCode();
        if(checkAbort(zipCode < 0 ? abort + "" : "")) return;
        String street = Helper.toShortUgly(5, entrys);
        if(checkAbort(street)) return;
        String streetNr = Helper.toShortUgly(6, entrys);
        if(checkAbort(streetNr)) return;
        Department dep = readDepartment();
        User newUser = new User(firstname, lastname, date, city, street, streetNr, zipCode, dep);
        concept.upsertUser(newUser);
        builder.setActualState(new MainMenu(
                builder,
                "Benutzer: " + newUser.getFirstname() + " " + newUser.getLastname() + " hinzugefügt",GREEN)
        );
    }
}
