package de.oszimt.ui.impl.tui;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.ui.iface.UserInterface;
import de.oszimt.ui.impl.tui.menu.MenuBuilder;

/**
 * Created by user on 17.09.2014.
 */
public class Tui implements UserInterface {

    private IConcept concept;

    public Tui(IConcept concept) {
        showMainMenu(concept);
    }

    private void showMainMenu(IConcept concept) {
        MenuBuilder builder = new MenuBuilder(concept);
        while (true) {
            builder.buildMenu();
        }
    }

    @Override
    public IConcept getConcept() {
        return this.concept;
    }

    @Override
    public void setConcept(IConcept concept) {
        this.concept = concept;
    }
}
