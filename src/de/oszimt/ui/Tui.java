package de.oszimt.ui;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

/**
 * Created by user on 17.09.2014.
 */
public class Tui {

    public Tui(){
        AnsiConsole.systemInstall();
        println(GREEN, "dududu");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clean();
        println(BLUE, "Hallo");
    }

    public void showMainMenu() {

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
