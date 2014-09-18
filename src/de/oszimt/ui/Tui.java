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


    public void showMainMenu() {
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
            print(STANDARD_COLOR, entrys[i]);
            for (int j = 0; j < max - entrys[i].length() + 2; j++) {
                print(STANDARD_COLOR, " ");
            }
            println(STANDARD_COLOR, "(" + (i + 1) + ")" );
        }
        println(STANDARD_COLOR, "");
        print(STANDARD_COLOR, "Menuepunkt eingeben: ");

        byte input = readInput(entrys.length);
        //check, 
        if(input == -1)
            return;
        switch (input){
            case 1:
                    break;
            case 2: break;
            case 3: break;
            case 4: break;
            case 5: break;
            case 6: System.exit(0);

        }
    }

    private void writeHeader(int length){
        println(STANDARD_COLOR, "*****************************************");
        print(STANDARD_COLOR, "*");
        for(int i = 0; i < length; i++)print(BLACK, " ");
        print(STANDARD_COLOR, concept.getTitle());
        for(int i = 0; i < length; i++)print(BLACK, " ");
        println(STANDARD_COLOR, "*");
        println(STANDARD_COLOR, "*****************************************");
    }

    private byte readInput(int length){
        Scanner scan = new Scanner(System.in);
        byte choice = 0;
        try {
            choice = scan.nextByte();
        } catch(InputMismatchException e){
            printErrorMessage(length);
            sleep(2000);
            showMainMenu();
            return -1;
        }
        if(choice > length || choice < 1) {
            printErrorMessage(length);
            sleep(2000);
            showMainMenu();
            return -1;
        }
        return choice;
    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    private void printErrorMessage(int length){
        println(STANDARD_COLOR, "");
        println(RED, "Falsche Eingabe, bitte eine Zahl zwischen 1 und " + length + " eingeben");
    }

    private int getMaxEntry(String[] array){
        int max = 0;
        for (int i = 0; i < array.length; i++) {
            if(array[i].length() > max){
                max = array[i].length();
            }
        }
        return max;
    }

    private void println(Color color, String text){
        System.out.println(ansi().fg(color).a(text).fg(STANDARD_COLOR));
    }

    private void print(Color color, String text){
        System.out.print(ansi().fg(color).a(text).fg(STANDARD_COLOR));
    }

    private void clean(){
        System.out.println(ansi().eraseScreen());
    }

}
