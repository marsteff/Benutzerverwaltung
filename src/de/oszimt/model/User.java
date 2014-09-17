package de.oszimt.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Marci on 03.07.2014.
 */
public class User {

	private int id;
	private final SimpleStringProperty firstname;
    private final SimpleStringProperty lastname;
    private final SimpleStringProperty city;
    private final SimpleStringProperty street;
    private final SimpleStringProperty streetnr;
    private final SimpleIntegerProperty zipcode;
    private final SimpleObjectProperty<LocalDate> birthday;
    
    public User(String fname, String lname, LocalDate bday,
                String city, String street, String streetnr, int zipcode) {
		super();
		this.firstname = new SimpleStringProperty(fname);
		this.lastname = new SimpleStringProperty(lname);
		this.city = new SimpleStringProperty(city);
		this.street = new SimpleStringProperty(street);
		this.streetnr = new SimpleStringProperty(streetnr);
		this.zipcode = new SimpleIntegerProperty(zipcode);
		this.birthday = new SimpleObjectProperty<LocalDate>(bday);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname(){
    	return firstname.get();
    }
    
    public void setFirstname(String name){
    	firstname.set(name);
    }
    
    public String getLastname(){
    	return lastname.get();
    }
    
    public void setLastname(String name){
    	lastname.set(name);
    }
    
    public String getCity(){
    	return city.get();
    }
    
    public void setCity(String name){
    	city.set(name);
    }
    
    public String getStreet(){
    	return street.get();
    }
    
    public void setStreet(String name){
    	street.set(name);
    }
    
    public String getStreetnr(){
    	return streetnr.get();
    }
    
    public void setStreetnr(String number){
    	streetnr.set(number);
    }
    
    public int getZipcode(){
    	return zipcode.get();
    }
    
    public void setPLZ(int plz){
    	this.zipcode.set(plz);
    }
    
    public LocalDate getBirthday(){
    	return birthday.get();
    }
    
    public void setBirthday(LocalDate geb){
    	birthday.set(geb);
    }
 
    public StringProperty getVorProp(){
    	return this.firstname;
    }

	@Override
    public String toString(){
    	return firstname.getValue() + " " + lastname.getValue();
    }
    
    @Override
	public boolean equals(Object obj){
    	return obj instanceof User && obj != null ? this.hashCode() == obj.hashCode() : false;
    }
    
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