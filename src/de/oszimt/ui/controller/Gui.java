package de.oszimt.ui.controller;

import de.oszimt.conzept.impl.Concept;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {


    private Concept concept;

    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        
        //setzen der Stage Variable im Controller um zuverl�ssigdas Fenster schliessen zu k�nnen
        Controller contr = loader.getController();
        contr.setStage(primaryStage);
        contr.setGui(this);
        
        //Initialisieren des Fensters
        primaryStage.setTitle(this.concept.getTitle());
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(300);
        primaryStage.show();

    }

    public Gui(){

    }

    public Gui(Concept concept){
        this.concept = concept;
        this.run();
    }

    public void run() {
        launch();
    }

    public Concept getConcept() {
        return concept;
    }

    public void setConcept(Concept concept) {
        this.concept = concept;
    }

}
