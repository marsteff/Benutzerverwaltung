package de.oszimt.ui.impl.tui.menu;

import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class SettignsMenu extends Menu {

    private boolean useRestService = false;

    public SettignsMenu(MenuBuilder builder) {
        super(builder);
    }
    @Override
    protected void buildMenu(Ansi.Color color, String message) {
        Helper.clean();

        Helper.writeHeader("Einstellungen");
        String[] StettingLabels = {
                "Alle Kunden löschen",
                "Zufalls Kunden erstellen",
                "REST Service (PLZ -> Stadt) [" +
                        (useRestService ?
                                Helper.colorString(GREEN, "aktiv") :
                                Helper.colorString(RED, "inaktiv")
                        ) + "]",
                "Zurück"
        };


        Helper.buildMenue(StettingLabels, color, message);


        //einlesen des Input´s
        int input = Helper.readInt();
        switch (input) {
            case 1:
                getConcept().getAllUser().forEach(u -> getConcept().deleteUser(u));
                buildMenu(GREEN, "Information: Alle Kunden wurden gelöscht");
                break;
            case 2:
                getConcept().createRandomUsers(useRestService);
                buildMenu(GREEN, "Information: Zufalls Kunden wurden erstellt");
                break;
            case 3:
                useRestService = !useRestService;
                buildMenu();
                break;
            case 4:
                builder.setActualState(new MainMenu(builder));
                break;
            default:
                buildMenu(RED, "Fehler: Falsche Eingabe, versuchen Sie es erneut");

        }
    }
}
