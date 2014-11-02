package de.oszimt.concept.iface;

import de.oszimt.model.Department;
import de.oszimt.model.User;

import java.util.List;

/**
 * Hier wird die Schnittstelle zwischen dem Fachkonzept und
 * der Anzeige (UI) definiert
 */
public interface IConcept {
    /**
     * @return Gibt den Title der Anwendung zurück
     */
    String getTitle();

    /**
     * Löscht einen Benutzer aus der Datenhaltung
     * @param user zu löschenen Benutzer
     */
    void deleteUser(User user);

    /**
     * Erstellt einen Neuen User
     * Benutzer Objekt wird in eine Map umgewandelt und dann
     * zu speichern an die Datenhaltungschicht weitergegeben
     * @param user zuspeichernde User Instanze
     */
    void createUser(User user);

    /**
     * Erstellt einen Benutzer insofern er noch nicht existsiert, andererseits werden
     * die Benutzerdetails aktualisiert
     * @param user Benutzer
     */
    void upsertUser(User user);

    /**
     * Erstellen von Zufall-Benutzern
     * Zu Test- und Demonstrationszwecken können zufällige Benutzer
     * angelegt werden.
     *
     * @notice Bei Benutzung REST Paramter muss Internet vorhanden sein.
     * Hier wird eine realistische Zuordnung von PLZ zu Stadt generiert.
     * Das Abfragen der Städte geschieht mittels eines REST Services
     * @param useRest REST Service an (true) oder aus (false)
     */
    void createRandomUsers(boolean useRest);

    /**
     * Erstllen einer neuen Abteilung
     * wird an die Datenhaltung weitergeleited
     * @param name Name der zuerstellenden Abteilung
     */
    void createDepartment(String name);

    /**
     * erstellt eine neue Abteilung oder ändert eine bestehende
     * @param department Abteilugs Instanze
     */
    void upsertDepartment(Department department);

    /**
     * Gibt eine Abteilung anhand Ihrer id
     * @param id Id der Abteilung
     * @return Abteilung Instanze
     */
    Department getDepartmentById(int id);

    /**
     * Gibt eine Liste von Benutzern anhand einer Abteilung
     * @param dep Abteiungs Instanze
     * @return Liste von Benutzer Instanzen
     */
    List<User> getUsersByDepartment(Department dep);

    /**
     * löscht eine Abteilung
     * @param department zulöschende Abteilung
     */
    void deleteDepartment(Department department);

    /**
     * Gibt eine Liste aller Abteilungen zurück
     * @return List von Abteilungen
     */
    List<Department> getAllDepartments();

    /**
     * Gibt eine Liste aller Benutzer zurück
     * @return Liste aller Benuter
     */
    List<User> getAllUser();

    /**
     * Gibt einen einzlnen Benutzer anhand seiner ID zurück
     * @param id Benutzer id
     * @return Benutzer Instanze
     */
    User getUser(int id);
}
