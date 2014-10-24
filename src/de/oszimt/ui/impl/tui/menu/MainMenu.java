package de.oszimt.ui.impl.tui.menu;

import de.oszimt.ui.impl.tui.util.Helper;

import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class MainMenu extends Menu{
    @Override
    public void buildMenu() {
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

        writeHeader(getConcept().getTitle());
        buildMenue(entrys,color,message);


        //einlesen des Input´s
        int input = readInt();

        //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
        if (input == -1 || input > entrys.length || input < 1) {
            showMainMenu(RED,"Falsche Eingabe, bitte eine Zahl zwischen 1 und " + entrys.length + " eingeben");
            return;
        }

        //Prüfung, welches Menue aufgerufen werden soll
        switch (input) {
            case 1:
                createUser();
                break;
            case 2:
                showUser();
                break;
            case 3:
                editUser();
                break;
            case 4:
                deleteUser();
                break;
            case 5:
                searchUser(createDummyUser());
                break;
            case 6:
                showAllUsers();
                break;
            case 7:
                settigns();
                break;
            case 8:
                System.exit(0);

        }
    }
}
