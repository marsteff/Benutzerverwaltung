package de.oszimt.util;

import java.time.LocalDate;

/**
 * Created by Marci on 08.10.2014.
 * @see Character Ranges: http://www.csbruce.com/software/utf-8.html
 */
public class Validation {
    public static boolean checkIfLetters(String text) {
        //return text.matches("^[a-zA-Z|Ä-Üä-ü|ß|\\-|é|è|ê|ú|ù|û|í|ì|î|á|à|â|ó|ò|ô]+$");
        return text.matches("^[\\u00c0-\\u01ffa-zA-Z\\-]+$");
    }

    public static boolean checkIfCity(String text) {
        //return text.matches("^[a-zA-Z|Ä-Üä-ü|ß|\\s|\\-|é|è|ê|ú|ù|û|í|ì|î|á|à|â|ó|ò|ô]*");
        return text.matches("^[\\u00c0-\\u01ffa-zA-Z][\\s\\u00c0-\\u01ffa-zA-Z'\\-]*$");
    }

    public static boolean checkIfZipCode(String text) {
        return text.matches("^\\d{5}$");
    }

    public static boolean checkIfStreetnr(String text) {
        return text.matches("^\\d{1,3}[a-zA-Z]{0,2}");
    }

    public static boolean checkIfStreet(String text) {
        //return text.matches("^[\\w|\\s|Ä-Üä-ü|ß|\\-|é|è|ê|ú|ù|û|í|ì|î|á|à|â|ó|ò|ô|\\.]*");
        return text.matches("^[\\u00c0-\\u01ffa-zA-Z][\\s\\.\\u00c0-\\u01ffa-zA-Z'\\-]*$");
    }

    public static boolean checkIfBirthday(LocalDate birthday) {
        return  birthday.isAfter(LocalDate.now().minusYears(14)) ||
                birthday.isBefore(LocalDate.now().minusYears(115));
    }

    public static boolean checkIfDepartment(String text) {
        return text.matches("^[A-Za-z|0-9]*$");

    }
}
