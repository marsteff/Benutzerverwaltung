package de.oszimt.model;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {

	private int id;
    //TODO department ID überhaupt notwendig, wenn man über das Department Object drauf zugreifen kann
    private int department_id;
	private final SimpleStringProperty firstname;
    private final SimpleStringProperty lastname;
    private final SimpleStringProperty city;
    private final SimpleStringProperty street;
    private final SimpleStringProperty streetnr;
    private final SimpleIntegerProperty zipcode;
    private final SimpleObjectProperty<Department> department;
    private final SimpleObjectProperty<LocalDate> birthday;
    
    public User(String fname, String lname, LocalDate bday,
                String city, String street, String streetnr, int zipcode,
                Department department) {
		this.firstname = new SimpleStringProperty(fname);
		this.lastname = new SimpleStringProperty(lname);
		this.city = new SimpleStringProperty(city);
		this.street = new SimpleStringProperty(street);
		this.streetnr = new SimpleStringProperty(streetnr);
		this.zipcode = new SimpleIntegerProperty(zipcode);
		this.birthday = new SimpleObjectProperty<LocalDate>(bday);
        this.department = new SimpleObjectProperty<Department>(department);
        this.department_id = department.getId();
	}

    public User(){}

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

    public Department getDepartment(){
        return this.department.get();
    }

    public void setDepartment(Department department){
        this.department.set(department);
        this.department_id = this.department.get().getId();
    }

    public int getDepartmentId(){
        return this.department_id;
    }

    public void setDepartmentId(int id){
        this.department_id = id;
    };

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
