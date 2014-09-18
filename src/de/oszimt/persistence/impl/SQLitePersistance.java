package de.oszimt.persistence.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.oszimt.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.oszimt.persistence.iface.IPersistance;

public class SQLitePersistance implements IPersistance {

	private static SQLitePersistance obj;
	private static final String DATABASEPATH = "customer.db";
	
	private SQLitePersistance() {
		this.createTable();
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
	
	public void createInitTable(){
		this.createTable();
	}
	
	private void createTable(){
       Connection con = this.getConnection();
       Statement stmt = null;
       String sql = "";
       try {

         stmt = con.createStatement();
         sql = "CREATE TABLE IF NOT EXISTS Usertable " +
                      "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                      " first_name TEXT NOT NULL, " +
                      " last_name TEXT NOT NULL, " +
                      " city TEXT NOT NULL, " +
                      " street TEXT NOT NULL, " +
                      " street_nr TEXT NOT NULL, " +
                      " zip_code INTEGER NOT NULL, " +
                      " birthday TEXT NOT NULL)";
         stmt.executeUpdate(sql);
         con.commit();
       } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.out.println(sql);
         System.exit(0);
       } finally {
    	   this.closeConnection(con, stmt);
       }
	}
	
	@Override
	public void upsertUser(User user) {
		if(existCustomer(user))
			update(user);
		else
			createUser(user);

	}

	private void update(User user) {
		Connection con = this.getConnection();
		Statement stmt = null;
        String sql = "";
		try{
			stmt = con.createStatement();

			sql = "UPDATE Usertable set " +
                        "first_name 		= '" + user.getFirstname() + "', " +
                        "last_name		= '" + user.getLastname() + "', " +
                        "city 			= '" + user.getCity() + "', " +
                        "street 		= '" + user.getStreet() + "', " +
                        "street_nr  	= '" + user.getStreetnr() + "', " +
                        "zip_code 		= " + user.getZipcode() + ", " +
                        "birthday		= '" + user.getBirthday() + "' " + //@todo check format
                        "WHERE id = " + user.getId();
        stmt.executeUpdate(sql);
			con.commit();

		} catch(Exception e) {
			System.err.println("Problem beim Aktualiseren eines Benutzers aufgetreten");
			System.out.println(e.getMessage());
		} finally{
			this.closeConnection(con, stmt);
			System.out.println(user + " erfolgreich bearbeitet");
		}
	}

    @Override
    public void deleteUser(User user) {
        Connection con = this.getConnection();
        Statement stmt = null;

        try {
            stmt = con.createStatement();

            String sql = "DELETE FROM Usertable WHERE id = " + user.getId();

            stmt.executeUpdate(sql);

            con.commit();

        } catch (Exception e) {

        } finally {
            this.closeConnection(con, stmt);
        }
    }

	@Override
	public void createUser(User user) {
		Connection con = this.getConnection();
		Statement stmt = null;
        String sql = "";

		try{
			stmt = con.createStatement();
			
			sql = "INSERT INTO Usertable (first_name,last_name,city,street,street_nr,zip_code,birthday)" +
						"VALUES (	'" + user.getFirstname() + "', " +
									"'" + user.getLastname() + "', " +
									"'" + user.getCity() + "', " +
									"'" + user.getStreet() + "', " +
									"'" + user.getStreetnr() + "', " +
									"'" + user.getZipcode() + "', " +
									"'" + user.getBirthday() + "');";
			stmt.executeUpdate(sql);
			con.commit();
			
		} catch(Exception e) {
			System.err.println("Fehler beim erstellen des Benutzers" );
			System.err.println(e.getMessage());
			System.err.println(sql);
		} finally {
			this.closeConnection(con, stmt);
		}

	}
	
	public boolean existCustomer(User user){
		boolean exist = false;
		Connection con = this.getConnection();
		Statement stmt = null;
		
		try{
			stmt = con.createStatement();
			
			String sql = "SELECT * FROM Usertable WHERE id = " + user.getId();
			
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

		Connection con = obj.getConnection();
		Statement stmt = null;
		
		try{
			
			stmt = con.createStatement();
			String sql = "SELECT * FROM Usertable;";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				User u = new User(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        LocalDate.parse(rs.getString("birthday")),
                        rs.getString("city"),
                        rs.getString("street"),
                        rs.getString("street_nr"),
                        rs.getInt("zip_code")
                );
				u.setId(rs.getInt("id"));
				list.add(u);
			}   
			rs.close();
			                                       
		} catch(Exception e) {                  
			System.err.println("Fehler beim Laden aller Benutzer");
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
