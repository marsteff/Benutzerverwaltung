package de.oszimt;

import javafx.scene.control.TextField;

/**
 * Created by Marci on 29.09.2014.
 */
public class NumberTextField extends TextField {

    @Override
    public void replaceText(int i, int i2, String s) {
        if(s.matches("[0-9]") || s.isEmpty()){
            super.replaceText(i, i2, s);
        }
    }

    @Override
    public void replaceSelection(String s) {
        if(s.matches("[0-9]") || s.isEmpty()) {
            super.replaceSelection(s);
        }
    }
}
