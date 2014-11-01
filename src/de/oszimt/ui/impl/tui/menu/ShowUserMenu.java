package de.oszimt.ui.impl.tui.menu;

import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class ShowUserMenu extends Menu {
    public static final String FIELDNAME = "Benutzer anzeigen";
    public static final int priority = 10;

    public ShowUserMenu(MenuBuilder builder) {
        super(builder);
    }

    @Override
    protected void buildMenu() {
        Helper.clean();

        Helper.writeHeader(FIELDNAME);
        if(message.length() > 0){
            Helper.println(color, message);
        }
        int inp = Helper.printGetUserId();

        if(inp == -1){
            builder.setActualState(new MainMenu(builder));
            return;
        }
        User user = getConcept().getUser(inp);
        if(user == null){
            buildMenu(RED,"Kein Benutzer mit der ID gefunden");
            return;
        }
        Helper.printUser(user, entrys);
        printGoOnYesNo("showUser", "Weiteren benutzer anzeigen");
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
