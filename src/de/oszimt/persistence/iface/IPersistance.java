package de.oszimt.persistence.iface;

import java.util.List;
import java.util.Map;

/**
 * Schnittstelle zur Datenhaltung
 */
public interface IPersistance {
    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Nummer zurück
     * @return String
     */
    String getKeyUserId();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzervornamens zurück
     * @return String
     */
    String getKeyUserFirstname();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzernachnamens zurück
     * @return String
     */
    String getKeyUserLastname();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Stadt zurück
     * @return String
     */
    String getKeyUserCity();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Strasse zurück
     * @return String
     */
    String getKeyUserStreet();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Strassen Nummer zurück
     * @return String
     */
    String getKeyUserStreetNr();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer PLZ zurück
     * @return String
     */
    String getKeyUserZipCode();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzergeburtstags
     * @return String
     */
    String getKeyUserBirthday();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Abteilung zurück
     * @return String
     */
    String getKeyDepartmentId();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Abteilungsnummer zurück
     * @return String
     */
    String getKeyDepartmentName();

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Abteilungsnames zurück
     * @return String
     */
    String getKeyUserDepartment();

    /**
     * Kombination aus update/insert User. Wenn ein Benutzer, welcher
     * sich aus den Werten der Map ergibt, nicht existstiert dann wird er
     * neu erzeugt. Andererseits werden alle vorhandenen werde aus der Map in
     * das Dokument übernommen
     *
     * @param user Map<String,Object>
     */
	void upsertUser(Map<String,Object> user);

    /**
     * Benutzer Dokument aus der Collection entfernen
     * @param id int
     */
	void deleteUser(int id);

    /**
     * Erstellt einen neuen Benutzer
     * @param user Map<String,Object>
     */
	void createUser(Map<String,Object> user);

    /**
     * Gibt eine Liste alle Benutzer (Maps) zurück
     * @return List<Map<String,Object>>
     */
	List<Map<String,Object>> getAllUser();

    /**
     * Neue Abteilung erzeugen
     * @param name String
     */
    void createDepartment(String name);

    /**
     * Aktualisieren einer Abteiung
     *
     * @param dep
     */
    void updateDepartment(Map<String,Object> dep);

    /**
     * Entfernen einer Abteilung
     *
     * @param id
     */
    void removeDepartment(int id);

    /**
     * Laden eines einzelnen Benutzers anhand seiner Id
     * @param id int
     * @return Map<String,Object>
     */
    Map<String,Object> getUserById(int id);

    /**
     * Gibt eine Liste aller Abteilungen zurück
     *
     * @return
     */
    List<Map<String,Object>> getAllDepartments();
}
