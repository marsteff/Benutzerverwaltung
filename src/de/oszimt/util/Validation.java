package de.oszimt.util;

import java.util.regex.Pattern;

/**
 * Created by Marci on 08.10.2014.
 */
public class Validation {
    public static boolean checkIfOnlyLetters(String text) {
        return text.matches("[a-zA-Z]");
    }
}
