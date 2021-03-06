package de.oszimt;

import de.oszimt.concept.enumeration.ConceptMethod;
import de.oszimt.factory.ConceptFactory;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.ui.impl.gui.Gui;
import de.oszimt.ui.impl.tui.Tui;

import java.util.Arrays;

/**
 * Startet die Anwendung
 */
public class Benutzerverwaltung {
    public static void main(String[] args) {

        /**
         * Die Argumente können in der Runconfig eingetragen werden.
         * Bzw. kann man sich für die verschiedenen Kombinationen Runconfigs anglegen.
         *
         * Run > Edit Configuration > Programm Arguments
         *
         */
        PersistanceMethod PersMeth = Arrays.asList(args).contains("--sqlite") ?
                PersistanceMethod.SQLITE : PersistanceMethod.MONGODB;

        ConceptMethod ConcMeth = ConceptMethod.STANDARD_CONCEPT;

        if(Arrays.asList(args).contains("--tui")){
            new Tui(ConceptFactory.buildConcept(ConcMeth,PersMeth));
        }else{
            new Gui(ConceptFactory.buildConcept(ConcMeth,PersMeth));
        }

    }
}
