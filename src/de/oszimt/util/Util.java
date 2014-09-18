package de.oszimt.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Util {
	
	/**
	 * Stupide Erzeugung von Zufallskunden mit richtiger PLZ und Stadt
	 * @return neu erzeugten Kunden
	 */
	public static ObservableList<User> createCustomers(boolean userest, List<Department> departments){
        RestService service = null;

        if(userest){
		   service = new RestService();
        }
		
		String[] vornamen = {"Dieter", "Ralf", "Bernd", "Matthias", "Tilo", "Steffen", "Marcus"};
		String[] nachnamen = {"Müller", "Heinz", "Kunt", "Schumacher", "Bäcker", "Münzberg", "Dahse"};
		String[] strassen = {"Achenbachstraße", "Bockenheimer Warte", "Galvanistraße",
                "Peter-Böhler-Straße", "Tillystraße", "Triftstraße", "Zentmarkweg"};
		
		ObservableList<User> liste = FXCollections.observableArrayList();
		Random rand = new Random();
		for(int i = 0; i < 50; i++){
			String ort = null;
			int plz = 0;
			do{
				plz = rand.nextInt(80000)+10000;
				ort = userest && service != null ? service.getTown(new String(plz + "")) : "Berlin";
			} while(ort.equals(""));
			
			liste.add(new User(vornamen[rand.nextInt(vornamen.length)],
			                    nachnamen[rand.nextInt(nachnamen.length)],
			                    LocalDate.now().minusYears(rand.nextInt(60)+18).minusDays(rand.nextInt(30)).minusMonths(rand.nextInt(12)),
			                    ort,
			                    strassen[rand.nextInt(strassen.length)],
			                    new String("" + rand.nextInt(200)),
			                    plz,
                                departments.get(rand.nextInt(departments.size()))
			                    ));
		}
		return liste;
	}

}
