package de.oszimt.ui;

import de.oszimt.conzept.impl.Concept;
import de.oszimt.factory.PersistanceFactory;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Gui extends Application {


    private static Concept concept;

    @Override
    public void start(Stage primaryStage) throws Exception{
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        
        //setzen der Stage Variable im Controller um zuverlässigdas Fenster schliessen zu können
        Controller contr = loader.getController();
        contr.setGui(this);
        contr.initSearchInTable();
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

    public Gui(Concept concept){
        this.setConcept(concept);
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
