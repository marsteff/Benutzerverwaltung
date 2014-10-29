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

import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class CreateUserMenu extends Menu {
    public static final String FIELDNAME = "Benutzer anlegen";
    public static final int priority = 0;

    public CreateUserMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu(Ansi.Color color, String message) {
        Helper.writeHeader("Neuen Benutzer anlegen");
        //TODO - at first i make it a lot ugly .. then i think to make it more comfortable
        String firstname = Helper.toShortUgly(0, entrys);
        String lastname = Helper.toShortUgly(1, entrys);
        LocalDate date = null;
        do {
            Helper.println("Geburtstag eingeben ");
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
        String city = Helper.toShortUgly(3, entrys);
        String zipCode = Helper.toShortUgly(4, entrys);
        String street = Helper.toShortUgly(5, entrys);
        String streetNr = Helper.toShortUgly(6, entrys);
        //DEPARTMENT PART
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
        int departmentValue = 0;
        do {
            Helper.print("Zahl für Department eingeben: ");
            departmentValue = Helper.readInt();
            if (departmentValue > 0 && departmentValue <= departmentArray.length) {
                break;
            }
            Helper.println(RED, "Eingabe nicht gueltig. Wert muss zwischen 1 und " + departmentArray.length + " liegen");
        } while (true);
        Department dep = new Department(departmentValue, departmentArray[departmentValue - 1]);
        User newUser = new User(firstname, lastname, date, city, street, streetNr, Integer.parseInt(zipCode), dep);
        concept.upsertUser(newUser);

        //TODO Rückmeldung ob erfolgreich oder nicht ?
        builder.setActualState(new MainMenu(builder));
    }
}
