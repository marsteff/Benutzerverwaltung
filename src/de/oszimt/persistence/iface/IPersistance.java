package de.oszimt.persistence.iface;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import javafx.collections.ObservableList;

import java.util.List;

public interface IPersistance {
	void upsertUser(User user);
	void deleteUser(User user);
	void createUser(User user);
	List<User> getAllUser();
    void createDepartment(Department dep);
    void updateDepartment(Department dep);
    void remoteDepartment(Department dep);
    List<Department> getAllDepartments();

}
