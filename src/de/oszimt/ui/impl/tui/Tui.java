package de.oszimt.ui.impl.tui;

import de.oszimt.concept.iface.IConcept;

/**
 * Created by user on 17.09.2014.
 */
public class Tui {

    public Tui(IConcept concept) {
        showMenu(concept);
    }

    private void showMenu(IConcept concept) {

        MenuBuilder builder = new MenuBuilder(concept);
        while(true) {
            builder.buildMenu();
        }
    }

}
