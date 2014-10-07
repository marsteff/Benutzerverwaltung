package de.oszimt;

import de.oszimt.concept.enumeration.ConceptMethod;
import de.oszimt.factory.ConceptFactory;
import de.oszimt.factory.PersistanceFactory;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.ui.Gui;
import de.oszimt.ui.Tui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;

/**
 * Startet die Anwendung
 */
public class Benutzerverwaltung {
    public static void main(String[] args) {
        new Tui(
                ConceptFactory.buildConcept(
                        ConceptMethod.STANDARD_CONCEPT,
                        PersistanceMethod.SQLITE
                )
        );
    }
}
