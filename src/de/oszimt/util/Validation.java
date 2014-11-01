package de.oszimt.util;

/**
 * Created by Marci on 08.10.2014.
 */
public class Validation {
    public static boolean checkIfLetters(String text) {
        return text.matches("[a-zA-Z|Ä-Üä-ü|ß|\\-|é|è|ê|ú|ù|û|í|ì|î|á|à|â|ó|ò|ô]*");
    }

    public static boolean checkIfCity(String text) {
        return text.matches("[a-zA-Z|Ä-Üä-ü|ß|\\s|\\-|é|è|ê|ú|ù|û|í|ì|î|á|à|â|ó|ò|ô]*");
    }

    public static boolean checkIfZipCode(String text) {
        return text.matches("[\\d]{5}");
    }

    public static boolean checkIfStreetnr(String text) {
        return text.matches("[0-9]{1,3}[a-zA-Z]{0,2}");
    }

    public static boolean checkIfStreet(String text) {
        return text.matches("[\\w|\\s|Ä-Üä-ü|ß|\\-|é|è|ê|ú|ù|û|í|ì|î|á|à|â|ó|ò|ô|.]*");
    }
}
