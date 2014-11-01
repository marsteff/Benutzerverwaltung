package de.oszimt.ui.impl.tui.menu;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class DeleteDepartmentMenu extends Menu {
    public static final String FIELDNAME = "Abteilung löschen";
    public static final int priority = 57;

    public DeleteDepartmentMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu() {
        Helper.clean();
        Helper.writeHeader(FIELDNAME);

        List<Department> allDeps = getConcept().getAllDepartments();
        Helper.buildTable(allDeps,allDeps.size(),0,new String[]{"ID","Name","Anzahl Mitarbeiter"},new Helper.entryToTableRow<Department>() {
            @Override
            public String[] toArray(Department entry) {
                return new String[]{entry.getId() + "",entry.getName(),entry.getAmount() + ""};
            }
        });
        if(message.length() > 0){
            Helper.println(color,message);
            color = Helper.STANDARD_COLOR;
            message = "";
        }
        int department_id = Helper.printGetDepartmentId();

        if (department_id == -1) {
            builder.setActualState(new MainMenu(builder));
            return;
        }

        List<Department> selectedList = allDeps.stream().filter(
                d -> d.getId() == department_id
        ).collect(Collectors.toList());


        if (selectedList.size() != 1) {
            buildMenu(RED, "Keine Abteilung mit der ID gefunden");
            return;
        }

        Department selected = selectedList.get(0);

        //Eine Abteilung kann nur gelöscht werden wenn sie keine Mitarbeiter mehr enthält
        if(selected.getAmount() > 0){
            buildMenu(RED, "Die Abteilung kann nur gelöscht werden wenn Sie keinen Mitarbeitern mehr zugeordnet ist");
            return;
        }

        //Soll der Benutzer wirklich gelöscht werden ?
        if (Helper.checkInputForYesOrNo("Abteilung " + selected.getName() + " wirklich löschen ? (j/n)")) {
            concept.deleteDepartment(selected);
            Helper.println(GREEN, selected.getName() + " (id: " + selected.getId() + ") wurde gelöscht");
        }

        //Soll ein weiterer Benutzer gelöscht werden ?
        printGoOnYesNo("deleteDepartment", "Eine weitere Abteilung löschen");
    }

    private void printGoOnYesNo(String method, String text){
        Helper.print(text + " (j/n): ");
        String jn = Helper.readString();
        if(jn.compareTo("j") == 0){
            buildMenu(color, message);
            return;
        }else if(jn.compareTo("n") == 0){
            builder.setActualState(new MainMenu(builder));
            return;
        }else{
            builder.setActualState(new MainMenu(builder, "'" + jn + "' ist keine gültige Eingabe und wird als Nein gewertet", Ansi.Color.RED));
        }
    }

}
