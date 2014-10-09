package de.oszimt.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Hier wird eine Abteilung abgebildet
 * Abteilungen bestehen in dieser Anwendung
 * aus einer ID und einem Namen
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
     * @param name
     */
    public Department(String name) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleIntegerProperty();
        this.amount = new SimpleIntegerProperty();
    }

    /**
     * Parametrisierter Konstruktor zum erzeugen
     * einer Abteilung durch den Namen und der ID
     *
     * @param id
     * @param name
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
     *
     * @param id
     * @param name
     * @param amount
     */
    public Department(int id, String name, int amount) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleIntegerProperty();
    }

    /**
     * Gibt die Id der Abteilung zurück
     * @return
     */
    public int getId() {
        return id.get();
    }

    /**
     * Setzt die Id der Abteilung
     * @param id
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Gibt den Namen der Abteilung zurück
     * @return
     */
    public String getName() {
        return name.get();
    }

    /**
     * Setzt den Namen der Abteilung
     * @param name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Gibt die Anzahl der Mitarbeiter der
     * Abteilung zurück
     * @return
     */
    public int getAmount() {
        return amount.get();
    }

    /**
     * Setzt die Anzahl der Mitarbeiter der
     * Abteilung
     * @param amount
     */
    public void setAmount(int amount) {
        this.amount.set(amount);
    }

    /**
     * Überschreibt die toString Methode
     * @return
     */
    @Override
    public String toString() {
        return getName();
    }
}
