package de.oszimt.factory;

import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.persistence.impl.RelationalDatabasePersistance;

public class PersistanceFactory {
	public static IPersistance buildPersistance(PersistanceMethod method){
		IPersistance persistance = null;
		switch(method){
			case RELATIONAL: persistance = RelationalDatabasePersistance.getInstance(); break;
		}
		
		return persistance;
	}

}
