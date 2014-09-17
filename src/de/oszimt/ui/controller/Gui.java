package de.oszimt.ui.controller;

import de.oszimt.ui.controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.*;

public class Gui extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        
        //setzen der Stage Variable im Controller um zuverl�ssigdas Fenster schliessen zu k�nnen
        Controller contr = loader.getController();
        contr.setStage(primaryStage);
        
        //Initialisieren des Fensters
        primaryStage.setTitle("Oszimt Projekt Benutzerverwaltung");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(650);
        primaryStage.setMinHeight(300);
        primaryStage.show();

    }

    public Gui(){

    }

    public Gui(String conzept){
        this.run();
    }

    public void run() {
        launch();
    }
}
