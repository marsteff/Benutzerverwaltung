package de.oszimt.ui.impl.tui.menu;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;

import java.util.List;

/**
 * Created by m588 on 24.10.2014.
 */
public class ShowAllDepartmentsMenu extends Menu {
    public static final String FIELDNAME = "Alle Abteilungen anzeigen";
    public static final int priority = 56;

    public ShowAllDepartmentsMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu() {
        Helper.clean();
        Helper.print("Anzahl der Benutzer pro Seite (5):");
        String entriesPerPageStr = Helper.readString();
        int entiresPerPage = 5;
        try {
            entiresPerPage = new Integer(entriesPerPageStr);
        } catch (NumberFormatException e) {}

        List<Department> depList = concept.getAllDepartments();
        buildMenu(entiresPerPage, 0, depList);
    }

    protected void buildMenu(int entriesPerPage, int page, List<Department> depList){
        Helper.clean();
        Helper.buildDepartmentTable(depList,entriesPerPage,page);
        Helper.print("Seite " + (page + 1) + "/" + (int) Math.ceil( depList.size() / (double) entriesPerPage) + " (0: Zurück):");
        String pageStr = Helper.readString();
        try {
            page = new Integer(pageStr) - 1;
        } catch (NumberFormatException e) {}

        if(page * entriesPerPage >= depList.size()){
            page = 0;
        }

        if(page < 0){
            builder.setActualState(new MainMenu(builder));
            return;
        }

        buildMenu(entriesPerPage, page, depList);
    }

}
