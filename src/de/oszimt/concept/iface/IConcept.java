package de.oszimt.concept.iface;

import de.oszimt.model.Department;
import de.oszimt.model.User;

import java.util.List;

/**
 * Created by Marci on 18.09.2014.
 */
public interface IConcept {
    String getTitle();

    boolean deleteUser(User user);

    boolean createUser(User user);

    boolean upsertUser(User user);

    void createRandomUsers(boolean useRest);

    List<Department> getAllDepartments();

    List<User> getAllUser();

    User getUser(int id);
}
