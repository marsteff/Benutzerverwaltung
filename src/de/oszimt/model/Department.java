package de.oszimt.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Hier wird eine Abteilung abgebildet
 * Abteilungen bestehen in dieser Anwendung
 * aus einer ID, einem Namen und der Anzahl der
 * enthaltenden Benutzer
 */
public class Department {
    /**
     * id der Abteilung
     */

    private SimpleIntegerProperty id;

    /**
     * Name der Abteilung
     */
    private SimpleStringProperty name;

    /**
     * Anzahl der Mitarbeiter in der Abteilung
     */
    private SimpleIntegerProperty amount;

    /**
     * Parametrisierter Konstruktor zum erzeugen
     * einer Abteilung allein durch den Namen
     * @param name Name der Abteilung
     */
    public Department(String name) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleIntegerProperty();
        this.amount = new SimpleIntegerProperty();
    }

    /**
     * Parametrisierter Konstruktor zum erzeugen
     * einer Abteilung durch den Namen und der ID
     * @param id ID der Abteilung
     * @param name Name der Abteilung
     */
    public Department(int id, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleIntegerProperty();
    }

    /**
     * Parametrisierter Konstruktor zum erzeugen
     * einer Abteilung durch den Namen, der ID
     * und der Anzahl der enthaltenen Mitarbeiter
     * @param id ID der ID der Abteilung
     * @param name Name der Abteilung
     * @param amount Anzahl der enthaltenden Benutzer
     */
    public Department(int id, String name, int amount) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleIntegerProperty(amount);
    }

    /**
     * Gibt die Id der Abteilung zurück
     * @return ID der Abteilung
     */
    public int getId() {
        return id.get();
    }

    /**
     * Setzt die Id der Abteilung
     * @param id ID der Abteilung
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Gibt den Namen der Abteilung zurück
     * @return Name der Abteilung
     */
    public String getName() {
        return name.get();
    }

    /**
     * Setzt den Namen der Abteilung
     * @param name Name der Abteilung
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Gibt die Anzahl der Mitarbeiter der
     * Abteilung zurück
     * @return Anzahl der enthaltenden Benutzer
     */
    public int getAmount() {
        return amount.get();
    }

    /**
     * Setzt die Anzahl der Mitarbeiter der
     * Abteilung
     * @param amount Anzahl der enthaltenden Benutzer
     */
    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    /**
     * Überschreibt die toString Methode
     * @return Name der Abteilung
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Überschreibt die equals Methode
     * @param o Vergleichs Objekt
     * @return true: ist gleich, false: ist ungleich
     */
    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof Department && this.hashCode() == o.hashCode();
    }


    /**
     * create unqiue number related to id and name hash number
     * @see "http://en.wikipedia.org/wiki/Pairing_function#Cantor_pairing_function"
     */
    @Override
    public int hashCode() {
        int x = id.get();
        int y = name.get().hashCode();
        return (1/2)*(x+y)*(x+y+1)+y;
    }
}
