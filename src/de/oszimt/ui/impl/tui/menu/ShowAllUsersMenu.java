package de.oszimt.ui.impl.tui.menu;

import com.sun.deploy.util.ArrayUtil;
import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import jdk.nashorn.internal.ir.LiteralNode;
import org.fusesource.jansi.Ansi;

import javax.ws.rs.HEAD;
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
    protected void buildMenu() {
        Helper.clean();
        Helper.print("Anzahl der Benutzer pro Seite (z=zurück):");
        String entriesPerPageStr = Helper.readString();
        if(entriesPerPageStr.trim().compareTo("z") == 0){
            builder.setActualState(new MainMenu(builder));
            return;
        }
        int entiresPerPage = 5;
        try {
            entiresPerPage = new Integer(entriesPerPageStr);
        } catch (NumberFormatException e) {}

        List<User> userList = concept.getAllUser();
        buildMenu(entiresPerPage, 0, userList);
    }

    protected void buildMenu(int entriesPerPage, int page, List<User> userList){
        Helper.clean();
        Helper.writeHeader(FIELDNAME);
        String[] labels = Helper.ArrayMerge(new String[]{"id"},entrys);
        Helper.buildTable(userList, entriesPerPage, page, labels, new Helper.entryToTableRow<User>() {
            @Override
            public String[] toArray(User entry) {
                return Helper.ArrayMerge(
                        new String[]{entry.getId() + ""},
                        Helper.userParameterToArray(entry)
                );
            }
        });
        Helper.print("Seite " + (page + 1) + "/" + (int) Math.ceil( userList.size() / (double) entriesPerPage) + " (0: Zurück):");
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
