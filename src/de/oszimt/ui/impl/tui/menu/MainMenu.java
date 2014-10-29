package de.oszimt.ui.impl.tui.menu;

import com.google.common.reflect.ClassPath;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.fusesource.jansi.Ansi.Color.RED;

/**
 * Created by m588 on 24.10.2014.
 */
public class MainMenu extends Menu {


    public MainMenu(MenuBuilder builder) {
        super(builder);
    }

    public MainMenu(MenuBuilder builder, String message, Ansi.Color color) {
        super(builder, message, color);
    }

    @Override
    protected void buildMenu(Ansi.Color color, String message) {
        Helper.clean();

        //TODO hier werde ich mal was versuchen
        //Laden der Menue Klassen im Package 'de.oszimt.ui.impl.tui.menu' und speichern dieser in einer Map
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Map<Integer, Class<Menu>> map = new HashMap<>();
        try {
            //Hier wirds die Google-Bibliothek Guava benutzt, um aus dem Package die Klassen zu laden
            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("de.oszimt.ui.impl.tui.menu.")) {
                    final Class<Menu> clazz = (Class<Menu>)info.load();
                    // Prüfen, ob die Klasse eine statische Variable namens 'FIELDNAME' hat und ob diese ein String ist
                    // Wenn ja, Klasse der Map hinzufügen
                    if(Helper.getDeclaredField(clazz, "FIELDNAME") != null &&
                            Helper.getDeclaredField(clazz, "FIELDNAME") instanceof String) {
                        //Auslesen des Statischen Feldes 'menuId', um eine feste Reihenfolge zu gewährleisten
                        map.put((Integer) Helper.getDeclaredField(clazz, "menuId"), clazz);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] entrys = new String[map.size()+1];

        // Befüllen des Arrays mit den Bezeichnungen der Klassen für das Tui-Menue
        // Dieser statische String wird mittels Reflection aus der Klasse gelesen.
        for (int i = 0; i < map.size(); i++) {
            entrys[i] = (String) Helper.getDeclaredField(map.get(i+1), "FIELDNAME");
        }
        //Als letztes Element das Beenden Feld hinzufügen
        entrys[entrys.length - 1] = "Beenden";

        //Aufbauen des Menues in der Konsole
        Helper.writeHeader(getConcept().getTitle());
        Helper.buildMenue(entrys, color, message);

        //einlesen des Input´s
        int input = Helper.readInt();

        //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
        if (input == -1 || input > entrys.length || input < 1) {
            this.buildMenu(RED, "Falsche Eingabe, bitte eine Zahl zwischen 1 und " + entrys.length + " eingeben");
            return;
        }
        //Wenn das letzte Element gewaehlt wurde (Beenden) dann das Programm beenden
        if(input == entrys.length){
            System.exit(0);
        }

        //Aufgrund des gewaehlten Menue, mit Reflection das entsprechende Objekt erzeugen und den Builder-Status setzen
        builder.setActualState(Helper.getObject(map.get(input), builder));
    }
}
