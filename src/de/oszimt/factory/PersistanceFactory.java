package de.oszimt.factory;

import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.persistence.impl.MongoDbPersistance;
import de.oszimt.persistence.impl.SQLitePersistance;

import java.net.UnknownHostException;

/**
 * Design Pattern Factory
 *
 * Anhand des PersistanceMethod Emuns können Instanzen der gewüschten
 * Datenhaltungs Klasse erzeugt/zurückgegeben werden
 */
public class PersistanceFactory {
    /**
     * Statische Methode, gibt eine Instanze der gewünschten Klasse zurück
     *
     * @param method
     * @return
     */
	public static IPersistance buildPersistance(PersistanceMethod method){
        /**
         * Initialisieren einer Instanze Variable
         */
		IPersistance persistance = null;
        /**
         * Fallunterscheidung zwischen den eizelnen Datenhaltungs Typen
         * @see /src/de/oszimt/persistence/enumeration/PersistanceMethod
         */
		switch(method){
            //Füllt die Instanze Variable mit einer SQLitePersistance Instanze
			case SQLITE: persistance = SQLitePersistance.getInstance(); break;
            //Füllt die Instanze Variable mit einer MongoDbPersistance Instanze
			case MONGODB:
                try{
                    persistance = MongoDbPersistance.getInstance();
                }catch (UnknownHostException unkownHostException){
                    System.err.println(unkownHostException.getMessage());
                }
                break;
		}
		
		return persistance;
	}

}
