package de.oszimt.ui;

import de.oszimt.concept.iface.IConcept;
import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.ui.iface.UserInterface;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Created by user on 17.09.2014.
 */
public class Tui implements UserInterface {

    private final Color STANDARD_COLOR = WHITE;
    private IConcept concept;

    private final char BORDER = '#';
    private final int HEADER_MARGIN = 3;
    private final int TABLE_COLUMN_MARGIN = 2;
    private final char BORDER_CROSS_THIN = (char) 9532;//9532 ┼
    private final char BORDER_CROSS_ONLY_BOTTOM_THIN = (char) 9543;//╇ 9543
    private final char BORDER_HALF_CROSS_RIGHT_THIN = (char) 9500;//9500 ├
    private final char BORDER_HALF_CROSS_TOP_THIN = (char) 9524;//9524 ┴
    private final char BORDER_HALF_CROSS_BOTTOM_THIN = (char) 9516;//9516 ┬
    private final char BORDER_HALF_CROSS_LEFT_THIN = (char) 9508;//9508 ┤
    private final char BORDER_HALF_CROSS_BOTTOM_BOLD = (char) 9523;//┳ 9523
    private final char BORDER_HALF_CROSS_RIGHT_ONLY_BOTTOM_THIN = (char) 9505;//┡ 9505
    private final char BORDER_HALF_CROSS_LEFT_ONLY_BOTTOM_THIN = (char) 9513;//┩ 9513
    private final char BORDER_HALF_CROSS_BOTTOM_ONLY_BOTTOM_THIN = (char) 9519;//┯ 9519
    private final char BORDER_LEFT_BOTTOM_ROUNDED = (char) 9584;//9584 ╰
    private final char BORDER_LEFT_TOP_ROUNDED = (char) 9581;//9581 ╭
    private final char BORDER_RIGHT_BOTTOM_ROUNDED = (char) 9583;//9583 ╯
    private final char BORDER_RIGHT_TOP_ROUNDED = (char) 9582;//9582 ╮
    private final char BORDER_MINUS_THIN = (char) 9472;//9472 ─
    private final char BORDER_MINUS_BOLD = (char) 9473;//━ 9473
    private final char BORDER_LEFT_TOP_BOLD = (char) 9487;//┏ 9487
    private final char BORDER_RIGHT_TOP_BOLD = (char) 9491;//┓ 9491
    private final char BORDER_PIPE_BOLD = (char) 9475;//┃ 9475
    private final char BORDER_PIPE_THIN = (char) 9474;//┃ 9475


    private boolean useRestService = false;

    private String[] entrys = {"Vorname",
            "Nachname",
            "Geburtstag",
            "Stadt",
            "Postleitzahl",
            "Strasse",
            "Strassennr.",
            "Abteilung"};

    public Tui(IConcept concept) {
        this.concept = concept;

        AnsiConsole.systemInstall();

        showMainMenu();
    }

    /**
     * Schreibt das Hauptmenue in die Konsole
     */
    private void showMainMenu() {
        showMainMenu(STANDARD_COLOR, "");
    }

    /**
     * Schreibt das Hauptmenue in die Konsole
     *
     * @param color
     * @param message
     */
    private void showMainMenu(Color color, String message) {
        clean();

        String[] entrys = {
                "Benutzer anlegen",
                "Benutzer anzeigen",
                "Benutzer bearbeiten",
                "Benutzer loeschen",
                "Benutzer suchen",
                "Alle Benutzer Anzeigen",
                "Einstellugen",
                "Beenden"};

        writeHeader(getConcept().getTitle());
        buildMenue(entrys,color,message);


        //einlesen des Input´s
        int input = readInt();

        //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
        if (input == -1 || input > entrys.length || input < 1) {
            showMainMenu(RED,"Falsche Eingabe, bitte eine Zahl zwischen 1 und " + entrys.length + " eingeben");
            return;
        }

        //Prüfung, welches Menue aufgerufen werden soll
        switch (input) {
            case 1:
                createUser();
                break;
            case 2:
                showUser();
                break;
            case 3:
                editUser();
                break;
            case 4:
                deleteUser();
                break;
            case 5:
                searchUser(createDummyUser());
                break;
            case 6:
                showAllUsers();
                break;
            case 7:
                settigns();
                break;
            case 8:
                System.exit(0);

        }
    }

    private void buildMenue(String[] options) {
        buildMenue(options, STANDARD_COLOR, "");
    }

    private void buildMenue(String[] options, Color color, String message) {
        //Aufbauen des Menue´s
        for (int i = 0; i < options.length; i++) {
            print(" " + options[i]);
            printWhitespace(options, i, 2);
            print("(");
            print(BLUE, i + 1 + "");
            println(")");
        }
        println(color, message);
        print(" Menuepunkt eingeben: ");
    }

    private void settigns() {
        settigns(STANDARD_COLOR, "");
    }

    private void settigns(Color color, String message) {
        clean();

        writeHeader("Einstellungen");
        String[] StettingLabels = {
                "Alle Kunden löschen",
                "Zufalls Kunden erstellen",
                "REST Service (PLZ -> Stadt) [" +
                        (useRestService ?
                                colorString(GREEN, "aktiv") :
                                colorString(RED, "inaktiv")
                        ) + "]",
                "Zurück"
        };


        buildMenue(StettingLabels, color, message);


        //einlesen des Input´s
        int input = readInt();
        switch (input) {
            case 1:
                getConcept().getAllUser().forEach(u -> getConcept().deleteUser(u));
                settigns(GREEN, "Information: Alle Kunden wurden gelöscht");
                break;
            case 2:
                getConcept().createRandomUsers(useRestService);
                settigns(GREEN, "Information: Zufalls Kunden wurden erstellt");
                break;
            case 3:
                useRestService = !useRestService;
                settigns();
                break;
            case 4:
                showMainMenu();
                break;
            default:
                settigns(RED, "Fehler: Falsche Eingabe, versuchen Sie es erneut");

        }

    }

    private void printlnSuccessMessage(String message) {
        println(GREEN, message);
    }

    private void createUser() {
        writeHeader("Neuen Benutzer anlegen");
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

    private User createDummyUser() {
        return new User("", "", null, "", "", "", 0, null);
    }

    private void showUser() {
        showUser(STANDARD_COLOR,"");
    }
    private void showUser(Color color, String message) {
        clean();

        writeHeader("Kunden anzeigen");
        if(message.length() > 0){
            println(color, message);
        }
        int inp = printGetUserId();

        if(inp == -1){
            showUser(RED,"Ungültige Kunden ID");
            return;
        }
        User user = getConcept().getUser(inp);
        if(user == null){
            showUser(RED,"Kein Benutzer mit der ID gefunden");
            return;
        }
        printUser(user);
        printGoOnYesNo("showUser","Weiteren benutzer anzeigen");
    }

    private void printGoOnYesNo(String method, String text){
        print(text + " (j/n): ");
        String jn = readString();
        if(jn.compareTo("j") == 0){
            this.methodCall(method,this);
            return;
        }else if(jn.compareTo("n") == 0){
            showMainMenu();
            return;
        }else{
            showMainMenu(RED, "'" + jn + "' ist keine gültige Eingabe und wird als Nein gewertet");
        }
    }

    private void printUser(User user){
        String[][] table = new String[entrys.length+1][2];
        table[0][0] = "Eigenschaft";
        table[0][1] = "Wert";
        String[] values = userParameterToArray(user);
        for (int i = 0; i < entrys.length; i++) {
            table[i+1][0] = entrys[i];
            table[i+1][1] = values[i].toString().length() < 1 ? "-" : values[i];
        }
        printTable(table);
    }

    private void printUserOptions(User user){
        String[][] table = new String[entrys.length+1][3];
        table[0][0] = "Eigenschaft";
        table[0][1] = "Wert";
        table[0][2] = "Auswahl";
        String[] values = userParameterToArray(user);
        for (int i = 0; i < entrys.length; i++) {
            table[i+1][0] = entrys[i];
            table[i+1][1] = values[i].toString().length() < 1 ? "-" : values[i];
            table[i+1][2] = "" + (i+1);
        }
        printTable(table);
    }

    private void editUser(){
        editUser(STANDARD_COLOR,"",-1);
    }
    private void editUser(Color color, String message){
        editUser(color,message,-1);
    }

    private int printGetUserId(){
        int user_id;
        print("Bitte geben Sie die Kunden ID ein (z=zurück): ");
        String inpString = readString();

        if (inpString.trim().compareTo("z") == 0) {
            showMainMenu();
            return -1;
        }

        int inp;
        try {
            inp = Integer.parseInt(inpString);
        } catch (NumberFormatException e) {
            inp = -1;
        }
        user_id = inp;
        return user_id;
    }
    private void editUser(Color color, String message, int kunden_id) {
        clean();
        boolean errorshown = false;
        writeHeader("Kunden bearbeiten");
        if(kunden_id < 0) {
            if (message.length() > 0) {
                println(color, message);
                errorshown = true;
            }
            kunden_id = printGetUserId();
        }

        if (kunden_id == -1) {
            editUser(RED, "Ungültige Kunden ID");
            return;
        }
        User user = getConcept().getUser(kunden_id);
        if (user == null) {
            editUser(RED, "Kein Benutzer mit der ID gefunden");
            return;
        }

        printUserOptions(user);
        if (message.length() > 0 && !errorshown) {
            println(color, message);
        }
        int input = 0;
        do {
            println("");
            print("Wählen Sie die zuändernde Eigenschaft aus (0=zurück | 1-" + entrys.length + "): ");

            //einlesen des Input´s
            input = readInt();

            //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
            if (input > -1 && input <= entrys.length) {
                break;
            }
            println(RED, "Falsche Eingabe");
        } while (true);

        if(input == 0){
            showMainMenu();
            return;
        }

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

                int departmentValue = buildDepartmentView(departmentArray, input,false);

                user.setDepartment(departmentList.get(departmentValue - 1));
                break;

        }
        concept.upsertUser(user);
        editUser(GREEN,"Änderung erfolgreich übernommen",user.getId());

    }

    private void deleteUser(){
        deleteUser(STANDARD_COLOR,"");
    }

    private void deleteUser(Color color, String message) {
        clean();
        writeHeader("Benutzer löschen");
        if(message.length() > 0){
            println(color,message);
        }
        int user_id = printGetUserId();

        if (user_id == -1) {
            deleteUser(RED, "Ungültige Kunden ID");
            return;
        }
        User user = getConcept().getUser(user_id);
        if (user == null) {
            deleteUser(RED, "Kein Benutzer mit der ID gefunden");
            return;
        }

        printUser(user);

        //Soll der Benutzer wirklich gelöscht werden ?
        if (checkInputForYesOrNo("Benutzer wirklich löschen ? (j/n)")) {
            concept.deleteUser(user);
        }

        //Soll ein weiterer Benutzer gelöscht werden ?
        printGoOnYesNo("deleteUser", "Einen weiteren Benutzer löschen");
    }

    private void searchUser(User user) {
        searchUser(user,STANDARD_COLOR,"");
    }
    private void searchUser(User user, Color color, String message) {
        clean();
        writeHeader("Benutzer Suchen");

        printUserOptions(user);

        int input = 0;
        String inputString;

        if(message.length() > 0){
            println(color,message);
        }

        print("Welches Attribut zur Suche hinzufuegen ? (1-" + entrys.length + ", s = suchen, 0 = zum Hauptmenü): ");
        //einlesen des Input´s
        inputString = readString();
        if(inputString.trim().compareTo("0") == 0){
            showMainMenu();
            return;
        }else if(inputString.trim().compareTo("s") == 0){

        }else {
            try {
                input = Integer.parseInt(inputString);

            } catch (NumberFormatException e) {
                input = 0;
            }
            //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben

            if (input < 1 || input > entrys.length) {
                searchUser(user, RED, "Falsche Eingabe");
                return;
            }

            String value;
            //Prüfung, welches Menue aufgerufen werden soll
            User tmp = createDummyUser();
            switch (input) {

                //Vorname
                case 1:
                    print("Wert für " + entrys[input - 1] + " eingeben (\"-\" = leeren): ");
                    value = readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setFirstname(tmp.getFirstname());
                    }else{
                        user.setFirstname(value);
                    }
                   break;

                //Nachname
                case 2:
                    print("Wert für " + entrys[input - 1] + " eingeben  (\"-\" = leeren): ");
                    value = readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setLastname(tmp.getLastname());
                    }else{
                        user.setLastname(value);
                    }
                    break;

                //Geburtstag
                case 3:
                    LocalDate date = null;
                    do {
                        println("Geburtstag eingeben (zum leeren alle Werte = 0 setzen) ");
                        int day = determineBirthday("Tag", 0, 31);
                        int month = determineBirthday("Monat", 0, 12);
                        int year = determineBirthday("Jahr", 1900, LocalDate.now().getYear());


                        if(day + month + year == 0){
                            user.setBirthday(tmp.getBirthday());
                            break;
                        }


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
                    print("Wert fuer " + entrys[input - 1] + " eingeben (\"-\" = leeren):  ");
                    value = readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setCity(tmp.getCity());
                    }else{
                        user.setCity(value);
                    }
                    break;

                //PLZ
                case 5:
                    print("Wert fuer " + entrys[input - 1] + " eingeben  eingeben (\"-\" = leeren): ");
                    value = readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setPLZ(tmp.getZipcode());
                    }else{
                        user.setPLZ(Integer.parseInt(value));
                    }
                    break;

                //Strasse
                case 6:
                    print("Wert fuer " + entrys[input - 1] + " eingeben (\"-\" = leeren):  ");
                    value = readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setStreet(tmp.getStreet());
                    }else{
                        user.setStreet(value);
                    }
                    break;

                //Stassen-Nummer
                case 7:
                    print("Wert fuer " + entrys[input - 1] + " eingeben (\"-\" = leeren): ");
                    value = readString();
                    if(value.trim().compareTo("-") == 0){

                        user.setStreetnr(tmp.getStreetnr());
                    }else{
                        user.setStreetnr(value);
                    }
                    break;

                //Abteilung
                case 8:
                    List<Department> departmentList = concept.getAllDepartments();
                    String[] departmentArray = new String[departmentList.size()];

                    for (int i = 0; i < departmentArray.length; i++) {
                        departmentArray[i] = toAscii(departmentList.get(i).getName());
                    }

                    int departmentValue = buildDepartmentView(departmentArray, input,true);
                    if(departmentValue == 0){
                        user.setDepartment(tmp.getDepartment());
                    }else{
                        user.setDepartment(departmentList.get(departmentValue - 1));
                    }
                    break;

            }

            searchUser(user,GREEN,"Suche erweitert");
            return;
        }

        List<User> userList = searchUsers(user);
        buildTable(userList,userList.size(),0);
        println("Filter ändern (1), Neue Suche(2), zum Hauptmenü (3)");
        int option = -1;
        boolean first = true;
        do {
            if(first){
                first = false;
            }else{
                println(RED, "Falsche Eingabe");
            }
             option = readInt();
        }while (option < 1 || option > 3);
        switch (option){
            case 1:
                searchUser(user);
                return;
            case 2:
                searchUser(createDummyUser());
                return;
            default:
                showMainMenu();
                return;
        }

    }

    //todo remove
    private void charmap() {
        for (int i = 8000; i < 10000; i++) {
            print(" " + ((char) i) + " " + i + " ");
            if (i % 50 == 0) System.out.println();
        }
    }

    private void printTable(String[][] table) {

        int[] maxColumnLengths = new int[table[0].length];

        for (int row = 0; row < table.length; row++) {
            for (int column = 0; column < table[row].length; column++) {
                if(row == 0){
                    maxColumnLengths[column] = table[row][column].length();
                }else if (table[row][column].length() > maxColumnLengths[column]) {
                    maxColumnLengths[column] = table[row][column].length();
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        StringBuilder sbLastLine = new StringBuilder();
        StringBuilder sbFirstLine = new StringBuilder();
        StringBuilder sbSecoundLine = new StringBuilder();
        println("");
        for (int i = 0; i < maxColumnLengths.length; i++) {
            sb.append(i == 0 ? BORDER_HALF_CROSS_RIGHT_THIN : BORDER_CROSS_THIN);
            sbLastLine.append(i == 0 ? BORDER_LEFT_BOTTOM_ROUNDED : BORDER_HALF_CROSS_TOP_THIN);
            sbFirstLine.append(i == 0 ? BORDER_LEFT_TOP_BOLD : BORDER_HALF_CROSS_BOTTOM_BOLD);
            sbSecoundLine.append(i == 0 ? BORDER_HALF_CROSS_RIGHT_ONLY_BOTTOM_THIN : BORDER_CROSS_ONLY_BOTTOM_THIN);
            for (int j = 0; j < maxColumnLengths[i] + 2 * TABLE_COLUMN_MARGIN; j++) {
                sb.append(BORDER_MINUS_THIN);
                sbLastLine.append(BORDER_MINUS_THIN);
                sbFirstLine.append(BORDER_MINUS_BOLD);
                sbSecoundLine.append(BORDER_MINUS_BOLD);
            }
        }
        sb.append(BORDER_HALF_CROSS_LEFT_THIN);
        sbLastLine.append(BORDER_RIGHT_BOTTOM_ROUNDED);
        sbFirstLine.append(BORDER_RIGHT_TOP_BOLD);
        sbSecoundLine.append(BORDER_HALF_CROSS_LEFT_ONLY_BOTTOM_THIN);
        String rowSeperator = sb.toString();
        String lastLine = sbLastLine.toString();
        String firstLine = sbFirstLine.toString();
        String secoundLine = sbSecoundLine.toString();
        println(firstLine);

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {

                print(((i == 0 ? BORDER_PIPE_BOLD : BORDER_PIPE_THIN)) + (i == 0 ?
                                ansi().bold().bg(WHITE).fg(BLACK).a("  " + table[i][j]).fg(WHITE).bg(DEFAULT) :
                                ansi().boldOff().a("  " + table[i][j])).toString()
                );
                print(repeat("" + (i == 0 ?
                        ansi().bg(WHITE).fg(BLACK).a(" ").fg(WHITE).bg(DEFAULT) :
                        ansi().a(" ")),  maxColumnLengths[j] - table[i][j].length() + TABLE_COLUMN_MARGIN)
                );

            }
            println(i == 0 ? BORDER_PIPE_BOLD : BORDER_PIPE_THIN);
            println(i == table.length - 1 ? lastLine : (i == 0 ? secoundLine : rowSeperator ));
        }
        println(table.length == 1 ? ansi().fg(YELLOW).a("Keine Einträge vorhanden").boldOff() + "" : "");
    }

    private void showAllUsers(int entriesPerPage, int page, List<User> userList) {
        clean();
        buildTable(userList,entriesPerPage,page);
        print("Seite " + (page + 1) + "/" + ( userList.size() / entriesPerPage) + " (0: Zurück):");
        String pageStr = readString();
        try {
            page = new Integer(pageStr) - 1;
        } catch (NumberFormatException e) {}

        if(page * entriesPerPage >= userList.size()){
            page = 0;
        }

        if(page < 0){
            showMainMenu();
            return;
        }

        showAllUsers(entriesPerPage,page,userList);

    }



    private void showAllUsers() {
        clean();
        print("Anzahl der Benutzer pro Seite (5):");
        String entriesPerPageStr = readString();
        int entiresPerPage = 5;
        try {
            entiresPerPage = new Integer(entriesPerPageStr);
        } catch (NumberFormatException e) {}

        List<User> userList = concept.getAllUser();
        showAllUsers(entiresPerPage,0,userList);
    }

    private void buildTable(List<User> userList, int entriesPerPage, int page) {
        String[][] table = new String[entriesPerPage + 1][entrys.length];

        for (int column = 0; column < entrys.length; column++) {
            table[0][column] = entrys[column];
        }

        for (int row = entriesPerPage * page; row < (entriesPerPage * page + entriesPerPage); row++) {
            String[] values = userParameterToArray(userList.get(row));
            for (int column = 0; column < values.length; column++) {
                table[row - entriesPerPage * page + 1][column] = values[column];
            }
        }
        printTable(table);
    }

    private void printUserInTable(User user) {
        String[] userParams = userParameterToArray(user);
        int maxLength = getMaxEntry(entrys);
        for (int i = 0; i < entrys.length; i++) {
            if (userParams[i].length() >= maxLength) {
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
     *
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
                    if (!bUser.getFirstname().equals("") && e.getFirstname().trim().toLowerCase().contains(bUser.getFirstname().trim().toLowerCase()))
                        checkNumber++;
                    if (!bUser.getLastname().equals("") && e.getLastname().trim().toLowerCase().contains(bUser.getLastname().trim().toLowerCase()))
                        checkNumber++;
                    if (bUser.getBirthday() != null && e.getBirthday().equals(bUser.getBirthday()))
                        checkNumber++;
                    if (e.getZipcode() == bUser.getZipcode())
                        checkNumber++;
                    if (!bUser.getCity().equals("") && e.getCity().toLowerCase().trim().contains(bUser.getCity().trim().toLowerCase()))
                        checkNumber++;
                    if (!bUser.getStreet().equals("") && e.getStreet().trim().toLowerCase().contains(bUser.getStreet().trim().toLowerCase()))
                        checkNumber++;
                    if (!bUser.getStreetnr().equals("") && e.getStreetnr().trim().toLowerCase().contains(bUser.getStreetnr().trim().toLowerCase()))
                        checkNumber++;

                    if (bUser.getDepartment() != null) {
                        if (e.getDepartment().getId() == bUser.getDepartment().getId())
                            checkNumber++;
                    }
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
        if (user.getDepartment() != null)
            number++;
        return number;
    }



    private int buildDepartmentView(String[] departmentArray, int input, boolean deleteable) {

        //Aufbauen der Struktur
        for (int i = 0; i < departmentArray.length; i++) {
            print(departmentArray[i]);
            printWhitespace(departmentArray, i, 2);
            println("(" + (i + 1) + ")");
        }
        int departmentValue = 0;
        do {
            print("Neuen Wert fuer " + entrys[input - 1] + " eingeben" + (deleteable ? "(0 = leeren)" : "") + ":");
            departmentValue = readInt();
            if (departmentValue > (deleteable ? -1 : 0) && departmentValue <= departmentArray.length) {
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
            if ((value >= min && value <= max) || value == 0) {
                return value;
            }
            println(RED, "Der Wert muss zwischen " + min + " und " + max + " liegen");
        } while (true);
    }

    /**
     * Schreibt eine Nachricht in die Konsole und prüft, ob mit 'j' oder 'n' eingegeben wurde
     *
     * @param message die anzuzeigende Nachricht
     * @return true, wenn 'j' eingegeben wurde. Andernfalls falls
     */
    private boolean checkInputForYesOrNo(String message) {
        String input = null;
        while (true) {
            println("");
            print(message + " ");
            input = readString();
            if (input.length() == 1 && input.toLowerCase().charAt(0) == 'j' || input.toLowerCase().charAt(0) == 'n') {
                break;
            }
            println("");
            printErrorMessage("Bitte 'j' oder 'n' eingeben");
        }
        if (input.toLowerCase().equals("j")) {
            return true;
        }
        return false;
    }

    /**
     * Schreibt die Benutzer Informationen in die Konsole. Da Dies in mehreren Methoden benutzt wird und dieses
     * sich selbst wieder aufrufen, wird mit Hilfe von Reflection gearbeit, um dieses dynamisch gestalten zu können
     *
     * @param methodName Methodenname der aufgerufen wird
     * @return den User zu der eingegebenen ID
     */
    private User searchAndPrintUserStats(String methodName, boolean editMenu) {
        //hole Methode um über Reflection diese aufrufen zu können

        clean();
        writeHeader(getConcept().getTitle());
        print("Benutzer ID eingeben: ");
        int id = readInt();
        println("");
        User user = null;
        try {
            user = concept.getUser(id);
            if (user == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            printErrorMessage("User wurde nicht gefunden");
            methodCall(methodName, this);
            return null;
        }
        writeUserStats(user, editMenu);
        return user;
    }

    /**
     * Ruft die jeweilige Methode auf.
     *
     * @param methodName Methode, die aufgerufen werden soll
     */
    private Object methodCall(String methodName, Object obj) {

        Method method = null;
        try {
            method = this.getClass().getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            return method.invoke(obj);
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * Schreibt die Benutzerdaten in die Konsole.
     * Wenn editMenu true ist, wird hinter Attributen eine Nummerierung gelistet
     *
     * @param user     Benutzer, dessen Attribute angezeigt werden sollen
     * @param editMenu Wenn true, wird hinter den Attributen eine Nummerierung gelistet
     */
    private void writeUserStats(User user, boolean editMenu) {
        String[] params = userParameterToArray(user);
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            printWhitespace(entrys, i, 2);
            print(": " + params[i]);
            if (editMenu) {
                printWhitespace(params, i, 2);
                print("(" + (i + 1) + ")");
            }
            println("");
        }
    }

    /**
     * Sorgt für den gleichen Abstand in der Konsole von z.B. Doppelpunkten bei einer Liste
     *
     * @param array         Das Array mit den Werten, an denen die Abstände angepasst werden sollen
     * @param iteratorIndex Iterator der eigentlichen Aufzählung
     */
    private void printWhitespace(String[] array, int iteratorIndex, int spacing) {
        int maxLenght = getMaxEntry(array);
        print(repeat(' ', maxLenght - stripAnsiColor(array[iteratorIndex]).length() + spacing));
    }

    private String stripAnsiColor(String str) {
        return str.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    private String toAscii(String text) {
        return text; //on linux not needed @todo remove comment -> .replaceAll("ü", "ue"). replaceAll("ö", "oe").replaceAll("ä", "ae").replaceAll("ß", "ss");
    }

    /**
     * Wandelt die Attribute von User in ein String Array um
     *
     * @param user der entsprechende User
     * @return Array mit den Attributen von User
     */
    private String[] userParameterToArray(User user) {
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
     *
     * @param title länge der einzuhaltenen Leerzeichen
     */
    private void writeHeader(String title) {
        StringBuilder sbHeader = new StringBuilder();

        sbHeader.append(BORDER_PIPE_THIN);
        sbHeader.append(repeat(' ',HEADER_MARGIN));
        sbHeader.append(ansi().bold().fg(BLUE).a(title).boldOff().fg(STANDARD_COLOR));
        sbHeader.append(repeat(' ',HEADER_MARGIN));
        sbHeader.append(BORDER_PIPE_THIN);
        int length = stripAnsiColor(sbHeader.toString()).length();
        sbHeader.insert(0,'\n');
        sbHeader.insert(0,BORDER_RIGHT_TOP_ROUNDED);
        sbHeader.insert(0,repeat(BORDER_MINUS_THIN,length - 2 ));
        sbHeader.insert(0,BORDER_LEFT_TOP_ROUNDED);
        sbHeader.append('\n');
        sbHeader.append(BORDER_LEFT_BOTTOM_ROUNDED);
        sbHeader.append(repeat(BORDER_MINUS_THIN, length - 2));
        sbHeader.append(BORDER_RIGHT_BOTTOM_ROUNDED);
        println(sbHeader.toString());

    }

    private String repeat(String ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }


    private String repeat(char ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Liest eine Benutzereingabe ein, und gibt im Fehlerfall gleich eine Fehlermeldung aus (z. B. wenn die Auswahl ausserhalb des Bereiches liegt
     *
     * @return -1 im Fehlerfall, ansonsten die eingegebene Zahl
     */
    private int readInt() {
        Scanner scan = new Scanner(System.in);
        int choice = 0;
        try {
            choice = scan.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        }
        return choice;
    }

    /**
     * Liest eine Benutzereingabe ein
     *
     * @return eingelesenen String
     */
    private String readString() {
        Scanner scan = new Scanner(System.in);
        return scan.next();
    }



    /**
     * Schreibt eine Fehlermeldung in die Konsole
     *
     * @param text der Fehlermeldungstext
     */
    private void printErrorMessage(String text) {
        println("");
        println(RED, text);
    }

    /**
     * Gibt die länge des längsten String im Array zurück
     *
     * @param array der zu untersuchende Array
     * @return die länge des längsten Strings oder 0, wenn kein String im Array ist
     */
    private int getMaxEntry(String[] array) {
        return Collections.max(Arrays.asList(array), (o1, o2) -> stripAnsiColor(o1).length() - stripAnsiColor(o2).length()).length();
    }

    /**
     * Schreibt auf die Kommandozeile Text mit entsprechender Farbe ohne Zeilenumbruch
     *
     * @param color Farbe des Textes
     * @param text  der anzuzeigende Text
     */
    private void print(Color color, String text) {
        System.out.print(colorString(color, text));
    }

    private Ansi colorString(Color color, String str) {
        return ansi().fg(color).a(str).fg(STANDARD_COLOR);
    }


    /**
     * Schreibt auf die Kommandozeile ein Zeichen
     *
     * @param ch anzuzeigendes Zeichen
     */
    private void print(char ch) {
        print(ch + "");
    }

    /**
     * Schreibt auf die Kommandozeile Text mit der gesetzten Standard Farbe ohne Zeilenumbruch
     *
     * @param text der anzuzeigende Text
     */
    private void print(String text) {
        print(STANDARD_COLOR, text);
        System.out.flush();
    }

    /**
     * Schreibt auf die Kommandozeile Text mit entsprechender Farbe mit Zeilenumbruch
     *
     * @param color Farbe des Textes
     * @param text  der anzuzeigende Text
     */
    private void println(Color color, String text) {
        System.out.println(colorString(color, text));
    }

    /**
     * Schreibt auf die Kommandozeile Text mit der gesetzten Standard Farbe mit Zeilenumbruch
     *
     * @param text der anzuzeigende Text
     */
    private void println(String text) {
        println(STANDARD_COLOR, text);
    }

    /**
     * Schreibt auf die Kommandozeile ein Zeichen
     *
     * @param ch Zeichen
     */
    private void println(char ch) {
        println(ch + "");
    }

    /**
     * Löscht den Inhalt der Kommandozeile
     */
    private void clean() {
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
