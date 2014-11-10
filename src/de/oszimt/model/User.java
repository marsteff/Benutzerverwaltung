package de.oszimt.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;

/**
 * Hier wird ein Benutzer abgebildet
 * Benutzer bestehen in dieser Anwendung
 * aus einer ID, einem Vor- und Nachnamen, der Adresse,
 * des Geburtstags und der Anbteilungen
 */
public class User {

    /**
     * Vorname des Benutzers
     */
	private final SimpleStringProperty firstname;

    /**
     * Nachname des Benutzers
     */
    private final SimpleStringProperty lastname;

    /**
     * Stadt in welcher der Benutzer lebt
     */
    private final SimpleStringProperty city;

    /**
     * Strasse der Wohnadresse
     */
    private final SimpleStringProperty street;

    /**
     * Hausnummer der Wohnadresse
     */
    private final SimpleStringProperty streetnr;

    /**
     * Postleitzahl der Wohnadresse
     */
    private final SimpleIntegerProperty zipcode;

    /**
     * Abteilungs Instanze
     * der Abteilung in welcher der Benutzer arbeitet
     */
    private final SimpleObjectProperty<Department> department;

    /**
     * Geburtsdatum des Benutzers
     */
    private final SimpleObjectProperty<LocalDate> birthday;

    /**
     * CombobBox mit Departments für das löschen einer Abteilung
     */
    private SimpleObjectProperty<ComboBox<Department>> combobox;

    /**
     * ID eines Benutzers
     */
    private SimpleIntegerProperty id;
    

    /**
     * Voll parametrisierter Konstruktor
     * @param fname Vorname
     * @param lname Nachname
     * @param bday Geburtstag
     * @param city Stadt der Wohnadresse
     * @param street Strasse der Wohnadresse
     * @param streetnr Hausnummer der Wohnadresse
     * @param zipcode Postleitzahl der Wohnadresse
     * @param department Abteilung in welcher der Benutzer arbeitet
     */
    public User(String fname, String lname, LocalDate bday,
                String city, String street, String streetnr, int zipcode,
                Department department) {
        //setzten der übergebenden Werte
		this.firstname = new SimpleStringProperty(fname);
		this.lastname = new SimpleStringProperty(lname);
		this.city = new SimpleStringProperty(city);
		this.street = new SimpleStringProperty(street);
		this.streetnr = new SimpleStringProperty(streetnr);
		this.zipcode = new SimpleIntegerProperty(zipcode);
		this.birthday = new SimpleObjectProperty<>(bday);
        this.department = new SimpleObjectProperty<>(department);
        this.combobox = new SimpleObjectProperty<ComboBox<Department>>();
        this.id = new SimpleIntegerProperty();
	}

    /**
     * SimpleProperty Getter
     * @return Vorname
     */
    public SimpleStringProperty firstnameProperty(){
        return firstname;
    }

    /**
     * SimpleProperty Getter
     * @return Nachname
     */
    public SimpleStringProperty lastnameProperty(){
        return lastname;
    }

    /**
     * SimpleProperty Getter
     * @return Stadt der Wohnadresse
     */
    public SimpleStringProperty cityProperty(){
        return city;
    }

    /**
     * SimpleProperty Getter
     * @return Strasse der Wohnadresse
     */
    public SimpleStringProperty streetProperty(){
        return street;
    }

    /**
     * SimpleProperty Getter
     * @return Hausnummer der Wohnadresse
     */
    public SimpleStringProperty streetnrProperty(){
        return streetnr;
    }

    /**
     * SimpleProperty Getter
     * @return Postleitzahl der Wohnadresse
     */
    public SimpleIntegerProperty zipcodeProperty(){
        return zipcode;
    }

    /**
     * SimpleProperty Getter
     * @return Geburtstag
     */
    public SimpleObjectProperty<LocalDate> birthdayProperty(){
        return birthday;
    }

    /**
     * SimpleProperty Getter
     * @return Abteilung
     */
    public SimpleObjectProperty<Department> departmentProperty(){
        return department;
    }


    /**
     * Gibt die Id des Benutzers
     * @return Id des Benutzers
     */
	public int getId() {
		return id.get();
	}

    /**
     * Setzt die Id des Benutzers
     * @param id Id des Benutzers
     */
	public void setId(int id) {
		this.id.set(id);
	}

    /**
     * Gibt den Vorname des Benutzers
     * @return Vorname des Benuters
     */
	public String getFirstname(){
    	return firstname.get();
    }

    /**
     * Setzt den Vornamen des Benutzers
     * @param name Vorname des Benutzers
     */
    public void setFirstname(String name){
    	firstname.set(name);
    }

    /**
     * Gibt den Nachnamen des Benutzers
     * @return Nachnamen des Benutzers
     */
    public String getLastname(){
    	return lastname.get();
    }

    /**
     * Setzt den Nachnamen des Benutzers
     * @param name Nachname des Benutzers
     */
    public void setLastname(String name){
    	lastname.set(name);
    }

    /**
     * Gibt die Stadt der Wohnadresse
     * @return Stadt der Wohnadresse
     */
    public String getCity(){
    	return city.get();
    }

    /**
     * Setzt die Stadt der Wohnadresse
     * @param name Stadt der Wohnadresse
     */
    public void setCity(String name){
    	city.set(name);
    }

    /**
     * Gibt die Strasse der Wohnadresse
     * @return Strasse der Wohnadresse
     */
    public String getStreet(){
    	return street.get();
    }

    /**
     * Setzt die Strasse der Wohnadresse
     * @param name
     */
    public void setStreet(String name){
    	street.set(name);
    }

    /**
     * Gibt die Hausnummer der Wohnadresse
     * @return Hausnummer der Wohnadresse
     */
    public String getStreetnr(){
    	return streetnr.get();
    }

    /**
     * Setzt die Hausnummer der Wohnadresse
     * @param number Hausnummer der Wohnadresse
     */
    public void setStreetnr(String number){
    	streetnr.set(number);
    }

    /**
     * Gibt die Postleitzahl der Wohnadresse
     * @return Postleitzahl der Wohnadresse
     */
    public int getZipcode(){
    	return zipcode.get();
    }

    /**
     * Setzt die Postleitzahl der Wohnadresse
     * @param zipcode Postleitzahl der Postleitzahl der Wohnadresse
     */
    public void setZipcode(int zipcode){
    	this.zipcode.set(zipcode);
    }

    /**
     * Gibt das Geburtsdatum des Benutzers
     * @return Geburtsdatum des Benutzers
     */
    public LocalDate getBirthday(){
    	return birthday.get();
    }

    /**
     * Setzt das Geburtsdatum des Benutzers
     * @param geb Geburtsdatum des Benutzers
     */
    public void setBirthday(LocalDate geb){
    	birthday.set(geb);
    }

    /**
     * Gibt die Abteilung in welcher der Benutzer arbeitet
     * @return Abteilung
     */
    public Department getDepartment(){
        return this.department.get();
    }

    /**
     * Setzt die Abteilung in welcher der Benutzer arbeitet
     * @param department Abteilung in welcher der Benutzer arbeitet
     */
    public void setDepartment(Department department){
        this.department.set(department);
    }

    /**
     * Combobox Getter
     * @return Combobox der Abteilung
     */
    public ComboBox<Department> getCombobox() {
        return combobox.get();
    }

    /**
     * Setzt die Abteilungs Combobox
     * @param combobox Abteilungs Combobox
     */
    public void setCombobox(ComboBox<Department> combobox) {
        this.combobox.set(combobox);
    }
    
    /**
     * Überschreibt die toString Methode
     * @return Vor- und Nachname des Benutzers
     */
	@Override
    public String toString(){
    	return firstname.getValue() + " " + lastname.getValue();
    }

    /**
     * Überschreibt die equals Methode
     * @param obj Vergleichs Objekt
     * @return true: ist gleich, false: ist ungleich
     */
    @Override
	public boolean equals(Object obj){
    	return obj != null && obj instanceof User && this.hashCode() == obj.hashCode();
    }

    /**
     * Überschreibt die hashCode Methode
     * @return Hashcode Number
     */
    @Override
	public int hashCode(){
    	return ("" + 
    			this.firstname +
    			this.lastname +
    			this.city +
    			this.zipcode +
    			this.street +
    			this.streetnr +
    			this.birthday.getValue().getDayOfMonth() +
    			this.birthday.getValue().getMonthValue() +
    			this.birthday.getValue().getYear()
    			).hashCode();
    }
}