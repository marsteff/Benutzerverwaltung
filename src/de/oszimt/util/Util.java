package de.oszimt.util;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public static boolean createDepartments(IConcept concept){
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
            return true;
        }
        return false;
    }


    /**
     * Umwandung von LocalDate zu Date
     *
     * @param date
     * @return
     */
    public static LocalDate DateToLocalDate(Date date){
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(date.getTime()),
                ZoneId.systemDefault()
        ).toLocalDate();
    }

    /**
     * Gibt den Wert einer Map zurück
     * Prüft ob der gegebene Key existsiert.
     * @notice Gibt bei fehlendem Key NULL zurück
     *
     * @param map
     * @param key
     * @return
     */
    public static <T> T readSaveFromMap(Map map, String key){
        return (T) (map.containsKey(key) ? map.get(key) : null);
    }
}
