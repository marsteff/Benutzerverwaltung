package de.oszimt.ui.templates;

import de.oszimt.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AdvancedSearch extends StackPane{

    @FXML
	private TextField vornameField;
	@FXML
	private TextField nachnameField;
	@FXML
	private TextField plzField;
	@FXML
    private TextField ortField;
	@FXML
    private TextField strasseField;
	@FXML
    private TextField strassenNummerField;
	@FXML
    private DatePicker geburtstagField;
    @FXML
    private TextField departmentField;
	
	TableView<User> customerTable;
    ObservableList<User> originalList;

    public void setOriginalList(ObservableList<User> userList) {
        this.originalList = userList;
    }

    public AdvancedSearch(TableView<User> customerTable){
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
		
		//sichern der Originalen Liste, da diese fuer spaetere Bearbeitungen benoetigt wird
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
		vornameField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
		nachnameField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
		geburtstagField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
		
		ortField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
		plzField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
		strasseField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
		strassenNummerField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
        departmentField.prefWidthProperty().bind(this.widthProperty().multiply(0.125));
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

        departmentField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            customerTable.setItems(searchInTable());
        });
		
	}

	
	/**
	 * Filtert die Kundenliste nach den jeweiligen Kriterien
	 * @return gefilterte ObservableList
	 */
    private ObservableList<User> searchInTable(){

        ObservableList<User> returnList = originalList;

        List<User> list = returnList.parallelStream()
                                    .filter(kunde -> kunde.getFirstname().toLowerCase().contains(vornameField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getLastname().toLowerCase().contains(nachnameField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getCity().toLowerCase().contains(ortField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getStreet().toLowerCase().contains(strasseField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getStreetnr().toLowerCase().contains(strassenNummerField.getText().toLowerCase()))
                                    .filter(kunde -> kunde.getDepartment().getName().toLowerCase().contains(departmentField.getText().toLowerCase()))
                                    .filter(kunde ->{
                                        if(geburtstagField.getValue() != null)
                                            return kunde.getBirthday().toString().toLowerCase().contains(geburtstagField.getValue().toString().toLowerCase());
                                        else
                                            return true;
                                    })
                                    .filter(kunde -> Integer.toString(kunde.getZipcode()).toLowerCase().indexOf(plzField.getText()) != -1)
                                    .collect(Collectors.<User>toList());

        return FXCollections.observableArrayList(list);
    }
	
}
