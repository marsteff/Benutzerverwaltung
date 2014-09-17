package de.oszimt;

import de.oszimt.conzept.impl.Concept;
import de.oszimt.factory.PersistanceFactory;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.ui.controller.Gui;

/**
 * Created by Philipp on 17.09.14.
 */
public class Benutzerverwaltung {
    public static void main(String[] args) {
        new Gui(new Concept(PersistanceFactory.buildPersistance(PersistanceMethod.SQLITE)));
        //System.out.println("xs");
    }
}
