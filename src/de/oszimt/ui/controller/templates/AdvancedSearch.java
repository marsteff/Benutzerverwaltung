package de.oszimt.ui.controller.templates;

import de.oszimt.model.Kunde;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class AdvancedSearch extends StackPane{
	@FXML
	TextField vornameField;
	@FXML
	TextField nachnameField;
	@FXML
	TextField plzField;
	@FXML
	TextField ortField;
	@FXML
	TextField strasseField;
	@FXML
	TextField strassenNummerField;
	@FXML
	DatePicker geburtstagField;
	
	TableView<Kunde> customerTable;
    ObservableList<Kunde> originalList;
	
	public AdvancedSearch(TableView<Kunde> customerTable){
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("advancedSearch.fxml"));
		loader.setRoot(this);
		loader.setController(this);

		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Fehler beim Laden der FXML");
		}
		
		//Breiten Binding der TextFields an die Breite des Fensters
		widthBindingOfControls();
		
		this.customerTable = customerTable;
		
		//sichern der Originalen Liste, da diese f�r sp�tere Bearbeitungen ben�tigt wird
        originalList = customerTable.getItems();
        
        //initialisieren der ChangeListener der einzelnen Felder
		alternativeSearch();

	}
	
	public TextField getVornameField() {
		return vornameField;
	}

	public TextField getNachnameField() {
		return nachnameField;
	}

	public TextField getPlzField() {
		return plzField;
	}

	public TextField getOrtField() {
		return ortField;
	}

	public TextField getStrasseField() {
		return strasseField;
	}

	public TextField getStrassenNummerField() {
		return strassenNummerField;
	}

	public DatePicker getGeburtstagField() {
		return geburtstagField;
	}

	/**
	 * Setzt das Breitenbinding der einzelnen Felder fest
	 */
	private void widthBindingOfControls(){
		vornameField.prefWidthProperty().bind(this.widthProperty().multiply(0.14));
		nachnameField.prefWidthProperty().bind(this.widthProperty().multiply(0.14));
		geburtstagField.prefWidthProperty().bind(this.widthProperty().multiply(0.14));
		
		ortField.prefWidthProperty().bind(this.widthProperty().multiply(0.14));
		plzField.prefWidthProperty().bind(this.widthProperty().multiply(0.14));
		strasseField.prefWidthProperty().bind(this.widthProperty().multiply(0.14));
		strassenNummerField.prefWidthProperty().bind(this.widthProperty().multiply(0.14));
	}
	
	
	/**
	 * setzen der ChangeListener der einzelnen Text Propertys
	 */
	private void alternativeSearch(){
		
		vornameField.textProperty().addListener((observableValue, oldValue, newValue) -> {
			customerTable.setItems(searchInTable());
		});
		
		nachnameField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            customerTable.setItems(searchInTable());
		});
		
		ortField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            customerTable.setItems(searchInTable());
		});
		
		strasseField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            customerTable.setItems(searchInTable());
		});
		
		strassenNummerField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            customerTable.setItems(searchInTable());
		});
		
		plzField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            customerTable.setItems(searchInTable());
		});
		
		geburtstagField.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            customerTable.setItems(searchInTable());
		});
		
	}

	
	/**
	 * Filtert die Kundenliste nach den jeweiligen Kriterien
	 * @return gefilterte ObservableList
	 */
    private ObservableList<Kunde> searchInTable(){

        ObservableList<Kunde> returnList = originalList;

        List<Kunde> list = returnList.parallelStream()
                                    .filter(kunde -> kunde.getVorname().toLowerCase().contains(vornameField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getNachname().toLowerCase().contains(nachnameField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getOrt().toLowerCase().contains(ortField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getStrasse().toLowerCase().contains(strasseField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getStrassenNummer().toLowerCase().contains(strassenNummerField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getVorname().toLowerCase().contains(vornameField.getText().toLowerCase()))
                                    .filter(kunde ->{
                                        if(geburtstagField.getValue() != null)
                                            return kunde.getGeburtstag().toString().toLowerCase().contains(geburtstagField.getValue().toString().toLowerCase());
                                        else
                                            return true;
                                    })
                                    .filter(kunde -> Integer.toString(kunde.getPlz()).toLowerCase().indexOf(plzField.getText()) != -1)
                                    .collect(Collectors.<Kunde>toList());

        return FXCollections.observableArrayList(list);
    }
	
}
