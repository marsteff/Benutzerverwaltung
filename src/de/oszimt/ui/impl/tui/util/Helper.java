package de.oszimt.ui.impl.tui.util;

import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Created by m588 on 24.10.2014.
 */
public class Helper {
    /**
     * Löscht den Inhalt der Kommandozeile
     */
    public static void clean() {
        //System.out.println(ansi().eraseScreen());
        System.out.print("\033[2J\033[;H");
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

    private static String repeat(String ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }


    private static String repeat(char ch, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    private static String stripAnsiColor(String str) {
        return str.replaceAll("\u001B\\[[;\\d]*m", "");
    }
}
