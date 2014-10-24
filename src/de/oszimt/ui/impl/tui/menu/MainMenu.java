package de.oszimt.ui.impl.tui.menu;

import com.google.common.reflect.ClassPath;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import java.io.IOException;

import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class MainMenu extends Menu{


    public MainMenu(MenuBuilder builder) {
        super(builder);
    }

    public MainMenu(MenuBuilder builder, String message, Ansi.Color color) {
        super(builder, message, color);
    }

    @Override
    protected void buildMenu(Ansi.Color color, String message) {
        Helper.clean();

        String[] entrys = {
                "Benutzer anlegen",
                "Benutzer anzeigen",
                "Benutzer bearbeiten",
                "Benutzer loeschen",
                "Benutzer suchen",
                "Alle Benutzer Anzeigen",
                "Einstellugen",
                "Beenden"};

        Helper.writeHeader(getConcept().getTitle());
        Helper.buildMenue(entrys, color, message);


        //einlesen des Input´s
        int input = Helper.readInt();

        //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
        if (input == -1 || input > entrys.length || input < 1) {
            this.buildMenu(RED, "Falsche Eingabe, bitte eine Zahl zwischen 1 und " + entrys.length + " eingeben");
            return;
        }

        //TODO hier werde ich mal was versuchen
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("my.package.")) {
                    final Class<?> clazz = info.load();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Prüfung, welches Menue aufgerufen werden soll
        switch (input) {
            case 1:
                builder.setActualState(new CreateUserMenu(builder));
                break;
            case 2:
                builder.setActualState(new ShowUserMenu(builder));
                break;
            case 3:
                builder.setActualState(new EditUserMenu(builder));
                break;
            case 4:
                builder.setActualState(new DeleteUserMenu(builder));
                break;
            case 5:
                builder.setActualState(new SearchUserMenu(builder));
                break;
            case 6:
                builder.setActualState(new ShowAllUsersMenu(builder));
                break;
            case 7:
                builder.setActualState(new SettignsMenu(builder));
                break;
            case 8:
                System.exit(0);

        }
    }
}
