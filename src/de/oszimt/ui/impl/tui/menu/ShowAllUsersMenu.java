package de.oszimt.ui.impl.tui.menu;

import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import java.util.List;

/**
 * Created by m588 on 24.10.2014.
 */
public class ShowAllUsersMenu extends Menu {
    public static final String FIELDNAME = "Alle Benutzer anzeigen";
    public static final int priority = 50;

    public ShowAllUsersMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu(Ansi.Color color, String message) {
        Helper.clean();
        Helper.print("Anzahl der Benutzer pro Seite (5):");
        String entriesPerPageStr = Helper.readString();
        int entiresPerPage = 5;
        try {
            entiresPerPage = new Integer(entriesPerPageStr);
        } catch (NumberFormatException e) {}

        List<User> userList = concept.getAllUser();
        buildMenu(entiresPerPage, 0, userList);
    }

    protected void buildMenu(int entriesPerPage, int page, List<User> userList){
        Helper.clean();
        Helper.buildTable(userList,entriesPerPage,page, entrys);
        Helper.print("Seite " + (page + 1) + "/" + ( userList.size() / entriesPerPage) + " (0: Zurück):");
        String pageStr = Helper.readString();
        try {
            page = new Integer(pageStr) - 1;
        } catch (NumberFormatException e) {}

        if(page * entriesPerPage >= userList.size()){
            page = 0;
        }

        if(page < 0){
            builder.setActualState(new MainMenu(builder));
            return;
        }

        buildMenu(entriesPerPage, page, userList);
    }

}
