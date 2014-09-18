package de.oszimt.persistence.iface;

import de.oszimt.model.User;
import javafx.collections.ObservableList;

import java.util.List;

public interface IPersistance {
	void upsertUser(User user);
	void deleteUser(User user);
	void createUser(User user);
    void createInitTable();
	List<User> getAllUser();
}
