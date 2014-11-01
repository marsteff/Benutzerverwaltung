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
public class CreateDepartmentMenu extends Menu {
    public static final String FIELDNAME = "Neue Abteilung anlegen";
    public static final int priority = 55;

    public CreateDepartmentMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu() {
        Helper.clean();
        Helper.writeHeader(FIELDNAME);
        Helper.print("Name (z=zurück): ");
        String name = Helper.readString();
        if(name.trim().compareTo("z") == 0){
            builder.setActualState(new MainMenu(builder));
            return;
        }
        concept.upsertDepartment(new Department(name));
        builder.setActualState(new MainMenu(builder,"Abteilung '" + name + "' hinzufügt",GREEN));
    }
}
