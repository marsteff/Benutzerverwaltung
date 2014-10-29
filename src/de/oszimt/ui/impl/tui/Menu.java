package de.oszimt.ui.impl.tui;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.ui.iface.UserInterface;
import de.oszimt.ui.impl.tui.MenuBuilder;
import de.oszimt.ui.impl.tui.util.Helper;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

/**
 * Created by Marci on 24.10.2014.
 */
public abstract class Menu implements UserInterface{
    protected IConcept concept;
    protected MenuBuilder builder;

    protected String message;
    protected Ansi.Color color;

    protected String[] entrys = {"Vorname",
            "Nachname",
            "Geburtstag",
            "Stadt",
            "Postleitzahl",
            "Strasse",
            "Strassennr.",
            "Abteilung"};

    public Menu(MenuBuilder builder){
        this(builder, "", Helper.STANDARD_COLOR);
    }

    public Menu(MenuBuilder builder, String message, Ansi.Color color){
        this.concept = builder.getConcept();
        AnsiConsole.systemInstall();
        this.builder = builder;
        this.message = message;
        this.color = color;
    }

    @Override
    public IConcept getConcept() {
        return this.concept;
    }

    @Override
    public void setConcept(IConcept concept) {
        this.concept = concept;
    }

    protected abstract void buildMenu(Ansi.Color color, String message);
    protected void buildMenu() {
        buildMenu(Helper.STANDARD_COLOR, "");
    }
}
