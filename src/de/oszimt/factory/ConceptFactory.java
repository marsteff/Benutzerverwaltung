package de.oszimt.factory;

import de.oszimt.concept.enumeration.ConceptMethod;
import de.oszimt.concept.iface.IConcept;
import de.oszimt.concept.impl.Concept;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.persistence.iface.IPersistance;

/**
 * Design Pattern Factory
 *
 * Anhand des ConceptMethod Emuns können Instanzen der gewüschten
 * Konzept Klasse erzeugt/zurückgegeben werden
 */
public class ConceptFactory {
    /**
     * Statische Methode, gibt eine Instanze der gewünschten Klasse zurück
     * @param method Konzeptwahl (de.oszimt.concept.enumeration.ConceptMethod)
     * @param persistance Datenhaltungswahl (de.oszimt.persisitence.enumeration.PersistanceMethod)
     * @return Konzept Instanze
     */
	public static IConcept buildConcept(ConceptMethod method, IPersistance persistance){
		IConcept concept = null;
		switch(method){
			case STANDARD_CONCEPT: concept = new Concept(persistance); break;
        }
		return concept;
	}

    /**
     * Statische Methode, gibt eine Instanze der gewünschten Klasse zurück
     *
     * Kurzschreibweise: Nimmt auch einen Enum Wert für die Datenhaltung entgegen
     *
     * @param method Konzeptwahl (de.oszimt.concept.enumeration.ConceptMethod)
     * @param persistanceMethod Datenhaltungswahl (de.oszimt.persisitence.enumeration.PersistanceMethod)
     * @return Konzept Instanze
     */
    public static IConcept buildConcept(ConceptMethod method, PersistanceMethod persistanceMethod){
        IConcept concept = null;
        switch(method){
            case STANDARD_CONCEPT: concept = new Concept(
                    PersistanceFactory.buildPersistance(
                        persistanceMethod
                    )
                ); break;
        }
        return concept;
    }
}
