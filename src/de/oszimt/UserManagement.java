package de.oszimt;

import de.oszimt.conzept.impl.Concept;
import de.oszimt.factory.PersistanceFactory;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.ui.Gui;

/**
 * Created by Philipp on 17.09.14.
 */
public class UserManagement {
    public static void main(String[] args) {
        new Gui(new Concept(PersistanceFactory.buildPersistance(PersistanceMethod.MONGODB)));
        //System.out.println("xs");
    }
}
