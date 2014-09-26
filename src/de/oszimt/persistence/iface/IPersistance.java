package de.oszimt.persistence.iface;
import java.util.List;
import java.util.Map;

public interface IPersistance {
    String getKeyUserId();
    String getKeyUserFirstname();
    String getKeyUserLastname();
    String getKeyUserCity();
    String getKeyUserStreet();
    String getKeyUserStreetNr();
    String getKeyUserZipCode();
    String getKeyUserBirthday();
    String getKeyDepartmentId();
    String getKeyDepartmentName();
    String getKeyUserDepartment();
	void upsertUser(Map<String,Object> user);
	void deleteUser(int id);
	void createUser(Map<String,Object> user);
	List<Map<String,Object>> getAllUser();
    void createDepartment(String name);
    void updateDepartment(Map<String,Object> dep);
    void removeDepartment(int id);
    Map<String,Object> getUserById(int id);
    List<Map<String,Object>> getAllDepartments();
}
