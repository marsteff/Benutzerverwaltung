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
			System.err.println("bei Erstellung der Connection ist was schief gelaufen");
		}
		
		return connection;
	}
	
	public void createInitTable(){
		this.createTable();
	}
	
	private void createTable(){
		 Connection con = this.getConnection();
       Statement stmt = null;
       try {

         stmt = con.createStatement();
         String sql = "CREATE TABLE IF NOT EXISTS CUSTOMERS " +
                      "(ID 				INTEGER PRIMARY KEY     AUTOINCREMENT," +
                      " VORNAME         TEXT    			NOT NULL, " + 
                      " NACHNAME        TEXT    			NOT NULL, " + 
                      " ORT        		TEXT				NOT NULL, " + 
                      " STRASSE    		TEXT				NOT NULL, " +
                      " STRASSENNUMMER	TEXT				NOT NULL, " +
                      " PLZ        		INTEGER				NOT NULL, " +
                      " GEBURTSTAG      TEXT				NOT NULL)"; 
         stmt.executeUpdate(sql);
         
       } catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
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

		try{
			stmt = con.createStatement();

			String sql = "UPDATE CUSTOMERS set " +
												"VORNAME 		= '" + user.getFirstname() + "', " +
												"NACHNAME 		= '" + user.getLastname() + "', " +
												"ORT 			= '" + user.getCity() + "', " +
												"STRASSE 		= '" + user.getStreet() + "', " +
												"STRASSENNUMMER	= '" + user.getStreetnr() + "', " +
												"PLZ 			= " + user.getZipcode() + ", " +
												"GEBURTSTAG		= '" + user.getBirthday() + "' " +
												"WHERE ID=" + user.getId();
			stmt.executeUpdate(sql);
			con.commit();

		} catch(Exception e) {
			System.err.println("Problem beim Aktualiseren eines Kunden aufgetreten");
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

            String sql = "DELETE FROM CUSTOMERS WHERE ID=" + user.getId();

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
		
		try{
			stmt = con.createStatement();
			
			String sql = "INSERT INTO CUSTOMERS (VORNAME,NACHNAME,ORT,STRASSE,STRASSENNUMMER,PLZ,GEBURTSTAG)" +
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
			System.err.println("Fehler beim erstellen des Kunden" );
			System.err.println(e.getMessage());
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
			
			String sql = "SELECT * FROM CUSTOMERS WHERE ID=" + user.getId();
			
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next())
				exist = true;
			
			rs.close();
			
		} catch(Exception e) {
			System.err.println("Fehler beim ermitteln ob User vorhanden ist");
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
			String sql = "SELECT * FROM CUSTOMERS;";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				User k = new User(
                        rs.getString("VORNAME"),
                        rs.getString("NACHNAME"),
                        LocalDate.parse(rs.getString("GEBURTSTAG")),
                        rs.getString("ORT"),
                        rs.getString("STRASSE"),
                        rs.getString("STRASSENNUMMER"),
                        rs.getInt("PLZ")
                );
				k.setId(rs.getInt("ID"));
				list.add(k);
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
