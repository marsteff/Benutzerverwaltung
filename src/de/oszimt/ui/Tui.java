package de.oszimt.ui;

import de.oszimt.concept.iface.IConcept;

import de.oszimt.model.User;
import org.fusesource.jansi.AnsiConsole;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.InputMismatchException;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Created by user on 17.09.2014.
 */
public class Tui {

    private final Color STANDARD_COLOR = WHITE;
    private IConcept concept;
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
        String[] entrys = { "Benutzer anzeigen",
                            "Benutzer bearbeiten",
                            "Benutzer loeschen",
                            "Benutzer suchen",
                            "Alle Benutzer Anzeigen",
                            "Abbrechen"};
        writeHeader(3);
        int max = getMaxEntry(entrys);

        //Aufbauen des Menue´s
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            for (int j = 0; j < max - entrys[i].length() + 2; j++) {
                print(" ");
            }
            println("(" + (i + 1) + ")" );
        }
        println("");
        print("Menuepunkt eingeben: ");

        //einlesen des Input´s
        int input = readInt();

        //im Fehlerfall oder wenn Eingabe ausserhalb des Gültigkeitsbereiches Fehlermeldung ausgeben
        if(input == -1 || input > entrys.length || input < 1) {
            printWrongEntryErrorMessage(6);
            sleep(1500);
            showMainMenu();
            return;
        }

        //Prüfung, welches Menue aufgerufen werden soll
        switch (input){
            case 1: showUser();
                    return;
            case 2: editUser();
                    return;
            case 3: deleteUser();
                    return;
            case 4: searchUser();
                    return;
            case 5: showAllUsers();
                    return;
            case 6: System.exit(0);

        }
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
        User user = searchAndPrintUserStats(stack[1].getMethodName(), false);


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

    private void searchUser() {

    }

    private void showAllUsers() {

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
     * @param methodName
     * @return
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
        String[] entrys = { "Vorname",
                            "Nachname",
                            "Geburtstag",
                            "Stadt",
                            "Postleitzahl",
                            "Strasse",
                            "Strassen-Nummer",
                            "Abteilung"};

        String[] params = userParameterToArray(user);
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            printWhitespace(entrys, i);
            print(": " + params[i]);
            if(editMenu) {
                printWhitespace(params, i);
                print("(" + i + 1 + ")");
            }
            println("");
        }
    }

    /**
     * Sorgt für den gleichen Abstand in der Konsole von z.B. Doppelpunkten bei einer Liste
     * @param array Das Array mit den Werten, an denen die Abstände angepasst werden sollen
     * @param iteratorIndex Iterator der eigentlichen Aufzählung
     */
    private void printWhitespace(String[] array, int iteratorIndex){
        int maxLength = getMaxEntry(array);
        for (int j = 0; j < maxLength - array[iteratorIndex].length() + 2; j++) {
            print(" ");
        }
    }

    /**
     * Wandelt die Attribute von User in ein String Array um
     * @param user der entsprechende User
     * @return Array mit den Attributen von User
     */
    private String[] userParameterToArray(User user){
        String[] params = new String[9];
        params[0] = user.getFirstname();
        params[1] = user.getLastname();
        params[2] = user.getBirthday().toString();
        params[3] = user.getCity();
        params[4] = new String(user.getZipcode() + "");
        params[5] = user.getStreet();
        params[6] = user.getStreetnr();
        params[7] = user.getDepartment().getName();

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
        print(concept.getTitle());
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
        System.out.println(ansi().eraseScreen());
    }

}
