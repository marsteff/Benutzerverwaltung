package de.oszimt.ui;

import de.oszimt.concept.iface.IConcept;

import de.oszimt.model.User;
import org.fusesource.jansi.AnsiConsole;

import java.util.InputMismatchException;
import java.util.List;
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
        writeHeader(2);
        clean();
        print("Benutzer ID eingeben");
        int id = readInt();

        User user = null;
        try {
            user = concept.getUser(id);
        } catch(Exception e) {
            printErrorMessage("User wurde nicht gefunden");
            showUser();
            return;
        }
        writeUserStats(user);
        String input = null;
        while(true) {
            println("");
            print("nach weiterem Benutzer suchen ? (j/n) ");
            input = readString();
            if (input.length() == 1) {
                break;
            }
        }
        if(input.toLowerCase().equals("j")) {
            showUser();
            return;
        }
        showMainMenu();
    }

    private void editUser() {

    }

    private void deleteUser() {
        writeHeader(2);
        clean();
        print("Benutzer ID eingeben");
        int id = readInt();

        User user = null;
        try {
            user = concept.getUser(id);
        } catch(Exception e) {
            printErrorMessage("User wurde nicht gefunden");
            showUser();
            return;
        }
        writeUserStats(user);
        String input = null;
        while(true) {
            println("");
            print("User wirklich löschen ? (j/n) ");
            input = readString();
            if (input.length() == 1) {
                break;
            }
        }
        if(input.toLowerCase().equals("j")) {
            concept.deleteUser(user);
        }
        showMainMenu();
    }

    private void searchUser() {

    }

    private void showAllUsers() {

    }

    private void writeUserStats(User user){
        String[] entrys = { "Vorname",
                "Nachname",
                "Geburtstag",
                "Stadt",
                "Postleitzahl",
                "Strasse",
                "Strassen-Nummer",
                "Abteilung"};

        String[] params = getUserParameter(user);
        int max = getMaxEntry(entrys);
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            for (int j = 0; j < max - entrys[i].length() + 2; j++) {
                print(" ");
            }
            println(": " + params[i]);
        }
    }

    private String[] getUserParameter(User user){
        String[] params = new String[9];
        params[0] = user.getFirstname();
        params[1] = user.getLastname();
        params[2] = user.getBirthday().toString();
        params[8] = user.getCity();
        params[4] = new String(user.getZipcode() + "");
        params[5] = user.getStreet();
        params[6] = user.getStreetnr();
        params[7] = user.getLastname();
        params[8] = user.getDepartment();

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
     * @param length gibt die Anzahl der Menüpunkte an
     * @return -1 im Fehlerfall, ansonsten die eingegebene Zahl
     */
    private byte readInput(int length){
        Scanner scan = new Scanner(System.in);
        byte choice = 0;
        try {
            choice = scan.nextByte();
        } catch(InputMismatchException e){
            return -1;
        }
//        if(choice > length || choice < 1) {
//            printWrongEntryErrorMessage(length);
//            sleep(2000);
//            showMainMenu();
//            return -1;
//        }
        return choice;
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
