package de.oszimt.ui.impl.tui.util;

import de.oszimt.model.User;
import de.oszimt.ui.impl.tui.Menu;
import de.oszimt.ui.impl.tui.MenuBuilder;
import org.fusesource.jansi.Ansi;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by m588 on 24.10.2014.
 */
public class Helper {

    public static final Ansi.Color STANDARD_COLOR = WHITE;

    private static final char BORDER = '#';
    private static final int HEADER_MARGIN = 3;
    private static final int TABLE_COLUMN_MARGIN = 2;
    private static final char BORDER_CROSS_THIN = (char) 9532;//9532 ┼
    private static final char BORDER_CROSS_ONLY_BOTTOM_THIN = (char) 9543;//╇ 9543
    private static final char BORDER_HALF_CROSS_RIGHT_THIN = (char) 9500;//9500 ├
    private static final char BORDER_HALF_CROSS_TOP_THIN = (char) 9524;//9524 ┴
    private static final char BORDER_HALF_CROSS_BOTTOM_THIN = (char) 9516;//9516 ┬
    private static final char BORDER_HALF_CROSS_LEFT_THIN = (char) 9508;//9508 ┤
    private static final char BORDER_HALF_CROSS_BOTTOM_BOLD = (char) 9523;//┳ 9523
    private static final char BORDER_HALF_CROSS_RIGHT_ONLY_BOTTOM_THIN = (char) 9505;//┡ 9505
    private static final char BORDER_HALF_CROSS_LEFT_ONLY_BOTTOM_THIN = (char) 9513;//┩ 9513
    private static final char BORDER_HALF_CROSS_BOTTOM_ONLY_BOTTOM_THIN = (char) 9519;//┯ 9519
    private static final char BORDER_LEFT_BOTTOM_ROUNDED = (char) 9584;//9584 ╰
    private static final char BORDER_LEFT_TOP_ROUNDED = (char) 9581;//9581 ╭
    private static final char BORDER_RIGHT_BOTTOM_ROUNDED = (char) 9583;//9583 ╯
    private static final char BORDER_RIGHT_TOP_ROUNDED = (char) 9582;//9582 ╮
    private static final char BORDER_MINUS_THIN = (char) 9472;//9472 ─
    private static final char BORDER_MINUS_BOLD = (char) 9473;//━ 9473
    private static final char BORDER_LEFT_TOP_BOLD = (char) 9487;//┏ 9487
    private static final char BORDER_RIGHT_TOP_BOLD = (char) 9491;//┓ 9491
    private static final char BORDER_PIPE_BOLD = (char) 9475;//┃ 9475
    private static final char BORDER_PIPE_THIN = (char) 9474;//┃ 9475

    public static User createDummyUser() {
        return new User("", "", null, "", "", "", 0, null);
    }

    public static void buildMenue(String[] options) {
        buildMenue(options, STANDARD_COLOR, "");
    }

    public static void buildMenue(String[] options, Ansi.Color color, String message) {
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

    /**
     * Sorgt für den gleichen Abstand in der Konsole von z.B. Doppelpunkten bei einer Liste
     *
     * @param array         Das Array mit den Werten, an denen die Abstände angepasst werden sollen
     * @param iteratorIndex Iterator der eigentlichen Aufzählung
     */
    public static void printWhitespace(String[] array, int iteratorIndex, int spacing) {
        int maxLenght = getMaxEntry(array);
        print(repeat(' ', maxLenght - stripAnsiColor(array[iteratorIndex]).length() + spacing));
    }

    public static String toAscii(String text) {
        return text; //on linux not needed @todo remove comment -> .replaceAll("ü", "ue"). replaceAll("ö", "oe").replaceAll("ä", "ae").replaceAll("ß", "ss");
    }

    /**
     * Wandelt die Attribute von User in ein String Array um
     *
     * @param user der entsprechende User
     * @return Array mit den Attributen von User
     */
    public static String[] userParameterToArray(User user) {
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
    public static void writeHeader(String title) {
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

    public static String repeat(String ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }


    public static String repeat(char ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    public static String stripAnsiColor(String str) {
        return str.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    /**
     * Liest eine Benutzereingabe ein, und gibt im Fehlerfall gleich eine Fehlermeldung aus (z. B. wenn die Auswahl ausserhalb des Bereiches liegt
     *
     * @return -1 im Fehlerfall, ansonsten die eingegebene Zahl
     */
    public static int readInt() {
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
    public static String readString() {
        Scanner scan = new Scanner(System.in);
        return scan.next();
    }

    /**
     * Schreibt eine Fehlermeldung in die Konsole
     *
     * @param text der Fehlermeldungstext
     */
    public static void printErrorMessage(String text) {
        println("");
        println(RED, text);
    }

    /**
     * Gibt die länge des längsten String im Array zurück
     *
     * @param array der zu untersuchende Array
     * @return die länge des längsten Strings oder 0, wenn kein String im Array ist
     */
    public static int getMaxEntry(String[] array) {
        return Collections.max(Arrays.asList(array), (o1, o2) -> stripAnsiColor(o1).length() - stripAnsiColor(o2).length()).length();
    }

    /**
     * Schreibt auf die Kommandozeile Text mit entsprechender Farbe ohne Zeilenumbruch
     *
     * @param color Farbe des Textes
     * @param text  der anzuzeigende Text
     */
    public static void print(Ansi.Color color, String text) {
        System.out.print(colorString(color, text));
    }

    public static Ansi colorString(Ansi.Color color, String str) {
        return ansi().fg(color).a(str).fg(STANDARD_COLOR);
    }


    /**
     * Schreibt auf die Kommandozeile ein Zeichen
     *
     * @param ch anzuzeigendes Zeichen
     */
    public static void print(char ch) {
        print(ch + "");
    }

    /**
     * Schreibt auf die Kommandozeile Text mit der gesetzten Standard Farbe ohne Zeilenumbruch
     *
     * @param text der anzuzeigende Text
     */
    public static void print(String text) {
        print(STANDARD_COLOR, text);
        System.out.flush();
    }

    /**
     * Schreibt auf die Kommandozeile Text mit entsprechender Farbe mit Zeilenumbruch
     *
     * @param color Farbe des Textes
     * @param text  der anzuzeigende Text
     */
    public static void println(Ansi.Color color, String text) {
        System.out.println(colorString(color, text));
    }

    /**
     * Schreibt auf die Kommandozeile Text mit der gesetzten Standard Farbe mit Zeilenumbruch
     *
     * @param text der anzuzeigende Text
     */
    public static void println(String text) {
        println(STANDARD_COLOR, text);
    }

    /**
     * Schreibt auf die Kommandozeile ein Zeichen
     *
     * @param ch Zeichen
     */
    public static void println(char ch) {
        println(ch + "");
    }

    /**
     * Löscht den Inhalt der Kommandozeile
     */
    public static void clean() {
        //System.out.println(ansi().eraseScreen());
        System.out.print("\033[2J\033[;H");
    }

    public static String toShortUgly(int i, String[] entrys) {
        print(entrys[i]);
        printWhitespace(entrys, i, 2);
        print(": ");
        String value = readString();
//        println("");
        return value;
    }

    public static int determineBirthday(String text, int min, int max) {
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
        //TODO zwecks Reflection muss ich mir da noch was überlegen
//    public static void printGoOnYesNo(String method, String text){
//        print(text + " (j/n): ");
//        String jn = readString();
//        if(jn.compareTo("j") == 0){
//            this.methodCall(method,this);
//            return;
//        }else if(jn.compareTo("n") == 0){
//            showMainMenu();
//            return;
//        }else{
//            showMainMenu(RED, "'" + jn + "' ist keine gültige Eingabe und wird als Nein gewertet");
//        }
//    }

    public static void printUser(User user, String[] entrys){
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

    public static int printGetUserId(){
        int user_id;
        print("Bitte geben Sie die Kunden ID ein (z=zurück): ");
        String inpString = readString();

        if (inpString.trim().compareTo("z") == 0) {
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

    private static void printTable(String[][] table) {

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

    public static void printUserOptions(User user, String[] entrys){
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

    public static int buildDepartmentView(String[] departmentArray, int input, boolean deleteable, String[] entrys) {

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

    /**
     * Schreibt eine Nachricht in die Konsole und prüft, ob mit 'j' oder 'n' eingegeben wurde
     *
     * @param message die anzuzeigende Nachricht
     * @return true, wenn 'j' eingegeben wurde. Andernfalls falls
     */
    public static boolean checkInputForYesOrNo(String message) {
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
     * Durchsucht alle Benutzer nach den Attributen von dem übergebenen User Objekt und gibt das Ergebnis als Liste zurueck
     *
     * @param user das User Objekt mit den zu durchsuchenden Attributen
     * @return Ergebnisliste
     */
    public static List<User> searchUsers(User user, List<User> userList) {
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

    private static int getSettedParamsNumber(User user) {
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

    public static void buildTable(List<User> userList, int entriesPerPage, int page, String[] entrys) {
        int end = entriesPerPage * page + entriesPerPage;
        if(end > userList.size()){
            end = userList.size();
        }

        int start = entriesPerPage * page;

        String[][] table = new String[end - start + 1][entrys.length];

        for (int column = 0; column < entrys.length; column++) {
            table[0][column] = entrys[column];
        }


        int index;
        for (int row = start; row < end; row++) {
            String[] values = userParameterToArray(userList.get(row));
            for (int column = 0; column < values.length; column++) {
                index = row - entriesPerPage * page + 1;
                table[index][column] = values[column];
            }
        }
        printTable(table);
    }

    /**
     * Gibt eine Statische Variable einer Klasse zurück
     * @param clazz Klasse, aus der man die Variable lesen will
     * @param field Name der Variable
     * @return Inhalt der Variable
     */
    public static Object getDeclaredField(Class<Menu> clazz, String field) {
        try {
            return clazz.getDeclaredField(field).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e1) {
        }
        return null;
    }

    /**
     * Erzeugt eine neue Instanz der angegeben Klasse mit dem übergebenen MenuBuilder.
     * Nur brauchbar für die Instanziierung der Klassen im Package de.oszimt.ui.impl.tui.menu mit dem
     * Konstruktor Menu(MenuBuilder)
     * @param builder der jeweilige Builder
     * @return das erzeugt Menu
     */
    public static Menu getObject(Class<Menu> clazz, MenuBuilder builder){
        try {
            return clazz.getConstructor(MenuBuilder.class).newInstance(builder);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
