package de.oszimt.ui;

import de.oszimt.conzept.impl.Concept;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Created by user on 17.09.2014.
 */
public class Tui {

    private Concept concept;
    public Tui(Concept concept){
        this.concept = concept;

        AnsiConsole.systemInstall();

        showMainMenu();
    }


    public void showMainMenu() {
        writeHeader(3);
    }

    private void writeHeader(int length){
        println(BLACK, "*****************************************");
        print(BLACK, "*");
        for(int i = 0; i < length; i++)print(BLACK, " ");
        print(BLACK, concept.getTitle());
        for(int i = 0; i < length; i++)print(BLACK, " ");
        println(BLACK, "*");
        println(BLACK, "*****************************************");
    }

    private void println(Color color, String text){
        System.out.println(ansi().fg(color).a(text).fg(BLACK));
    }

    private void print(Color color, String text){
        System.out.print(ansi().fg(color).a(text).fg(BLACK));
    }

    private void clean(){
        System.out.println(ansi().eraseScreen());
    }

}
