package de.oszimt.model;

/**
 * Hier wird eine Abteilung abgebildet
 * Abteilungen bestehen in dieser Anwendung
 * aus einer ID und einem Namen
 */
public class Department {
    /**
     * id der Abteilung
     */
    private int id;
    /**
     * Name der Abteilung
     */
    private String name;

    /**
     * Parametrisierter Konstruktor zum erzeugen
     * einer Abteilung allein durch den Namen
     * @param name
     */
    public Department(String name) {
        this.name = name;
    }

    /**
     * Parametrisierter Konstruktor zum erzeugen
     * einer Abteilung durch den Namen und der ID
     *
     * @param id
     * @param name
     */
    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gibt die Id der Abteilung zurück
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setzt die Id der Abteilung
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gibt den Namen der Abteilung zurück
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Abteilung
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
