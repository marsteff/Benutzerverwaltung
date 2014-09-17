package de.oszimt.ui;

import java.time.LocalDate;

import de.oszimt.model.User;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import de.oszimt.ui.templates.AdvancedSearch;
import de.oszimt.factory.PersistanceFactory;
import de.oszimt.persistence.enumeration.PersistanceMethod;
import de.oszimt.persistence.iface.IPersistance;
import de.oszimt.util.RestService;
import de.oszimt.util.Util;

public class Controller {
	
	@FXML
	private TableView<User> customerTable;
	
	@FXML 
	private TableColumn<User, String> vornameColumn;
	@FXML 
	private TableColumn<User, String> nachnameColumn;
	@FXML 
	private TableColumn<User, String> ortColumn;
	@FXML 
	private TableColumn<User, String> strasseColumn;
	@FXML 
	private TableColumn<User, String> strassenNummerColumn;
	@FXML 
	private TableColumn<User, LocalDate> geburtstagColumn;
	@FXML 
	private TableColumn<User, Integer> plzColumn;
	
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
	private Button changeButton;
	@FXML
	private Button abortButton;
	
	@FXML
	private TextField searchField;
	@FXML
	private VBox searchVBox;
	
	@FXML
	private Label informationLabel;
	
	@FXML
	private CheckMenuItem restServiceContextMenu;
	@FXML
	private CheckMenuItem restServiceMainMenu;
	
	private boolean isNewSelection = false;
	private boolean isAdvancedSearch = false;
	
	private AdvancedSearch advancedSearcher;
	
	private IPersistance db;
	
	private Stage stage;
	
	private RestService service;
	private boolean serviceTown = false;
	private boolean writeError = false;
	
	@FXML
	public void initialize(){
		//Initialisierung der DB-Schnittstelle
		db = PersistanceFactory.buildPersistance(PersistanceMethod.SQLITE);
				
		//setzt die Bindings der einzelnen Controls - unter anderem f�r eine Full-Responsive GUI zust�ndig
		bindingOfControls();
		
		//bei klicken auf einen Nutzer Felder mit Daten fuellen
		fillControls();
		
		//Tabelle mit Daten fuellen und Livesuche erm�glichen
		searchInTable();
		
		//
		setListenerForControls();
		
		//service = new RestService();
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	@FXML
	private void abortButtonAction(){
		vornameField.setText("");
		nachnameField.setText("");
		geburtstagField.setValue(null);
		ortField.setText("");
		plzField.setText("");
		strasseField.setText("");
		strassenNummerField.setText("");
		
		changeButton.setDisable(true);
		customerTable.getSelectionModel().clearSelection();
		vornameField.requestFocus();
	}
	
	@FXML
	private void advancedSearch(){
		if(!isAdvancedSearch){
			searchField.setDisable(true);
			searchField.setText("");
			advancedSearcher = new AdvancedSearch(customerTable);
			searchVBox.getChildren().add(advancedSearcher);
			isAdvancedSearch = true;
		}
		else{
			searchField.setDisable(false);
			searchVBox.getChildren().remove(searchVBox.getChildren().size()-1);
			isAdvancedSearch = false;
			this.advancedSearcher = null;
			searchInTable();
		}
	}
	
	@FXML
	private void deleteCustomer(){
		User customer = customerTable.getSelectionModel().getSelectedItem();
        db.deleteUser(customer);
		
		searchInTable();
	}
	
	@FXML
	private void deleteCustomerButton(){
		
		ObservableList<User> testList = customerTable.getItems();
		testList.forEach(e -> {
			if(e.getVorname() == null)
				System.err.println(e + " ist fehlerhaft");
			else
				System.out.println(e + " weist keine Fehler auf");
		});
	}
	
	@FXML
	private void changeButtonAction(){
		//get index of actual selection to make double chnage possible
		int index = customerTable.getSelectionModel().getSelectedIndex();
		
		//get content of fields to make new customer
		String vorname = vornameField.getText();
		String nachname = nachnameField.getText();
		String ort = ortField.getText();
		LocalDate geburtstag = geburtstagField.getValue();
		String strasse = strasseField.getText();
		String strassenNummer = strassenNummerField.getText();
		int plz = Integer.parseInt(plzField.getText());
		
		User newCustomer = new User(vorname, nachname, geburtstag, ort, strasse, strassenNummer, plz);
		
		//to specify if is it a new user or only a change
		boolean isNew = false;
		
		if(!customerTable.getSelectionModel().isEmpty()){
			newCustomer.setId(customerTable.getSelectionModel().getSelectedItem().getId());
			isNew = true;
		}
		
		db.updateUser(newCustomer);
		
		//create a nice Message of Action
		setSuccedMessage(newCustomer + " erfolgreich" + (isNew ?  " bearbeitet" : " erfolgreich als User hinzugef�gt"));
		
		searchInTable();
		
		changeButton.setDisable(true);
		customerTable.getSelectionModel().select(index);
	}
	
	@FXML
	private void createRandomCustomersAction(){
		IPersistance db = PersistanceFactory.buildPersistance(PersistanceMethod.SQLITE);
    	
    	ObservableList<User> customerList = Util.createCustomers();
    	for(User user : customerList){
    		db.createUser(user);
    	}
    	searchInTable();
    	setSuccedMessage("Zufallsnutzer erfolgreich erstellt!");
	}
	
    @FXML
    private void deleteAllCustomersAction(){
    	ObservableList<User> list = db.getAllKunden();
    	list.forEach(e -> db.deleteUser(e));
    	searchInTable();
    }
    
    @FXML
    private void newCustomerAction() {
        abortButtonAction();
        vornameField.requestFocus();
    }

    @FXML
    private void closeAction(ActionEvent event) {
    	stage.close();
    }

	/**
	 * Fuellt die Textfelder beim klick auf einen Kunden in der TableView mit den entsprechenden Werten
	 */
	private void fillControls() {
		customerTable.getSelectionModel().selectedItemProperty().addListener((obsValue, oldValue, newValue) -> {
			if(newValue != null){
				changeButton.setDisable(true);
				isNewSelection = true;
				vornameField.setText(newValue.getVorname());
				nachnameField.setText(newValue.getNachname());
				ortField.setText(newValue.getOrt());
				plzField.setText(new String("" + newValue.getPlz()));
				
				strasseField.setText(newValue.getStrasse());
				strassenNummerField.setText(newValue.getStrassenNummer());
				geburtstagField.setValue(newValue.getGeburtstag());
				isNewSelection = false;
				serviceTown = false;
			}
		});
	}
	
	/**
	 * Ermoeglicht das Suchen in der TableView.
	 * Dieses erfolgt Live.
	 */
	private void searchInTable() {
		ObservableList<User> masterDate = db.getAllKunden();

		FilteredList<User> filteredDate = new FilteredList<>(masterDate, p -> true);

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredDate.setPredicate(kunde -> {
				if(newValue == null || newValue.isEmpty())
					return true;

				String lowerCaseFilter = newValue.toLowerCase();

				if(kunde.getVorname().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(kunde.getNachname().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(kunde.getOrt().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(kunde.getStrasse().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(kunde.getStrassenNummer().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(Integer.toString(kunde.getPlz()).toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(kunde.getGeburtstag().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;

				return false;
			});
		});

		SortedList<User> sortedDate = new SortedList<>(filteredDate);
		sortedDate.comparatorProperty().bind(customerTable.comparatorProperty());

		customerTable.setItems(filteredDate);
	}
	
	/**
	 * Bindet die Breite der Controls an die Breite des Fensters, zwecks Responsive-GUI
	 */
	private void bindingOfControls() {
		//Breiten Binding der Columns an die Breite des Fensters
				vornameColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.14));
				nachnameColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.14));
				ortColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.14));
				strasseColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.14));
				strassenNummerColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.14));
				geburtstagColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.14));
				plzColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.14));
				
				//Breiten Binding der TextFields an die Breite des Fensters
				vornameField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				nachnameField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				geburtstagField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				
				ortField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				plzField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
				strasseField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				strassenNummerField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
				
				//Breiten Binding der Buttons an die Breite des Fensters
				changeButton.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				abortButton.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
				
				//Breiten Binding der Suchleiste an die Breite des Fensters
				searchField.prefWidthProperty().bind(customerTable.widthProperty().multiply(1));
				
				//Binding der CheckMenuItems f�r den RestService
				restServiceContextMenu.selectedProperty().bindBidirectional(restServiceMainMenu.selectedProperty());
	}
	
	
	/**
	 * Set a Listener for the TextField Controls
	 */
	private void setListenerForControls(){
		//Definierung eines textListener f�r die jeweiligen TextFelder,  
		//ausser PLZ-Feld, da dieses gesondert behandelt werden muss zwecks RestService
		ChangeListener<String> textListener =(observable, oldValue, newValue) -> {
			
			//serviceTown auf Standardwert zur�cksetzen
			serviceTown = false;
				
			//Validierung der L�nge und gegebenfalls Meldung l�schen
			if(newValue.length() < 28){
				writeError = false;
				cleanInformationLabel();
			}
				
			//wenn Input l�nger als 28 Zeichen, alten Wert setzen und Fehler melden
			else if(newValue.length() > 28){
				writeError = true;
				((StringProperty) observable).setValue(oldValue);
			}
				
			//Ausgabe der Fehlermeldung
			if(writeError){
				setErrorMessage("Eingabe darf nicht l�nger als 30 Zeichen lang sein");
			}
				
			//Pr�fen, ob der �bernehmenButton aktiviert werden darf
			activateChangeButton(oldValue, newValue, "");
		};
		
		ChangeListener<String> plzListener = (observable, oldValue, newValue) -> {
				
			//Validierung der L�nge und gegebenfalls Meldung l�schen
			if(newValue.length() < 5){
				writeError = false;
				cleanInformationLabel();
			}
			
			//wenn Input l�nger als 5 Zeichen, alten Wert setzen und Fehler melden
			else if(newValue.length() > 5){
				writeError = true;
				((StringProperty) observable).setValue(oldValue);
			}
				
			//Wenn RestService benutzt wird, soll der Ort beim l�schen der 5 Stelle der PLZ auch der Ort gel�scht werden
			if(restServiceMainMenu.isSelected() ||serviceTown && newValue.length() == 4 && oldValue.length() == 5){
				ortField.setText("");
				serviceTown = false;
			}

			//Wenn RestService benutzt wird und 5 PLZ-Zahlen eingegeben wurden, soll nach dem jeweiligen Ort gesucht und gesetzt werden
			//Wenn keiner gefunden wird, Fehlermeldung ausgeben
			if(restServiceMainMenu.isSelected()){
				if(newValue.length() == 5){
					String town = service.getTown(plzField.getText());
					if(!town.equals(""))
						ortField.setText(town);
					else
						setErrorMessage("Diese PLZ ist uns leider nicht bekannt");
					serviceTown = true;
				}
			}
			
			//Pr�fen, ob der �bernehmenButton aktiviert werden darf
			activateChangeButton(oldValue, newValue, "");
		};
		
		ChangeListener<LocalDate> dateListener = (observable, oldValue, newValue) -> {
			if(oldValue != null)	
				//Pr�fen, ob der �bernehmenButton aktiviert werden darf
				activateChangeButton(oldValue, newValue, null);
		};
		
		//setzen der ebend definierten Listener
		vornameField.textProperty().addListener(textListener);
		vornameField.textProperty().addListener(textListener);
		nachnameField.textProperty().addListener(textListener);
		ortField.textProperty().addListener(textListener);
		strasseField.textProperty().addListener(textListener);
		strassenNummerField.textProperty().addListener(textListener);
		plzField.textProperty().addListener(plzListener);
		geburtstagField.valueProperty().addListener(dateListener);
	}
	
	/**
	 * Prueft, ob alle TextFields + DatePicker gefuellt sind
	 * @return true, wenn alle TextFields + DatePicker gefuellt sind, ansonsten false
	 */
	private boolean isAllFieldsFillt(){
		if(		vornameField.getText().equals("") ||
				nachnameField.getText().equals("") ||
				ortField.getText().equals("") ||
				strasseField.getText().equals("") ||
				strassenNummerField.getText().equals("") ||
				plzField.getText().equals("") ||
				geburtstagField.getValue() == null
			) 
			return false;
		
		return true;
	}
	
	/**
	 * Pr�ft, ob der �bernehmeButton aktiviert werden darf
	 * Kriterien: Kein Control darf leer sein, der alte Inhalt darf nicht dem neuen gleich sein 
	 * und es darf keine neue Selektion sein
	 * @param oldValue 	- alter Wert eines Controls
	 * @param newValue 	- neuer Wert eines Controls
	 * @param value		- wogegen gepr�ft wird (in der regel null, oder bei Strings "")
	 */
	private <T> void activateChangeButton(T oldValue, T newValue, String value){
		changeButton.setDisable(true);
		if(		isAllFieldsFillt() &&  
				!newValue.equals(value) && 
				!oldValue.equals(newValue) && 
				!isNewSelection)
		
			changeButton.setDisable(false);
		
	}
 
	/**
	 * setzt eine entsprechende Fehlermeldung
	 * @param message
	 */
    private void setErrorMessage(String message){
    	informationLabel.setTextFill(Color.RED);
    	informationLabel.setText(message);
    }
    
    /**
     * setzt entsprechende Erfolgsmeldung
     * @param message
     */
    private void setSuccedMessage(String message){
    	informationLabel.setTextFill(Color.GREEN);
    	informationLabel.setText(message);
    }
    
    /**
     * s�ubert das Nachrichtenfeld
     */
    private void cleanInformationLabel(){
    	informationLabel.setText("");
    }
    
}
