package de.oszimt.persistence.impl;

import de.oszimt.persistence.iface.IPersistance;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

/**
 * Datenhaltung for SQLite
 * getestet für SQLite version: 3.0.0+
 *
 * Implementierung des IPersistance Interfaces für die
 * relationale Datenbank SQLite
 */
public class SQLitePersistance implements IPersistance {

    /**
     * Da SQLitePersistance als Singleton fungiert haben
     * wir eine statische Variable um immer ein Zeiger auf
     * die Refferenze zu haben
     */
	private static SQLitePersistance instance;

    /**
     * Pfad der SQLite Datenbank Datei
     */
	private static final String DATABASEPATH = "SQLiteDatabase.db";

    /**
     * Der Konstruktor, hier werden evtl. noch fehlende
     * Tabelle erstellt
     */
	private SQLitePersistance() {
		this.createTables();
	}

    /**
     * Um die Klasse auch als Singleton nutzen zu können haben wir
     * die statische Methode getInstance(). Sie erzeugt oder gibt eine
     * bestehende Refference der Klasse zurück
     *
     * @return SQLitePersistance
     */
	public static SQLitePersistance getInstance(){
		if(instance == null) {
            instance = new SQLitePersistance();
        }
		return instance;
	}

    /**
     * Baut eine Verbung zur Datenbank auf
     * @return Verbindungs Objekt
     */
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

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Nummer zurück
     * @return String
     */
    @Override
    public String getKeyUserId() {
        return "id";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzervornamens zurück
     * @return String
     */
    @Override
    public String getKeyUserFirstname() {
        return "first_name";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzernachnamens zurück
     * @return String
     */
    @Override
    public String getKeyUserLastname() {
        return "last_name";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Stadt zurück
     * @return String
     */
    @Override
    public String getKeyUserCity() {
        return "city";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Strasse zurück
     * @return String
     */
    @Override
    public String getKeyUserStreet() {
        return "street";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Strassen Nummer zurück
     * @return String
     */
    @Override
    public String getKeyUserStreetNr() {
        return "street_nr";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer PLZ zurück
     * @return String
     */
    @Override
    public String getKeyUserZipCode() {
        return "zip_code";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Benutzergeburtstags
     * @return String
     */
    @Override
    public String getKeyUserBirthday() {
        return "birthday";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Benutzer Abteilung zurück
     * @return String
     */
    @Override
    public String getKeyDepartmentId() {
        return "id";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Abteilungsnummer zurück
     * @return String
     */
    @Override
    public String getKeyDepartmentName() {
        return "name";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen der Abteilungsnummer zurück
     * @return String
     */
    @Override
    public String getKeyDepartmentAmount() {
        return "amount";
    }

    /**
     * Für die Rückgabe der Datenbank-Methoden werden allgemeine Datentypen
     * wie z.B. Map verwendet. Da somit nicht immer direkt klar ist wie ein Key
     * benannt ist, kann er hier abgefragt werden.
     *
     * Gibt den Key-Namen des Abteilungsnames zurück
     * @return String
     */
    @Override
    public String getKeyUserDepartment() {
        return "department";
    }

    /**
     * Erzeugt die benötigten Tabellen in der SQLite Datenbank, insofern
     * diese nicht bereits erzeugt wurden
     */
	private void createTables(){
        //definieren des CREATE Statemnts unter berücksichtigung der Key namen
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
        //definieren des CREATE Statemnts unter berücksichtigung der Key namen
        String Department_sql = "CREATE TABLE IF NOT EXISTS Department " +
                            "(" +   this.getKeyDepartmentId() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    this.getKeyDepartmentName() + " TEXT NOT NULL)";
        //erstellen der Tabellen (denn nicht bereiots vorhanden)
        this.dbUpdate(User_sql);
        this.dbUpdate(Department_sql);
	}

    /**
     * Erstellt einen neuen Benutzer oder ändert einen bestehenden
     * @param user Map mit Daten des Benutzers
     */
    @Override
	public void upsertUser(Map<String, Object> user) {
		if(existCustomer((int)user.get(this.getKeyUserId())))
			updateUser(user);
		else
			createUser(user);
	}

    /**
     * Kombination aus updateUser/insert User. Wenn ein Benutzer, welcher
     * sich aus den Werten der Map ergibt, nicht existstiert dann wird er
     * neu erzeugt. Andererseits werden alle vorhandenen werde aus der Map in
     * das Dokument übernommen
     * @param user Map mit Daten des Benutzers
     */
    private void updateUser(Map<String, Object> user) {
        //ausführen des Update Statements, unter berücksichtiung der key namen
        this.dbUpdate("UPDATE User set " +
                "first_name 	= '" + user.get(this.getKeyUserFirstname()) + "', " +
                "last_name		= '" + user.get(this.getKeyUserLastname()) + "', " +
                "city 			= '" + user.get(this.getKeyUserCity()) + "', " +
                "street 		= '" + user.get(this.getKeyUserStreet()) + "', " +
                "street_nr  	= '" + user.get(this.getKeyUserStreetNr()) + "', " +
                "zip_code 		= " + user.get(this.getKeyUserZipCode()) + ", " +
                "birthday		= '" + user.get(this.getKeyUserBirthday()) + "'," +
                " department_id = '" + ((Map)user.get(this.getKeyUserDepartment())).get(this.getKeyDepartmentId()) + "' " +
                "WHERE id = " + user.get(this.getKeyUserId()));
	}

    /**
     * Alle Datenbank Aktionen die keine Rückgabe haben (CREATE, INSERT, UPDATE...)
     * werden hier ausgeführt
     * @param sql Datenbank Abfrage
     */
    private void dbUpdate(String sql){
        //Verbindungsaufbau
        Connection con = this.getConnection();
        //Statement Variable initialisieren
        Statement stmt = null;
        try{
            //neues Statement erzeugen
            stmt = con.createStatement();
            //sql ausführen
            stmt.executeUpdate(sql);
            //Verbindung commiten (Änderungen auf die Datenbank übertragen)
            con.commit();
        //Fehlerhandhabung
        } catch(Exception e) {
            //System.err.println(sql);
            System.err.println("Fehler beim Kundenupdate.");
        //schließen der Verbindung egal ob Exception oder nicht
        } finally{
            this.closeConnection(con, stmt);
           // System.out.println(sql + " erfolgreich");
        }
    }

    /**
     * Benutzer Dokument aus der Collection entfernen
     * @param id Id des Benutzers
     */
    @Override
    public void deleteUser(int id) {
        this.dbUpdate("DELETE FROM User WHERE id = " + id);
    }

    /**
     * Neue Abteilung erzeugen
     * @param name Name der neuen Abteilung
     */
    @Override
    public void createDepartment(String name){
        this.dbUpdate("INSERT INTO Department (name) VALUES ('"+name+"')");
    }

    /**
     * Aktualisieren einer Abteiung
     * @param dep Map mit Daten der Abteilung
     */
    @Override
    public void upsertDepartment(Map<String, Object> dep){
        if(existDepartment((int) dep.get(this.getKeyDepartmentId())))
            updateDepartment(dep);
        else
            createDepartment((String) dep.get(this.getKeyDepartmentName()));
    }

    /**
     * Ändert eine Abteilung
     * @param dep Map mit Daten der Abteilung
     */
    private void updateDepartment(Map<String, Object> dep){
        this.dbUpdate("UPDATE Department SET name = '" +
                        dep.get(this.getKeyDepartmentName()) +
                        "' WHERE id = '" + dep.get(this.getKeyDepartmentId()) +
                        "'"
        );
    }

    /**
     * Entfernen einer Abteilung
     * @param id Id der zulöschenden Abteilung
     */
    @Override
    public void deleteDepartment(int id){
        this.dbUpdate("DELETE FROM Department WHERE id = '" + id + "'");
    }

    /**
     * Gibt eine Liste aller Abteilungen zurück
     * @return Liste von Maps mit Daten aller Abteilungen
     */
    @Override
    public List<Map<String,Object>> getAllDepartments(){
        //leere Liste initialisieren
        List<Map<String,Object>> list = new ArrayList<>();
        //Verbindungsaufbau
        Connection con = this.getConnection();
        //Statement Variable initialisieren
        Statement stmt = null;
        try{
            //neues Statement erzeugen
            stmt = con.createStatement();
            //SQL Abfrage auf alle Abtleiungen setzten
            String sql = "select Department.id,Department.name,sub.amount " +
                         "from Department " +
                         "left join ( " +
                            "select department_id, count(1) as amount " +
                            "from User " +
                            "group by department_id " +
                         ") sub " +
                        "on Department.id = sub.department_id;";
            //SQL ausführen und Ergebniss Reffernce entgegen nehmen
            ResultSet rs = stmt.executeQuery(sql);
            //Ergebniss durchlaufen, Zeiger Iteration
            while(rs.next()){
                //Zwischenergebniss der Liste hinzufügen
                list.add(departmentResultToDepartmentMap(rs));
            }
            //Ergebinss schließen
            rs.close();
        //Fehlerhandhabung
        } catch(Exception e) {
            System.err.println("Fehler beim Laden aller Abteilungen");
        //Schließen der Verbindung (Auch im Fehlerfall)
        } finally{
            this.closeConnection(con, stmt);
        }
        return list;
    }

    /**
     * Wandet ein ResultSet in eine Map mit Daten einer Abteilung um
     * @param rs ResultSet
     * @return Map mit Daten einer Abteilung
     * @throws SQLException
     */
    private Map<String, Object> departmentResultToDepartmentMap(ResultSet rs) throws SQLException {
        //Neue Map für Zwischenergenis initialisieren
        Map<String,Object> dep = new HashMap<String, Object>();
        //Abteilungs Id der Map hinzufügen
        dep.put(this.getKeyDepartmentId(),rs.getInt(this.getKeyDepartmentId()));
        //AbteilungS Name der Map hinzufügen
        dep.put(this.getKeyDepartmentName(),rs.getString(this.getKeyDepartmentName()));
        //Anzahl der Mitarbeiter in einer Abteilung hinzufügen
        dep.put(this.getKeyDepartmentAmount(),rs.getInt(this.getKeyDepartmentAmount()));
        return  dep;
    }

    /**
     * Erstellt einen neuen Benutzer
     * @param user Map mit Daten eines Benutzers
     */
	@Override
	public void createUser(Map<String,Object> user) {
        //Ausführen des Einfügebefehls auf die Datenbank (unter Berücksichtigung der Key Namen)
	    this.dbUpdate("INSERT INTO User (first_name,last_name,city,street,street_nr,zip_code,birthday,department_id)" +
                "VALUES (	'" + user.get(this.getKeyUserFirstname()) + "', " +
                "'" + user.get(this.getKeyUserLastname()) + "', " +
                "'" + user.get(this.getKeyUserCity()) + "', " +
                "'" + user.get(this.getKeyUserStreet()) + "', " +
                "'" + user.get(this.getKeyUserStreetNr()) + "', " +
                "'" + user.get(this.getKeyUserZipCode()) + "', " +
                "'" + user.get(this.getKeyUserBirthday()) + "'," +
                "'" + ((Map)user.get(this.getKeyUserDepartment())).get(this.getKeyDepartmentId())+"');");
	}

    /**
     * Prüfen ob ein Benutzer existiert (anhand der ID)
     * @param id Id des Benutzers
     * @return ture=existiert, false=existiert noch nicht
     */
	public boolean existCustomer(int id){
        //Verbindungsaufbau
		Connection con = this.getConnection();
        //Initialisieren einer Statement Variable
		Statement stmt = null;
		try{
            //Erzeugen eines neuen Statements
			stmt = con.createStatement();
			//SQL für die Abfrage definieren
			String sql = "SELECT * " +
                    "FROM User " +
                    "WHERE id = " + id;
            //Abfrage ausführen und eine Refferenze auf das Ergebniss erhalten
            ResultSet rs = stmt.executeQuery(sql);
            //Gibt es ein Ergebniss?
			if(rs.next()){
              return true;
            }
            //Ergbinis schließen
			rs.close();

        //Fehlerhandhabung
		} catch(Exception e) {
			System.err.println("Fehler beim ermitteln ob Benutzer vorhanden ist");
			System.out.println(e.getMessage());
        //Verbindung schießen (auch im Fehlerfall)
		} finally {
			this.closeConnection(con, stmt);
		}
		return false;
	}

    /**
     * Prüfen ob ein Department existiert (anhand der ID)
     * @param id Id der Abteilung
     * @return ture=existiert, false=existiert noch nicht
     */
    public boolean existDepartment(int id){
        //Verbindungsaufbau
        Connection con = this.getConnection();
        //Initialisieren einer Statement Variable
        Statement stmt = null;
        try{
            //Erzeugen eines neuen Statements
            stmt = con.createStatement();
            //SQL für die Abfrage definieren
            String sql = "SELECT * " +
                    "FROM Department " +
                    "WHERE id = " + id;
            //Abfrage ausführen und eine Refferenze auf das Ergebniss erhalten
            ResultSet rs = stmt.executeQuery(sql);
            //Gibt es ein Ergebniss?
            if(rs.next()){
                return true;
            }
            //Ergbinis schließen
            rs.close();
            //Fehlerhandhabung
        } catch(Exception e) {
            System.err.println("Fehler beim ermitteln ob Department vorhanden ist");
            System.out.println(e.getMessage());
            //Verbindung schießen (auch im Fehlerfall)
        } finally {
            this.closeConnection(con, stmt);
        }
        return false;
    }

    /**
     * Gibt eine Liste alle Benutzer (Maps) zurück
     * @return Liste von Maps mit Daten aller Benutzer
     */
	@Override
	public List<Map<String ,Object>> getAllUser() {
        //Erzeugen einer neuen/leeren Liste
		List<Map<String ,Object>> list = new ArrayList<Map<String ,Object>>();
        //sql string leer initialisieren
        String sql = "";
        //Verbindungaufbau
		Connection con = this.getConnection();
        //Initialisieren ein Statement Variable
		Statement stmt = null;
		try{
			//neues Statement erzeugen
			stmt = con.createStatement();
            //Abfrage, unter Berücksichtigung der key Namen, definieren
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
            //Abfrage ausführen und Reffernze auf Ergebiss entgegennehmen
            ResultSet rs = stmt.executeQuery(sql);
			//Ergebnis durchlaufen (Zeiger Iteration)
			while(rs.next()){
                //Teilergebniss zu einer Map konvertieren und der Liste hinzufügen
				list.add(this.userResultSetToUserMap(rs));
			}
            //Ergebniss schließen
			rs.close();
        //Fehlerhandhabung
		} catch(Exception e) {                  
			System.err.println("Fehler beim Laden aller Benutzer: " + e.getMessage() + sql);
        //Verbindung schließen (auch im Fehlerfall)
		} finally{
			this.closeConnection(con, stmt);
		}
        //Liste zurückgeben
		return list;
	}

    /**
     * Gibt eine Liste von Benutzer Maps anhand Ihrer Abteilung
     * @param id Id der Abteilung
     * @return List von Maps mit Daten aller Benutzer die in der gegebenden Abteilung sind
     */
    @Override
    public List<Map<String, Object>> getUsersByDepartmentId(int id) {
        String sql;
        //Verbindungsaufbau
        Connection con = this.getConnection();
        //Statement Variable initialisieren
        Statement stmt = null;
        List<Map<String, Object>> users = new ArrayList<>();
        try {
            //neues Statement erzeugeb
            stmt = con.createStatement();
            //Abfrage, unter Berüchksichtiung der gegebenen Keynamen, definieren
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
                    "WHERE User.department_id = '" + id + "';";
            //Abfrage ausführen, Reffereze auf Ergebnis entgegennehmen
            ResultSet rs = stmt.executeQuery(sql);
            //Gibt es ein Ergebnis?
            while(rs.next()){
                //Ergebnis in eine Map umwandeln und zturückgeben
                users.add(this.userResultSetToUserMap(rs));
            }
            //Fehlerbehandlung
        }catch (Exception e){
            System.err.println(e.getMessage());
            //Verbindung schließen (auch im Fehlerfall)
        } finally{
            this.closeConnection(con, stmt);
        }
        return users.size() == 0 ? null : users;
    }

    /**
     * Gibt eine Abteilung anhand Ihrer Id
     * @param id Id der Abteiling
     * @return Map mit Daten der Abteilung
     */
    @Override
    public Map<String, Object> getDepartmentById(int id) {
        String sql;
        //Verbindungsaufbau
        Connection con = this.getConnection();
        //Statement Variable initialisieren
        Statement stmt = null;
        try {
            //neues Statement erzeugeb
            stmt = con.createStatement();
            //Abfrage, unter Berüchksichtiung der gegebenen Keynamen, definieren
            sql = "select Department.id,Department.name,sub.amount " +
                    "from Department " +
                    "left join ( " +
                    "select department_id, count(1) as amount " +
                    "from User " +
                    "group by department_id " +
                    ") sub " +
                    "on Department.id = sub.department_id " +
                    "WHERE Department." + this.getKeyDepartmentId() + " = '" + id + "';";
            //Abfrage ausführen, Reffereze auf Ergebnis entgegennehmen
            ResultSet rs = stmt.executeQuery(sql);
            //Gibt es ein Ergebnis?
            if(rs.next()){
                //Ergebnis in eine Map umwandeln und zturückgeben
                return this.departmentResultToDepartmentMap(rs);
            }
            //Fehlerbehandlung
        }catch (Exception e){
            System.err.println(e.getMessage());
            //Verbindung schließen (auch im Fehlerfall)
        } finally{
            this.closeConnection(con, stmt);
        }
        return null;
    }

    /**
     * Wandelt ein ResultSet in eine Benutzer Map um
     * @param rs ResultSet
     * @return Map mit Daten des Benutzers
     * @throws SQLException
     */
    private Map<String, Object> userResultSetToUserMap(ResultSet rs) throws SQLException {
        //Leere Map für den Benutzer
        Map<String, Object> user = new HashMap<String, Object>();
        //Leere List für die Abteilung
        Map<String, Object> depMap = new HashMap<String, Object>();

        //hinzufügen der Abteilungs id (mit gegeben Keynamen)
        depMap.put(this.getKeyDepartmentId(),rs.getInt("department_id"));
        //hinzufügen des Abteilungsnamen (mit gegeben Keynamen)
        depMap.put(this.getKeyDepartmentName(),rs.getString(this.getKeyDepartmentName()));

        //hinzufügen der Benutzer id (mit gegeben Keynamen)
        user.put(this.getKeyUserId(),rs.getInt(this.getKeyUserId()));
        //hinzufügen des Benutzervornamen (mit gegeben Keynamen)
        user.put(this.getKeyUserFirstname(),rs.getString(this.getKeyUserFirstname()));
        //hinzufügen des Benutzernachnamen (mit gegeben Keynamen)
        user.put(this.getKeyUserLastname(),rs.getString(this.getKeyUserLastname()));
        //hinzufügen des Benutzer Geburtstags (mit gegeben Keynamen)
        user.put(this.getKeyUserBirthday(), Date.from(
                LocalDate.parse(
                        rs.getString(this.getKeyUserBirthday())).atStartOfDay().atZone(
                        ZoneId.systemDefault()
                ).toInstant()));
        //hinzufügen der Benutzer Stadt (mit gegeben Keynamen)
        user.put(this.getKeyUserCity(),rs.getString(this.getKeyUserCity()));
        //hinzufügen der Benutzer Strasse (mit gegeben Keynamen)
        user.put(this.getKeyUserStreet(),rs.getString(this.getKeyUserStreet()));
        //hinzufügen der Benutzer Hausnummer (mit gegeben Keynamen)
        user.put(this.getKeyUserStreetNr(),rs.getString(this.getKeyUserStreetNr()));
        //hinzufügen der Benutzer PLZ (mit gegeben Keynamen)
        user.put(this.getKeyUserZipCode(),rs.getInt(this.getKeyUserZipCode()));
        //hinzufügen der Benutzer Abteilung (mit gegeben Keynamen)
        user.put(this.getKeyUserDepartment(),depMap);
        //Benutzer zurückgeben
        return user;
    }

    /**
     * Laden eines einzelnen Benutzers anhand seiner Id
     * @param id Id des Benutzers
     * @return Map mit Daten des Benutzers
     */
    @Override
    public Map<String, Object> getUserById(int id) {
        String sql;
        //Verbindungsaufbau
        Connection con = this.getConnection();
        //Statement Variable initialisieren
        Statement stmt = null;
        try {
            //neues Statement erzeugeb
            stmt = con.createStatement();
            //Abfrage, unter Berüchksichtiung der gegebenen Keynamen, definieren
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
            //Abfrage ausführen, Reffereze auf Ergebnis entgegennehmen
            ResultSet rs = stmt.executeQuery(sql);
            //Gibt es ein Ergebnis?
            if(rs.next()){
                //Ergebnis in eine Map umwandeln und zturückgeben
                return this.userResultSetToUserMap(rs);
            }
        //Fehlerbehandlung
        }catch (Exception e){
            System.err.println(e.getMessage());
        //Verbindung schließen (auch im Fehlerfall)
        } finally{
            this.closeConnection(con, stmt);
        }
        return null;
    }

    /**
     * Schließen der Verbindung zur Datenbank
     * @param con Verbindung
     * @param stmt Datenbank Statement
     */
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
