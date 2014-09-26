package de.oszimt.concept.iface;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.persistence.iface.IPersistance;

import java.util.List;

/**
 * Created by Marci on 18.09.2014.
 */
public interface IConcept {
    String getTitle();

    void deleteUser(User user);

    void createUser(User user);

    void upsertUser(User user);

    void createRandomUsers(boolean useRest);

    void createDepartment(String name);

    List<Department> getAllDepartments();

    List<User> getAllUser();

    User getUser(int id);

    IPersistance getPersistance();
}
