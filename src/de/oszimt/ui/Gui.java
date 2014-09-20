package de.oszimt.ui;

import de.oszimt.concept.iface.IConcept;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {

    private static IConcept concept;

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

    public Gui(){

    }

    public Gui(IConcept concept){
        this.setConcept(concept);
        this.run();
    }

    public void run() {
        launch();
    }

    public IConcept getConcept() {
        return concept;
    }

    public void setConcept(IConcept concept) {
        this.concept = concept;
    }

}
