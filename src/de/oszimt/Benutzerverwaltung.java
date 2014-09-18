package de.oszimt;

import de.oszimt.concept.enumeration.ConceptMethod;
import de.oszimt.factory.ConceptFactory;
import de.oszimt.factory.PersistanceFactory;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.ui.Tui;

/**
 * Created by Philipp on 17.09.14.
 */
public class Benutzerverwaltung {
    public static void main(String[] args) {
//        new Gui(new Concept(PersistanceFactory.buildPersistance(PersistanceMethod.SQLITE)));
        new Tui(ConceptFactory.buildConcept(ConceptMethod.STANDARD_CONCEPT, PersistanceFactory.buildPersistance(PersistanceMethod.SQLITE)));
    }
}
