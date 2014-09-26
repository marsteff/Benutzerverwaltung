package de.oszimt.persistence.impl;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.oszimt.persistence.iface.IPersistance;

public class SQLitePersistance implements IPersistance {

	private static SQLitePersistance instance;
	private static final String DATABASEPATH = "SQLiteDatabase.db";
	
	private SQLitePersistance() {
		this.createTables();
	}
	
	public static SQLitePersistance getInstance(){
		if(instance == null) {
            instance = new SQLitePersistance();
        }
		return instance;
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


    @Override
    public String getKeyUserId() {
        return "id";
    }

    @Override
    public String getKeyUserFirstname() {
        return "first_name";
    }

    @Override
    public String getKeyUserLastname() {
        return "last_name";
    }

    @Override
    public String getKeyUserCity() {
        return "city";
    }

    @Override
    public String getKeyUserStreet() {
        return "street";
    }

    @Override
    public String getKeyUserStreetNr() {
        return "street_nr";
    }

    @Override
    public String getKeyUserZipCode() {
        return "zip_code";
    }

    @Override
    public String getKeyUserBirthday() {
        return "birthday";
    }


    @Override
    public String getKeyDepartmentId() {
        return "id";
    }

    @Override
    public String getKeyDepartmentName() {
        return "name";
    }

    @Override
    public String getKeyUserDepartment() {
        return "department";
    }

	
	private void createTables(){
        String User_sql = "CREATE TABLE IF NOT EXISTS User " +
                      "(" + this.getKeyUserId() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                            this.getKeyUserFirstname() + " TEXT NOT NULL, " +
                            this.getKeyUserLastname() + " TEXT NOT NULL, " +
                            this.getKeyUserCity() + " TEXT NOT NULL, " +
                            this.getKeyUserStreet() + " TEXT NOT NULL, " +
                            this.getKeyUserStreetNr() + " TEXT NOT NULL, " +
                            this.getKeyUserZipCode() + " INTEGER NOT NULL, " +
                            this.getKeyUserBirthday() + " TEXT NOT NULL," +
                            "department_id INTEGER NOT NULL)";

        String Department_sql = "CREATE TABLE IF NOT EXISTS Department " +
                            "(" +   this.getKeyDepartmentId() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    this.getKeyDepartmentName() + " TEXT NOT NULL)";
        this.dbUpdate(User_sql);
        this.dbUpdate(Department_sql);
	}

    @Override
	public void upsertUser(Map<String, Object> user) {
		if(existCustomer((int)user.get(this.getKeyUserId())))
			update(user);
		else
			createUser(user);

	}


    private void update(Map<String, Object> user) {

        this.dbUpdate("UPDATE User set " +
                "first_name 	= '" + user.get(this.getKeyUserFirstname()) + "', " +
                "last_name		= '" + user.get(this.getKeyUserLastname()) + "', " +
                "city 			= '" + user.get(this.getKeyUserCity()) + "', " +
                "street 		= '" + user.get(this.getKeyUserStreet()) + "', " +
                "street_nr  	= '" + user.get(this.getKeyUserStreetNr()) + "', " +
                "zip_code 		= " + user.get(this.getKeyUserZipCode()) + ", " +
                "birthday		= '" + user.get(this.getKeyUserBirthday()) + "'," +
                " department_id = '" + ((Map)user.get(this.getKeyUserDepartment())).get(this.getKeyDepartmentId()) + "'" +
                "WHERE id = " + user.get(this.getKeyUserId()));

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
    public void deleteUser(int id) {
        this.dbUpdate("DELETE FROM User WHERE id = " + id);
    }

    public void createDepartment(String name){
        this.dbUpdate("INSERT INTO Department (name) VALUES ('"+name+"')");
    }

    @Override
    public void updateDepartment(Map<String,Object> dep){
        this.dbUpdate("UPDATE Department SET name = '" +
                        dep.get(this.getKeyDepartmentName()) +
                        "' WHERE id = id = '" + dep.get(this.getKeyDepartmentId()) +
                        "'"
        );
    }

    @Override
    public void removeDepartment(int id){
        this.dbUpdate("DELETE FROM Department WHERE id = id = '" + id + "'");
    }

    @Override
    public List<Map<String,Object>> getAllDepartments(){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

        Connection con = this.getConnection();
        Statement stmt = null;

        try{

            stmt = con.createStatement();
            String sql = "SELECT * FROM Department;";

            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                Map<String,Object> dep = new HashMap<String, Object>();
                dep.put(this.getKeyDepartmentId(),rs.getInt(this.getKeyDepartmentId()));
                dep.put(this.getKeyDepartmentName(),rs.getString(this.getKeyDepartmentName()));
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
	public void createUser(Map<String,Object> user) {
	    String sql = "INSERT INTO User (first_name,last_name,city,street,street_nr,zip_code,birthday,department_id)" +
                "VALUES (	'" + user.get(this.getKeyUserFirstname()) + "', " +
                "'" + user.get(this.getKeyUserLastname()) + "', " +
                "'" + user.get(this.getKeyUserCity()) + "', " +
                "'" + user.get(this.getKeyUserStreet()) + "', " +
                "'" + user.get(this.getKeyUserStreetNr()) + "', " +
                "'" + user.get(this.getKeyUserZipCode()) + "', " +
                "'" + user.get(this.getKeyUserBirthday()) + "'," +
                "'" + ((Map)user.get(this.getKeyUserDepartment())).get(this.getKeyDepartmentId())+"');";

	    this.dbUpdate(sql);
	}
	
	public boolean existCustomer(int id){
        //@todo by for SQL
		boolean exist = false;
		Connection con = this.getConnection();
		Statement stmt = null;
		
		try{
			stmt = con.createStatement();
			
			String sql = "SELECT * " +
                    "FROM User " +
                    "WHERE id = " + id;

            System.out.println(sql);

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
	public List<Map<String ,Object>> getAllUser() {
		List<Map<String ,Object>> list = new ArrayList<Map<String ,Object>>();
        String sql = "";
		Connection con = this.getConnection();
		Statement stmt = null;
		
		try{
			
			stmt = con.createStatement();
			sql = "SELECT " +
                    "User." + this.getKeyUserId() + ", " +
                    "User." + this.getKeyUserFirstname() + ", " +
                    "User." + this.getKeyUserLastname() + ", " +
                    "User." + this.getKeyUserCity() + ", " +
                    "User." + this.getKeyUserStreet() + ", " +
                    "User." + this.getKeyUserStreetNr() + ", " +
                    "User." + this.getKeyUserZipCode() + ", " +
                    "User." + this.getKeyUserBirthday() + ", " +
                    "Department." + this.getKeyDepartmentId() + " as department_id,"  +
                    "Department." + this.getKeyDepartmentName() + " as `" + this.getKeyDepartmentName() + "` " +
                    "FROM User " +
                    "LEFT JOIN Department ON User.department_id" +
                    " = Department." + this.getKeyDepartmentId() + ";";
            System.out.println(sql);
            ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				list.add(this.userResultSetToUserMap(rs));
			}   
			rs.close();
			                                       
		} catch(Exception e) {                  
			System.err.println("Fehler beim Laden aller Benutzer: " + e.getMessage() + sql);
		} finally{
			this.closeConnection(con, stmt);
		}
		return list;
	}

    private Map<String, Object> userResultSetToUserMap(ResultSet rs) throws SQLException {
        Map<String, Object> user = new HashMap<String, Object>();
        Map<String, Object> depMap = new HashMap<String, Object>();

        depMap.put(this.getKeyDepartmentId(),rs.getInt("department_id"));
        depMap.put(this.getKeyDepartmentName(),rs.getString(this.getKeyDepartmentName()));

        user.put(this.getKeyUserId(),rs.getInt(this.getKeyUserId()));
        user.put(this.getKeyUserFirstname(),rs.getString(this.getKeyUserFirstname()));
        user.put(this.getKeyUserLastname(),rs.getString(this.getKeyUserLastname()));
        user.put(this.getKeyUserBirthday(), Date.from(
                LocalDate.parse(
                        rs.getString(this.getKeyUserBirthday())).atStartOfDay().atZone(
                        ZoneId.systemDefault()
                ).toInstant()));
        user.put(this.getKeyUserCity(),rs.getString(this.getKeyUserCity()));
        user.put(this.getKeyUserStreet(),rs.getString(this.getKeyUserStreet()));
        user.put(this.getKeyUserStreetNr(),rs.getString(this.getKeyUserStreetNr()));
        user.put(this.getKeyUserZipCode(),rs.getString(this.getKeyUserZipCode()));
        user.put(this.getKeyUserDepartment(),depMap);

        return user;
    }
    @Override
    public Map<String, Object> getUserById(int id) {

        String sql;
        Connection con = this.getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            sql = "SELECT " +
                    "User." + this.getKeyUserId() + ", " +
                    "User." + this.getKeyUserFirstname() + ", " +
                    "User." + this.getKeyUserLastname() + ", " +
                    "User." + this.getKeyUserCity() + ", " +
                    "User." + this.getKeyUserStreet() + ", " +
                    "User." + this.getKeyUserStreetNr() + ", " +
                    "User." + this.getKeyUserZipCode() + ", " +
                    "User." + this.getKeyUserBirthday() + ", " +
                    "Department." + this.getKeyDepartmentId() + " as department_id, " +
                    "Department." + this.getKeyDepartmentName() + " as " + this.getKeyDepartmentName() + " " +
                    "FROM User " +
                    "LEFT JOIN Department ON User.department_id" +
                    " = Department." + this.getKeyDepartmentId() + " " +
                    "WHERE User." + this.getKeyUserId() + " = '" + id + "';";

            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                return this.userResultSetToUserMap(rs);
            }
        }catch (Exception e){
            System.err.println(e.getMessage());
        }

        return null;
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
