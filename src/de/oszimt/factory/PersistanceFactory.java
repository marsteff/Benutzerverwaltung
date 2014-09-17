package de.oszimt.factory;

import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.persistence.impl.MongoDbPersistance;
import de.oszimt.persistence.impl.SQLitePersistance;

public class PersistanceFactory {
	public static IPersistance buildPersistance(PersistanceMethod method){
		IPersistance persistance = null;
		switch(method){
			case SQLITE: persistance = SQLitePersistance.getInstance(); break;
			case MONGODB: persistance = MongoDbPersistance.getInstance(); break;
		}
		
		return persistance;
	}

}
