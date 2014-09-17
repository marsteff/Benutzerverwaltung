package de.oszimt.persistence.iface;

import javafx.collections.ObservableList;
import de.oszimt.model.Kunde;

public interface IPersistance {
	void updateUser(Kunde kunde);
	void deleteUser(Kunde kunde);
	void createUser(Kunde kunde);
	ObservableList<Kunde> getAllKunden();
}
