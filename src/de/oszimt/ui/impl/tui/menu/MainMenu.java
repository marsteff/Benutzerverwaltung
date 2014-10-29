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
        //Laden der Menue Klassen und speichern in einer Map
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Map<Integer, Class<Menu>> map = new HashMap<>();
        try {
            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("de.oszimt.ui.impl.tui.menu.")) {
                    final Class<Menu> clazz = (Class<Menu>)info.load();
                    if(getDeclaredField(clazz, "FIELDNAME") != null) {
                        map.put((Integer) getDeclaredField(clazz, "menuId"), clazz);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] entrys = new String[map.size()+1];

        for (int i = 0; i < map.size(); i++) {
            entrys[i] = (String)getDeclaredField(map.get(i+1), "FIELDNAME");
        }
        entrys[entrys.length - 1] = "Beenden";

        Helper.writeHeader(getConcept().getTitle());
        Helper.buildMenue(entrys, color, message);

        //einlesen des Input´s
        int input = Helper.readInt();

        //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
        if (input == -1 || input > entrys.length || input < 1) {
            this.buildMenu(RED, "Falsche Eingabe, bitte eine Zahl zwischen 1 und " + entrys.length + " eingeben");
            return;
        }
        if(input == 8){
            System.exit(0);
        }

        builder.setActualState(getObject(map, builder, input));
    }

    public static Object getDeclaredField(Class<Menu> clazz, String field) {
        try {
            return clazz.getDeclaredField(field).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e1) {
        }
        return null;
    }

    public static Menu getObject(Map<Integer, Class<Menu>> map, MenuBuilder builder, Integer input){
        try {
            return map.get(input).getConstructor(MenuBuilder.class).newInstance(builder);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
