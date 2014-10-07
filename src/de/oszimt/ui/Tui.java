package de.oszimt.ui;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.ui.iface.UserInterface;
import org.fusesource.jansi.AnsiConsole;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Created by user on 17.09.2014.
 */
public class Tui implements UserInterface{

    private final Color STANDARD_COLOR = WHITE;
    private IConcept concept;

    private String[] entrys = { "Vorname",
                                "Nachname",
                                "Geburtstag",
                                "Stadt",
                                "Postleitzahl",
                                "Strasse",
                                "Strassennr.",
                                "Abteilung"};

    public Tui(IConcept concept){
        this.concept = concept;

        AnsiConsole.systemInstall();

        showMainMenu();
    }

    /**
     * Schreibt das Hauptmenue in die Konsole
     */
    private void showMainMenu() {
        clean();
        String[] entrys = { "Benutzer anlegen",
                            "Benutzer anzeigen",
                            "Benutzer bearbeiten",
                            "Benutzer loeschen",
                            "Benutzer suchen",
                            "Alle Benutzer Anzeigen",
                            "Abbrechen"};
        writeHeader(3);

        //Aufbauen des Menue´s
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            printWhitespace(entrys, i, 2);
            print("(");
            print(BLUE,i+1+"");
            println(")");
        }
        println("");
        print("Menuepunkt eingeben: ");

        //einlesen des Input´s
        int input = readInt();

        //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
        if(input == -1 || input > entrys.length || input < 1) {
            printWrongEntryErrorMessage(6);
            //System kurz einschlafen lassen, damit Fehlermeldung lesbar ist
            sleep(1500);
            showMainMenu();
            return;
        }

        //Prüfung, welches Menue aufgerufen werden soll
        switch (input){
            case 1: createUser();
                    break;
            case 2: showUser();
                    break;
            case 3: editUser();
                    break;
            case 4: deleteUser();
                    break;
            case 5: searchUser(createDummyUser());
                    break;
            case 6: showAllUsers();
                    break;
            case 7: System.exit(0);

        }
    }

    private void createUser() {
        //TODO - at first i make it a lot ugly .. then i think to make it more comfortable
        String firstname = toShortUgly(0);
        String lastname = toShortUgly(1);
        LocalDate date = null;
        do {
            println("Geburtstag eingeben ");
            int day = determineBirthday("Tag", 1, 31);
            int month = determineBirthday("Monat", 1, 12);
            int year = determineBirthday("Jahr", 1900, LocalDate.now().getYear());
            try {
                date = LocalDate.of(year, month, day);
            } catch (DateTimeException e) {
            }
            if (date != null && date.isBefore(LocalDate.now())) {
                break;
            }
            println(RED, "Das Datum ist nicht gültig");
        } while (true);
        String city = toShortUgly(3);
        String zipCode = toShortUgly(4);
        String street = toShortUgly(5);
        String streetNr = toShortUgly(6);
        //DEPARTMENT PART
        print(entrys[7]);
        printWhitespace(entrys, 7, 2);
        println(": ");
        List<Department> departmentList = concept.getAllDepartments();
        String[] departmentArray = new String[departmentList.size()];

        for (int i = 0; i < departmentArray.length; i++) {
            departmentArray[i] = toAscii(departmentList.get(i).getName());
        }
        for (int i = 0; i < departmentArray.length; i++) {
            print(departmentArray[i]);
            printWhitespace(departmentArray, i, 2);
            println("(" + (i + 1) + ")");
        }
        int departmentValue = 0;
        do {
            print("Zahl für Department eingeben: ");
            departmentValue = readInt();
            if (departmentValue > 0 && departmentValue <= departmentArray.length) {
                break;
            }
            println(RED, "Eingabe nicht gueltig. Wert muss zwischen 1 und " + departmentArray.length + " liegen");
        } while (true);
        Department dep = new Department(departmentValue, departmentArray[departmentValue - 1]);
        User newUser = new User(firstname, lastname, date, city, street, streetNr, Integer.parseInt(zipCode), dep);
        concept.upsertUser(newUser);

    }

    private String toShortUgly(int i) {
        print(entrys[i]);
        printWhitespace(entrys, i, 2);
        print(": ");
        String value = readString();
//        println("");
        return value;
    }

    private User createDummyUser(){
        return new User("", "", null, "", "", "", 0, null);
    }

    private void showUser() {
        //hole den aktuellen StackTrace
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        //hole aus dem StackTrace die aufrufende Methode und übergebe ihn der Methode
        searchAndPrintUserStats(stack[1].getMethodName(), false);

        //Soll nach einem weiteren Benutzer gesucht werden ?
        if(checkInputForYesOrNo("Nach weiterem Benutzer suchen ? (j/n)")) {
            showUser();
            return;
        }
        showMainMenu();
    }

    private void editUser() {
        //hole den aktuellen StackTrace
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        //hole aus dem StackTrace die aufrufende Methode und übergebe ihn der Methode
        User user = searchAndPrintUserStats(stack[1].getMethodName(), true);

        buildEditUser(user, true, false);
    }

    private void buildEditUser(User user, boolean editMenu, boolean print) {
        if (print) {
            writeUserStats(user, editMenu);
        }

        int input = 0;
        do {
            println("");
            print("Welches Attribut soll bearbeitet werden ? (1-" + entrys.length + "): ");
            println("");

            //einlesen des Input´s
            input = readInt();

            //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben

            if (input > 0 && input <= entrys.length) {
                break;
            }
            println(RED, "Falsche Eingabe");
        } while (true);

        String value = null;
        //Prüfung, welches Menue aufgerufen werden soll
        switch (input) {

            //Vorname
            case 1:
                print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setFirstname(value);
                break;

            //Nachname
            case 2:
                print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setLastname(value);
                break;

            //Geburtstag
            case 3:
                LocalDate date = null;
                do {
                    println("Neuen Geburtstag eingeben ");
                    int day = determineBirthday("Tag", 1, 31);
                    int month = determineBirthday("Monat", 1, 12);
                    int year = determineBirthday("Jahr", 1900, LocalDate.now().getYear());
                    String birthdayString = year + "-" + month + "-" + day;
                    try {
                        date = LocalDate.of(year, month, day);
                    } catch (DateTimeException e) {
                    }
                    if (date != null && date.isBefore(LocalDate.now())) {
                        break;
                    }
                    println(RED, "Das Datum ist nicht gültig");
                } while (true);

                user.setBirthday(date);
                break;

            //Stadt
            case 4:
                print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setCity(value);
                break;

            //PLZ
            case 5:
                print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setPLZ(Integer.parseInt(value));
                break;

            //Strasse
            case 6:
                print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setStreet(value);
                break;

            //Stassen-Nummer
            case 7:
                print("Neuen Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setStreetnr(value);
                break;

            //Abteilung
            case 8:
                List<Department> departmentList = concept.getAllDepartments();
                String[] departmentArray = new String[departmentList.size()];

                for (int i = 0; i < departmentArray.length; i++) {
                    departmentArray[i] = toAscii(departmentList.get(i).getName());
                }

                int departmentValue = buildDepartmentView(departmentArray, input);
                user.setDepartment(departmentList.get(departmentValue - 1));
                break;

        }
        concept.upsertUser(user);

        //weitere Attribute bearbeiten ?
        if (checkInputForYesOrNo("Weitere Attribute bearbeiten ? (j/n)")) {
            println("");
            buildEditUser(user, editMenu, true);
            return;
        }

        //Soll nach ein weiterer Benutzer bearbeitet werden ?
        if (checkInputForYesOrNo("Weiteren Benutzer bearbeiten ? (j/n)")) {
            editUser();
            return;
        }
        showMainMenu();
    }

    private void deleteUser() {
        //hole den aktuellen StackTrace
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        //hole aus dem StackTrace die aufrufende Methode und übergebe ihn der Methode
        User user = searchAndPrintUserStats(stack[1].getMethodName(), false);

        //Soll der Benutzer wirklich gelöscht werden ?
        if(checkInputForYesOrNo("Benutzer wirklich löschen ? (j/n)")) {
            concept.deleteUser(user);
        }

        //Soll ein weiterer Benutzer gelöscht werden ?
        if(checkInputForYesOrNo("Weiteren Benutzer loeschen ? (j/n)")) {
            this.deleteUser();
            return;
        }
        showMainMenu();
    }

    private void searchUser(User user) {
        User searchUser = user;

        String[] params = userParameterToArray(user);
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            printWhitespace(entrys, i, 2);
            print(": " + params[i]);
            printWhitespace(params, i, 2);
            print("(" + (i + 1) + ")");

            println("");
        }

        //TODO weiter kapseln
        int input = 0;
        do {
            println("");
            print("Welches Attribut zur Suche hinzufuegen ? (1-" + entrys.length + "): ");
            println("");

            //einlesen des Input´s
            input = readInt();

            //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben

            if (input > 0 && input <= entrys.length) {
                break;
            }
            println(RED, "Falsche Eingabe");
        } while (true);

        String value;
        //Prüfung, welches Menue aufgerufen werden soll
        switch (input) {

            //Vorname
            case 1:
                print("Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setFirstname(value);
                break;

            //Nachname
            case 2:
                print("Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setLastname(value);
                break;

            //Geburtstag
            case 3:
                LocalDate date = null;
                do {
                    println("Geburtstag eingeben ");
                    int day = determineBirthday("Tag", 0, 31);
                    int month = determineBirthday("Monat", 0, 12);
                    int year = determineBirthday("Jahr", 1900, LocalDate.now().getYear());
                    String birthdayString = year + "-" + month + "-" + day;
                    try {
                        date = LocalDate.of(year, month, day);
                    } catch (DateTimeException e) {
                    }
                    if (date != null) {
                        break;
                    }
                    println(RED, "Das Datum ist nicht gueltig");
                } while (true);

                user.setBirthday(date);
                break;

            //Stadt
            case 4:
                print("Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setCity(value);
                break;

            //PLZ
            case 5:
                print("Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setPLZ(Integer.parseInt(value));
                break;

            //Strasse
            case 6:
                print("Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setStreet(value);
                break;

            //Stassen-Nummer
            case 7:
                print("Wert fuer " + entrys[input - 1] + " eingeben: ");
                value = readString();
                user.setStreetnr(value);
                break;

            //Abteilung
            case 8:
                List<Department> departmentList = concept.getAllDepartments();
                String[] departmentArray = new String[departmentList.size()];

                for (int i = 0; i < departmentArray.length; i++) {
                    departmentArray[i] = toAscii(departmentList.get(i).getName());
                }

                int departmentValue = buildDepartmentView(departmentArray, input);
                user.setDepartment(departmentList.get(departmentValue - 1));
                break;

        }

        //weitere Attribute zur Suche hinzufügen ?
        if (checkInputForYesOrNo("Weitere Attribute zur Suche hinzufuegen ? (j/n)")) {
            println("");
            searchUser(user);
            return;
        }

        List<User> userList = searchUsers(user);
        println("");
        buildTable(userList);
    }

    private void buildTable(List<User> userList) {
        //TODO Erster Versuch mit starren breiten
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            printWhitespace(entrys, i, 1);
            if(i != entrys.length - 1) {
                print("|");
            }
        }
        println("");
        int max = getMaxEntry(entrys);
        for (int i = 0; i < (max * entrys.length) + (entrys.length * 2); i++) {
            print("-");
        }
        println("");
        for (int i = 0; i < userList.size(); i++) {
            printUserInTable(userList.get(i));
        }
    }

    private void printUserInTable(User user) {
        String[] userParams = userParameterToArray(user);
        int maxLength = getMaxEntry(entrys);
        for (int i = 0; i < entrys.length; i++) {
            if(userParams[i].length() >= maxLength) {
                userParams[i] = cutStringForList(userParams[i], maxLength);
            }
            print(userParams[i]);

            //TODO modify printWhitespace later
            for (int j = 0; j < maxLength - userParams[i].length() + 1; j++) {
                print(" ");
            }

            if (i != entrys.length - 1) {
                print("|");
            }
        }
        println("");
    }

    private String cutStringForList(String text, int max) {
        return text.substring(0, max - 2) + "..";
    }

    /**
     * Durchsucht alle Benutzer nach den Attributen von dem übergebenen User Objekt und gibt das Ergebnis als Liste zurueck
     * @param user das User Objekt mit den zu durchsuchenden Attributen
     * @return Ergebnisliste
     */
    private List<User> searchUsers(User user) {
        List<User> userList = concept.getAllUser();
        List<User> filteredUsers;
        //Filtert nach allen Scuhkriterien und gibt diesen als Liste zurück
        filteredUsers = userList.stream()
                                .filter(e -> {
                                    final User bUser = user;
                                    int paramNumber = getSettedParamsNumber(user);
                                    int checkNumber = 0;
                                    if (!bUser.getFirstname().equals("") && e.getFirstname().toLowerCase().contains(bUser.getFirstname().toLowerCase()))
                                        checkNumber++;
                                    if (!bUser.getLastname().equals("") && e.getLastname().toLowerCase().contains(bUser.getLastname().toLowerCase()))
                                        checkNumber++;
                                    if (bUser.getBirthday() != null && e.getBirthday().equals(bUser.getBirthday()))
                                        checkNumber++;
                                    if (e.getZipcode() == bUser.getZipcode())
                                        checkNumber++;
                                    if (!bUser.getCity().equals("") && e.getCity().toLowerCase().contains(bUser.getCity().toLowerCase()))
                                        checkNumber++;
                                    if (!bUser.getStreet().equals("") && e.getStreet().toLowerCase().contains(bUser.getStreet().toLowerCase()))
                                        checkNumber++;
                                    if (!bUser.getStreetnr().equals("") && e.getStreetnr().toLowerCase().contains(bUser.getStreetnr().toLowerCase()))
                                        checkNumber++;
                                    if (e.getDepartment().getId() != bUser.getDepartment().getId())
                                        checkNumber++;
                                    return paramNumber == checkNumber;
                                })
                                .collect(Collectors.toList());
        return filteredUsers;
    }

    private int getSettedParamsNumber(User user) {
        int number = 0;
        if (!user.getFirstname().equals(""))
            number++;
        if (!user.getLastname().equals(""))
            number++;
        if (user.getBirthday() != null)
            number++;
        if (user.getZipcode() != 0)
            number++;
        if (!user.getCity().equals(""))
            number++;
        if (!user.getStreet().equals(""))
            number++;
        if (!user.getStreetnr().equals(""))
            number++;
        if (user.getDepartment().getId() != 0)
            number++;
        return number;
    }

    private void showAllUsers() {
        buildTable(concept.getAllUser());
        println("");
        print("Zum Abbrechen Taste drücken");
        readString();
        showMainMenu();
    }

    private int buildDepartmentView(String[] departmentArray, int input) {

        //Aufbauen der Struktur
        for (int i = 0; i < departmentArray.length; i++) {
            print(departmentArray[i]);
            printWhitespace(departmentArray, i, 2);
            println("(" + (i + 1) + ")");
        }
        int departmentValue = 0;
        do {
            print("Neuen Wert fuer " + entrys[input-1] + " eingeben: ");
            departmentValue = readInt();
            if (departmentValue > 0 && departmentValue <= departmentArray.length) {
                break;
            }
            println(RED, "Eingabe nicht gueltig. Wert muss zwischen 1 und " + departmentArray.length + " liegen");
        } while (true);
        return departmentValue;
    }

    private int determineBirthday(String text, int min, int max) {
        do {
            print(text + ": ");
            int value = readInt();
            println("");
            if (value >= min && value <= max) {
                return value;
            }
            println(RED, "Der Wert muss zwischen " + min + " und " + max + " liegen");
        } while (true);
    }

    /**
     * Schreibt eine Nachricht in die Konsole und prüft, ob mit 'j' oder 'n' eingegeben wurde
     * @param message die anzuzeigende Nachricht
     * @return true, wenn 'j' eingegeben wurde. Andernfalls falls
     */
    private boolean checkInputForYesOrNo(String message){
        String input = null;
        while(true) {
            println("");
            print(message + " ");
            input = readString();
            if (input.length() == 1 && input.toLowerCase().charAt(0) == 'j' || input.toLowerCase().charAt(0) == 'n') {
                break;
            }
            println("");
            printErrorMessage("Bitte 'j' oder 'n' eingeben");
        }
        if(input.toLowerCase().equals("j")) {
            return true;
        }
        return false;
    }

    /**
     * Schreibt die Benutzer Informationen in die Konsole. Da Dies in mehreren Methoden benutzt wird und dieses
     * sich selbst wieder aufrufen, wird mit Hilfe von Reflection gearbeit, um dieses dynamisch gestalten zu können
     * @param methodName Methodenname der aufgerufen wird
     * @return den User zu der eingegebenen ID
     */
    private User searchAndPrintUserStats(String methodName, boolean editMenu){
        //hole Methode um über Reflection diese aufrufen zu können
        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        clean();
        writeHeader(2);
        print("Benutzer ID eingeben: ");
        int id = readInt();
        println("");

        User user = null;
        try {
            user = concept.getUser(id);
            if (user == null) {
                throw new Exception();
            }
        } catch(Exception e) {
            printErrorMessage("User wurde nicht gefunden");
            methodCall(method, this);
            return null;
        }
        writeUserStats(user, editMenu);
        return user;
    }

    /**
     * Ruft die jeweilige Methode auf.
     * @param method Methode, die aufgerufen werden soll
     */
    private void methodCall(Method method, Object obj) {
        try {
            method.invoke(obj);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Schreibt die Benutzerdaten in die Konsole.
     * Wenn editMenu true ist, wird hinter Attributen eine Nummerierung gelistet
     * @param user Benutzer, dessen Attribute angezeigt werden sollen
     * @param editMenu Wenn true, wird hinter den Attributen eine Nummerierung gelistet
     */
    private void writeUserStats(User user, boolean editMenu){
        String[] params = userParameterToArray(user);
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            printWhitespace(entrys, i, 2);
            print(": " + params[i]);
            if(editMenu) {
                printWhitespace(params, i, 2);
                print("(" + (i + 1) + ")");
            }
            println("");
        }
    }

    /**
     * Sorgt für den gleichen Abstand in der Konsole von z.B. Doppelpunkten bei einer Liste
     * @param array Das Array mit den Werten, an denen die Abstände angepasst werden sollen
     * @param iteratorIndex Iterator der eigentlichen Aufzählung
     */
    private void printWhitespace(String[] array, int iteratorIndex, int spacing){
        int maxLength = getMaxEntry(array);
        for (int j = 0; j < maxLength - array[iteratorIndex].length() + spacing; j++) {
            print(" ");
        }
    }

    private String toAscii(String text) {
        return text; //on linux not needed @todo remove comment -> .replaceAll("ü", "ue"). replaceAll("ö", "oe").replaceAll("ä", "ae").replaceAll("ß", "ss");
    }

    /**
     * Wandelt die Attribute von User in ein String Array um
     * @param user der entsprechende User
     * @return Array mit den Attributen von User
     */
    private String[] userParameterToArray(User user){
        String[] params = new String[8];
        params[0] = toAscii(user.getFirstname());
        params[1] = toAscii(user.getLastname());
        params[2] = toAscii(user.getBirthday() != null ? user.getBirthday().toString() : "");
        params[3] = toAscii(user.getCity());
        params[4] = toAscii(user.getZipcode() != 0 ? (user.getZipcode() + "") : "");
        params[5] = toAscii(user.getStreet());
        params[6] = toAscii(user.getStreetnr());
        params[7] = toAscii(user.getDepartment() != null ? user.getDepartment().getName() : "");

        return params;
    }

    /**
     * Schreibt den Header in die Konsole
     * @param length länge der einzuhaltenen Leerzeichen
     */
    private void writeHeader(int length){
        println("*****************************************");
        print("*");
        for(int i = 0; i < length; i++)print(BLACK, " ");
        print(YELLOW, concept.getTitle());
        for(int i = 0; i < length; i++)print(BLACK, " ");
        println("*");
        println("*****************************************");
    }

    /**
     *  Liest eine Benutzereingabe ein, und gibt im Fehlerfall gleich eine Fehlermeldung aus (z. B. wenn die Auswahl ausserhalb des Bereiches liegt
     * @return -1 im Fehlerfall, ansonsten die eingegebene Zahl
     */
    private int readInt() {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        try {
            choice = scan.nextInt();
        } catch(InputMismatchException e){
            return -1;
        }
        return choice;
    }

    /**
     * Liest eine Benutzereingabe ein
     * @return eingelesenen String
     */
    private String readString() {
        Scanner scan = new Scanner(System.in);
        return scan.next();
    }

    /**
     * lässt das Programm für die angegebene Zeit schlafen
     * @param time gibt an, wie lange das Programm schlafen soll
     */
    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Schreibt eine Fehlermeldung bezüglich einer falschen Eingabe in die Konsole und gibt den gültigen Zahlenbereich an
     * @param length länge des gültigen Zahlenbereich
     */
    private void printWrongEntryErrorMessage(int length){
        println("");
        println(RED, "Falsche Eingabe, bitte eine Zahl zwischen 1 und " + length + " eingeben");
    }

    /**
     * Schreibt eine Fehlermeldung in die Konsole
     * @param text der Fehlermeldungstext
     */
    private void printErrorMessage(String text) {
        println("");
        println(RED, text);
    }

    /**
     * Gibt die länge des längsten String im Array zurück
     * @param array der zu untersuchende Array
     * @return die länge des längsten Strings oder 0, wenn kein String im Array ist
     */
    private int getMaxEntry(String[] array){
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            if(array[i].length() > max){
                max = array[i].length();
            }
        }
        return max;
    }

    /**
     * Schreibt auf die Kommandozeile Text mit entsprechender Farbe ohne Zeilenumbruch
     * @param color Farbe des Textes
     * @param text der anzuzeigende Text
     */
    private void print(Color color, String text){
        System.out.print(ansi().fg(color).a(text).fg(STANDARD_COLOR));
    }

    /**
     * Schreibt auf die Kommandozeile Text mit der gesetzten Standard Farbe ohne Zeilenumbruch
     * @param text der anzuzeigende Text
     */
    private void print(String text){
        print(STANDARD_COLOR, text);
        System.out.flush();
    }

    /**
     * Schreibt auf die Kommandozeile Text mit entsprechender Farbe mit Zeilenumbruch
     * @param color Farbe des Textes
     * @param text der anzuzeigende Text
     */
    private void println(Color color, String text){
        System.out.println(ansi().fg(color).a(text).fg(STANDARD_COLOR));
    }

    /**
     * Schreibt auf die Kommandozeile Text mit der gesetzten Standard Farbe mit Zeilenumbruch
     * @param text der anzuzeigende Text
     */
    private void println(String text){
        println(STANDARD_COLOR, text);
    }

    /**
     * Löscht den Inhalt der Kommandozeile
     */
    private void clean(){
        //System.out.println(ansi().eraseScreen());
        System.out.print("\033[2J\033[;H");
    }

    @Override
    public IConcept getConcept() {
        return this.concept;
    }

    @Override
    public void setConcept(IConcept concept) {
        this.concept = concept;
    }
}
