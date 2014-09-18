package de.oszimt.persistence.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.oszimt.persistence.iface.IPersistance;

public class SQLitePersistance implements IPersistance {

	private static SQLitePersistance obj;
	private static final String DATABASEPATH = "SQLiteDatabase.db";
	
	private SQLitePersistance() {
		this.createTables();
	}
	
	public static SQLitePersistance getInstance(){
		if(obj == null)
			obj = new SQLitePersistance();
		return obj;
	}
	
	private Connection getConnection(){
		Connection connection = null;
		try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASEPATH);
			connection.setAutoCommit(false);
		} catch (Exception e) {
			System.err.println("bei Erstellung der Verbindung ist was schief gelaufen");
		}
		
		return connection;
	}

	
	private void createTables(){
        String User_sql = "CREATE TABLE IF NOT EXISTS User " +
                      "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                      " first_name TEXT NOT NULL, " +
                      " last_name TEXT NOT NULL, " +
                      " city TEXT NOT NULL, " +
                      " street TEXT NOT NULL, " +
                      " street_nr TEXT NOT NULL, " +
                      " zip_code INTEGER NOT NULL, " +
                      " birthday TEXT NOT NULL," +
                      " department_id INTEGER NOT NULL)";

        String Department_sql = "CREATE TABLE IF NOT EXISTS Department " +
                            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT NOT NULL)";
        this.dbUpdate(User_sql);
        this.dbUpdate(Department_sql);

	}
	
	@Override
	public void upsertUser(User user) {
		if(existCustomer(user))
			update(user);
		else
			createUser(user);

	}

	private void update(User user) {

        this.dbUpdate("UPDATE User set " +
                "first_name 		= '" + user.getFirstname() + "', " +
                "last_name		= '" + user.getLastname() + "', " +
                "city 			= '" + user.getCity() + "', " +
                "street 		= '" + user.getStreet() + "', " +
                "street_nr  	= '" + user.getStreetnr() + "', " +
                "zip_code 		= " + user.getZipcode() + ", " +
                "birthday		= '" + user.getBirthday() + "'," +
                " department_id = '"+user.getDepartmentId()+"'" +
                "WHERE id = " + user.getId());

	}

    private void dbUpdate(String sql){
        Connection con = this.getConnection();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            stmt.executeUpdate(sql);
            con.commit();

        } catch(Exception e) {
            System.err.println(sql);
            System.out.println(e.getMessage());
        } finally{
            this.closeConnection(con, stmt);
            System.out.println(sql + " erfolgreich");
        }
    }

    @Override
    public void deleteUser(User user) {
        this.dbUpdate("DELETE FROM User WHERE id = " + user.getId());
    }

    public void createDepartment(Department dep){
        this.dbUpdate("INSERT INTO Department (name) VALUES ('"+dep.getName()+"')");
    }

    public void updateDepartment(Department dep){
        this.dbUpdate("UPDATE Department SET name = '"+dep.getName()+"'" +
                       "WHERE id = id = '"+dep.getId()+"'");
    }

    public void remoteDepartment(Department dep){
        this.dbUpdate("DELETE FROM Department WHERE id = id = '"+dep.getId()+"'");
    }

    public List<Department> getAllDepartments(){
        List<Department> list = new ArrayList<Department>();

        Connection con = obj.getConnection();
        Statement stmt = null;

        try{

            stmt = con.createStatement();
            String sql = "SELECT * FROM Department;";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Department dep = new Department(
                        rs.getInt("id"),
                        rs.getString("name")
                );

                list.add(dep);
            }
            rs.close();

        } catch(Exception e) {
            System.err.println("Fehler beim Laden aller Abteilungen");
        } finally{
            this.closeConnection(con, stmt);
        }
        return list;
    }

	@Override
	public void createUser(User user) {
			
	    this.dbUpdate("INSERT INTO User (first_name,last_name,city,street,street_nr,zip_code,birthday,department_id)" +
					 "VALUES (	'" + user.getFirstname() + "', " +
									"'" + user.getLastname() + "', " +
									"'" + user.getCity() + "', " +
									"'" + user.getStreet() + "', " +
									"'" + user.getStreetnr() + "', " +
									"'" + user.getZipcode() + "', " +
									"'" + user.getBirthday() + "'," +
                                    "'" + user.getDepartmentId()+"');");
	}
	
	public boolean existCustomer(User user){
		boolean exist = false;
		Connection con = this.getConnection();
		Statement stmt = null;
		
		try{
			stmt = con.createStatement();
			
			String sql = "SELECT *" +
                    "FROM User " +
                    "WHERE id = " + user.getId();
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
				exist = true;
			
			rs.close();
			
		} catch(Exception e) {
			System.err.println("Fehler beim ermitteln ob Benutzer vorhanden ist");
			System.out.println(e.getMessage());
		} finally {
			this.closeConnection(con, stmt);
		}
		
		return exist;
	}

	@Override
	public List<User> getAllUser() {
		List<User> list = new ArrayList<User>();
        String sql = "";
		Connection con = obj.getConnection();
		Statement stmt = null;
		
		try{
			
			stmt = con.createStatement();
			sql = "SELECT " +
                    "User.id, User.first_name,User.last_name,User.city,User.street, " +
                    "User.street_nr, User.zip_code,User.birthday,Department.id as department_id, " +
                    "Department.name as department_name " +
                    "FROM User " +
                    "LEFT JOIN Department ON User.department_id = Department.id;";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				User u = new User(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        LocalDate.parse(rs.getString("birthday")),
                        rs.getString("city"),
                        rs.getString("street"),
                        rs.getString("street_nr"),
                        rs.getInt("zip_code"),
                        new Department(
                            rs.getInt("department_id"),
                            rs.getString("department_name")
                        )
                );
				u.setId(rs.getInt("id"));
				list.add(u);
			}   
			rs.close();
			                                       
		} catch(Exception e) {                  
			System.err.println("Fehler beim Laden aller Benutzer: " + e.getMessage() + sql);
		} finally{
			this.closeConnection(con, stmt);
		}
		return list;
	}
	
	private void closeConnection(Connection con, Statement stmt){
		try{
			stmt.close();
			con.close();
		} catch(Exception e){
			System.err.println("Fehler beim schliessen der Verbindung");
			System.out.println(e.getMessage());
		}
	}
	
	

}
