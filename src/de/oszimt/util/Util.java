package de.oszimt.util;

import java.time.LocalDate;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import de.oszimt.model.Kunde;

public class Util {
	
	/**
	 * Stupide Erzeugung von Zufallskunden mit richtiger PLZ und Stadt
	 * @return neu erzeugten Kunden
	 */
	public static ObservableList<Kunde> createCustomers(){
		RestService service = new RestService();
		
		String[] vornamen = {"Dieter", "Ralf", "Bernd", "Matthias", "Tilo", "Steffen", "Marcus"};
		String[] nachnamen = {"M�ller", "Heinz", "Kunt", "Schumacher", "B�cker", "M�nzberg", "Dahse"};
		String[] strassen = {"Achenbachstra�e", "Bockenheimer Warte", "Galvanistra�e", "Peter-B�hler-Stra�e", "Tillystra�e", "Triftstra�e", "Zentmarkweg"};
		
		ObservableList<Kunde> liste = FXCollections.observableArrayList();
		Random rand = new Random();
		for(int i = 0; i < 50; i++){
			String ort = null;
			int plz = 0;
			do{
				plz = rand.nextInt(80000)+10000;
				ort = service.getTown(new String(plz + ""));
			} while(ort.equals(""));
			
			liste.add(new Kunde(vornamen[rand.nextInt(7)],
			                    nachnamen[rand.nextInt(7)],
			                    LocalDate.now().minusYears(rand.nextInt(60)+18).minusDays(rand.nextInt(30)).minusMonths(rand.nextInt(12)),
			                    ort,
			                    strassen[rand.nextInt(7)],
			                    new String("" + rand.nextInt(200)),
			                    plz
			                    ));
		}
		return liste;
	}

}
