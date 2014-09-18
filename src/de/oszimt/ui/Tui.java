package de.oszimt.ui;

import de.oszimt.conzept.impl.Concept;

import org.fusesource.jansi.AnsiConsole;

import java.util.InputMismatchException;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Created by user on 17.09.2014.
 */
public class Tui {

    private final Color STANDARD_COLOR = WHITE;
    private Concept concept;
    public Tui(Concept concept){
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
        for (int i = 0; i < entrys.length; i++) {
            print(entrys[i]);
            for (int j = 0; j < max - entrys[i].length() + 2; j++) {
                print(" ");
            }
            println("(" + (i + 1) + ")" );
        }
        println("");
        print("Menuepunkt eingeben: ");

        byte input = readInput(entrys.length);
        //check,
        if(input == -1)
            return;
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

    }

    private void editUser() {

    }

    private void deleteUser() {

    }

    private void searchUser() {

    }

    private void showAllUsers() {

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
            printWrongEntryErrorMessage(length);
            sleep(2000);
            showMainMenu();
            return -1;
        }
        if(choice > length || choice < 1) {
            printWrongEntryErrorMessage(length);
            sleep(2000);
            showMainMenu();
            return -1;
        }
        return choice;
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
        System.out.println(ansi().fg(STANDARD_COLOR).a(text).fg(STANDARD_COLOR));
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
        System.out.println(ansi().fg(STANDARD_COLOR).a(text).fg(STANDARD_COLOR));
    }

    /**
     * Löscht den Inhalt der Kommandozeile
     */
    private void clean(){
        System.out.println(ansi().eraseScreen());
    }

}
