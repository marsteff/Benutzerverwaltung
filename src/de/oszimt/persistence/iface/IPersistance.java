package de.oszimt.persistence.iface;

import de.oszimt.model.User;
import javafx.collections.ObservableList;

public interface IPersistance {
	void upsertUser(User user);
	void deleteUser(User user);
	void createUser(User user);
	ObservableList<User> getAllUser();
}
