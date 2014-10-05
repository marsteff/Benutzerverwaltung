package de.oszimt.util;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Util {
	
	/**
	 * Stupide Erzeugung von Zufallskunden mit richtiger PLZ und Stadt, wenn userest true ist
	 */
	public static void createCustomers(boolean userest, IConcept concept){
        RestService service = null;

        if(userest){
		   service = new RestService();
        }

        //Holden der erstellten Abteilungen aus der Datenhaltung.
        //Notwendig da die generierten IDs benötigt werden
        List<Department> departments = concept.getAllDepartments();

        //Dem Zufall bereitstehende Vornamen
        String[] firstnames = {
                "Dieter", "Ralf", "Bernd", "Matthias", "Tilo", "Steffen", "Marcus"
        };
        //Dem Zufall bereitstehende Nachnamen
        String[] lastnames = {
                "Müller", "Heinz", "Kunt", "Schumacher", "Bäcker", "Münzberg", "Dahse"
        };
        //Dem Zufall bereitstehende Strassennamen
        String[] streets = {
                "Achenbachstraße", "Bockenheimer Warte", "Galvanistraße",
                "Peter-Böhler-Straße", "Tillystraße", "Triftstraße", "Zentmarkweg"
        };
        //Dem Zufall bereitstehende Städte (nur bei useRest == false relevant)
        String[] cities = {
                "Berlin", "Hambung", "Perleberg",
                "Schwerin", "München", "Flensbug", "Potsdam"
        };

        //Zufallsgeber
        Random rand = new Random();

        //Zufallsbenutzer anlegen
        for(int i = 0; i < 50; i++){
            //ort und PLZ definieren
            String ort = null;
            int plz = 0;
            do{
                plz = rand.nextInt(80000)+10000;
                ort = userest && service != null ?
                        service.getTown(plz + "") : cities[rand.nextInt(cities.length)];
            } while(ort.equals(""));

            //Benutzer über die Datenhaltung speichern
            concept.createUser(
                    //Benutzerinstance erzeugen
                    new User(
                            firstnames[rand.nextInt(firstnames.length)],
                            lastnames[rand.nextInt(lastnames.length)],
                            //brithday
                            LocalDate.now()
                                    .minusYears(rand.nextInt(60)+18)
                                    .minusDays(rand.nextInt(30))
                                    .minusMonths(rand.nextInt(12)),
                            ort,
                            streets[rand.nextInt(streets.length)],
                            rand.nextInt(99) + Integer.toHexString(rand.nextInt(15)),
                            plz,
                            departments.get(rand.nextInt(departments.size()))
                    )
            );
        }
	}

    public static void createDepartments(IConcept concept){
        //Prüfen, ob bereits Departments in der Datenbank enthalten sind, wenn nein, neu erzeugen
        List<Department> checkList = concept.getAllDepartments();
        if (checkList == null || checkList.size() == 0) {

            //Dem Zufall bereitstehende Abteilung
            String[] department_names = new String[]{
                    "Geschäftsführung","Entwicklung","Marketing",
                    "Rechnungsabteilung", "Kundenservice", "Verkauf",
                    "Logistik","Lager", "Fahrer","Projektmanagement"
            };

            //Abteilung in der Datenhaltung anlegen
            for(String dep : department_names){
                concept.createDepartment(dep);
            }
        }
    }
}
