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
	private final SimpleStringProperty vorname;
    private final SimpleStringProperty nachname;
    private final SimpleStringProperty ort;
    private final SimpleStringProperty strasse;
    private final SimpleStringProperty strassenNummer;
    private final SimpleIntegerProperty plz;
    private final SimpleObjectProperty<LocalDate> geburtstag;
    
    public User(String vorname, String nachname, LocalDate geburtstag,
                String ort, String strasse, String strassenNummer, int plz) {
		super();
		this.vorname 		= new SimpleStringProperty(vorname);
		this.nachname 		= new SimpleStringProperty(nachname);
		this.ort 			= new SimpleStringProperty(ort);
		this.strasse 		= new SimpleStringProperty(strasse);
		this.strassenNummer = new SimpleStringProperty(strassenNummer);
		this.plz 			= new SimpleIntegerProperty(plz);
		this.geburtstag 	= new SimpleObjectProperty<LocalDate>(geburtstag);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVorname(){
    	return vorname.get();
    }
    
    public void setVorname(String name){
    	vorname.set(name);
    }
    
    public String getNachname(){
    	return nachname.get();
    }
    
    public void setNachname(String name){
    	nachname.set(name);
    }
    
    public String getOrt(){
    	return ort.get();
    }
    
    public void setOrt(String name){
    	ort.set(name);
    }
    
    public String getStrasse(){
    	return strasse.get();
    }
    
    public void setStrasse(String name){
    	strasse.set(name);
    }
    
    public String getStrassenNummer(){
    	return strassenNummer.get();
    }
    
    public void setStrassenNummer(String nummer){
    	strassenNummer.set(nummer);
    }
    
    public int getPlz(){
    	return plz.get();
    }
    
    public void setPLZ(int plz){
    	this.plz.set(plz);
    }
    
    public LocalDate getGeburtstag(){
    	return geburtstag.get();
    }
    
    public void setGeburtstag(LocalDate geb){
    	geburtstag.set(geb);
    }
 
    public StringProperty getVorProp(){
    	return this.vorname;
    }

	@Override
    public String toString(){
    	return vorname.getValue() + " " + nachname.getValue();
    }
    
    @Override
	public boolean equals(Object obj){
    	return obj instanceof User && obj != null ? this.hashCode() == obj.hashCode() : false;
    }
    
    @Override
	public int hashCode(){
    	return ("" + 
    			this.vorname + 
    			this.nachname + 
    			this.ort + 
    			this.plz + 
    			this.strasse + 
    			this.strassenNummer + 
    			this.geburtstag.getValue().getDayOfMonth() + 
    			this.geburtstag.getValue().getMonthValue() + 
    			this.geburtstag.getValue().getYear()
    			).hashCode();
    }
}
