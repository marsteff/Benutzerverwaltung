package de.oszimt.ui.impl.tui.menu;

import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class DeleteUserMenu extends Menu {
    public static final String FIELDNAME = "Benutzer loeschen";
    public static final int priority = 30;

    public DeleteUserMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu() {
        Helper.clean();
        Helper.writeHeader("Benutzer löschen");
        if(message.length() > 0){
            Helper.println(color,message);
        }
        int user_id = Helper.printGetUserId();

        if (user_id == -1) {
            builder.setActualState(new MainMenu(builder));
            return;
        }
        User user = getConcept().getUser(user_id);
        if (user == null) {
            buildMenu(RED, "Kein Benutzer mit der ID gefunden");
            return;
        }

        Helper.printUser(user, entrys);

        //Soll der Benutzer wirklich gelöscht werden ?
        if (Helper.checkInputForYesOrNo("Benutzer wirklich löschen ? (j/n)")) {
            concept.deleteUser(user);
            Helper.println(GREEN, user.getFirstname() + " " + user.getLastname() + " (id: " + user.getId() + ") wurde gelöscht");
        }

        //Soll ein weiterer Benutzer gelöscht werden ?
        printGoOnYesNo("deleteUser", "Einen weiteren Benutzer löschen");
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
