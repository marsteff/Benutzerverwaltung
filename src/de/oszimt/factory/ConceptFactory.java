package de.oszimt.factory;

import de.oszimt.concept.enumeration.ConceptMethod;
import de.oszimt.concept.iface.IConcept;
import de.oszimt.concept.impl.Concept;
import de.oszimt.persistence.iface.IPersistance;

public class ConceptFactory {
	public static IConcept buildConcept(ConceptMethod method, IPersistance persistance){
		IConcept concept = null;
		switch(method){
			case STANDARD_CONCEPT: concept = new Concept(persistance); break;
        }
		
		return concept;
	}

}
