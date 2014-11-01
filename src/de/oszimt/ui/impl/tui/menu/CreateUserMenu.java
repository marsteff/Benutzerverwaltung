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

    @Override
    protected void buildMenu() {
        Helper.writeHeader(FIELDNAME);
        Helper.println(YELLOW,"Zum Abbrechen '"+abort+"' eingeben!");
        String firstname = Helper.toShortUgly(0, entrys);
        if(checkAbort(firstname)) return;
        String lastname = Helper.toShortUgly(1, entrys);
        if(checkAbort(lastname)) return;
        LocalDate date = Helper.readDate();
        if(checkAbort(date == null ? abort + "" : "")) return;
        String city = Helper.toShortUgly(3, entrys);
        if(checkAbort(city)) return;
        int zipCode = Helper.readZipCode(entrys,4,abort);
        if(checkAbort(zipCode < 0 ? abort + "" : "")) return;
        String street = Helper.toShortUgly(5, entrys);
        if(checkAbort(street)) return;
        String streetNr = Helper.toShortUgly(6, entrys);
        if(checkAbort(streetNr)) return;
        Department dep = Helper.readDepartment(entrys,7,2,concept.getAllDepartments());
        User newUser = new User(firstname, lastname, date, city, street, streetNr, zipCode, dep);
        concept.upsertUser(newUser);
        builder.setActualState(new MainMenu(
                builder,
                "Benutzer: " + newUser.getFirstname() + " " + newUser.getLastname() + " hinzugefügt",GREEN)
        );
    }
}
