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


public class Benutzerverwaltung {
    public static void main(String[] args) {
        new Gui(
                ConceptFactory.buildConcept(
                        ConceptMethod.STANDARD_CONCEPT,
                        PersistanceFactory.buildPersistance(
                                PersistanceMethod.SQLITE
                        )
                )
        );

/*
        MongoDbPersistance m;
        try {
            m = MongoDbPersistance.getInstance();
            m.getAllUser();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/
    }
}
