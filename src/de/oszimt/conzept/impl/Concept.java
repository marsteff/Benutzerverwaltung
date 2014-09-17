package de.oszimt.conzept.impl;

import de.oszimt.persistence.iface.IPersistance;

public class Concept {
    private String title = "Oszimt Projekt Benutzerverwaltung";
    private IPersistance database;

    public Concept(IPersistance db){
        this.database = db;
    }
    public String getTitle(){
        return this.title;
    }

    public boolean remmoveUser(){
        return true;
    }
}
