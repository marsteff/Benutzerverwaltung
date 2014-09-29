package de.oszimt.ui;

import de.oszimt.concept.iface.IConcept;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

    /**
     * Instanze Variable zur Haltung des Konzepts
     * @notice muss in diesem Fall statisch sein da
     *         eine FX-Anwendung mittels Class-Reflection immer
     *         eine Instance über den Default-Konstrucktor erzeugt
     *
     *         Wir können jedoch erst den Paramentrisierten Konstrukor
     *         ausrufen und die Konzeptinstance setzten
     */
    private static IConcept concept;

    /**
     * FX Start-Method Läd das GUI mittels Kontroller und FXML Dateien
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        
        //setzen der Stage Variable im Controller um zuverlaessig das Fenster schliessen zu können
        Controller contr = loader.getController();
        contr.setGui(this);
        contr.setStage(primaryStage);

        //Initialisieren des Fensters
        primaryStage.setTitle(this.getConcept().getTitle());
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(300);
        primaryStage.show();

    }

    /**
     * Default Konstructor
     * Wird von FXApplication aufgerufen
     */
    public Gui(){}

    /**
     * Paramentrisierter Konstrukor
     * wird zum setzten der Konzept Instanze benutzt
     *
     * @param concept
     */
    public Gui(IConcept concept){
        this.setConcept(concept);
        this.run();
    }

    /**
     * Starten des GUIs
     */
    public void run() {
        launch();
    }

    /**
     * Gibt die Konzept Instanze zurück
     * @return
     */
    public IConcept getConcept() {
        return concept;
    }

    /**
     * Setzt die Konzept Instanze
     * @param concept
     */
    public void setConcept(IConcept concept) {
        this.concept = concept;
    }
}
