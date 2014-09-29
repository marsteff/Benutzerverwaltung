package de.oszimt.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.util.Util;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import de.oszimt.ui.templates.AdvancedSearch;
import de.oszimt.util.RestService;

/**
 * Kontroller für das GUI
 *
 * Ganz nach JavaFX Art gibt es auch hier einen
 * Kontroller um Events etc. des GUIs zu steuern
 */
public class Controller {

    /*
        Definieren der Kontrollelemente
     */
    @FXML
    public StackPane rootPane;
    @FXML
    public BorderPane mainPane;

    @FXML
	private TableView<User> customerTable;
	
	@FXML 
	private TableColumn<User, String> firstnmaeColumn;
	@FXML 
	private TableColumn<User, String> lastnameColumn;
	@FXML 
	private TableColumn<User, String> cityColumn;
	@FXML 
	private TableColumn<User, String> streetColumn;
	@FXML 
	private TableColumn<User, String> streetNrColumn;
	@FXML 
	private TableColumn<User, LocalDate> birthdayColumn;
	@FXML 
	private TableColumn<User, Integer> zipcodeColumn;
    @FXML
    public TableColumn<User, Department> departmentColumn;
	
	@FXML
	private TextField firstnameField;
	@FXML
	private TextField lastnameField;
	@FXML
	private TextField zipCodeField;
	@FXML
	private TextField cityField;
	@FXML
	private TextField streetField;
	@FXML
	private TextField streetNrField;
	@FXML
	private DatePicker birthdayField;
    @FXML
    public ComboBox<Department> departmentComboBox;
	
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

    /*
        Schaltenvariablen um bestimmte Funktionalitäten
        umzusetzten
     */
	private boolean isNewSelection = false;
	private boolean isAdvancedSearch = false;

    /**
     * Hält eine Instanze des Kontrollers für die
     * Erweiterte Suche
     */
	private AdvancedSearch advancedSearcher;

    /**
     * Hält eine Instance des GUI Objects
     */
    private Gui gui;

    /**
     * Hält eine Instance der Stage
     */
    private Stage stage;

    /**
     * Hält eine Instance des REST Services (wenn benutzt)
     */
	private RestService service;
    /**
     * Schaltervariablen um den REST Service zu steuern
     */
	private boolean serviceTown = false;
	private boolean writeError = false;

    StackPane glass = null;

    /**
     * Initialisieren einzelner Kontrollelemente
     */
	@FXML
	public void initialize(){
		//setzt die Bindings der einzelnen Controls - unter anderem für eine Full-Responsive GUI zuständig
		bindingOfControls();
		
		//bei klicken auf einen Nutzer Felder mit Daten fuellen
		fillControls();

		//setzt die Listener der Controls
		setListenerForControls();

        //TODO das muss doch eigentlich bei dem aktivieren des RestServices passieren
//		service = new RestService();

        //Hiermit ist es möglich, nach der Initialierung Code auszuführen. Notwendig, um in der initialize Methode auf das GUI Objekt zugreifen zu können
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Initialisiert die ComboBox
                setDepartmentComboBox();

                //Tabelle mit Daten fuellen und Livesuche ermöglichen
                searchInTable();
            }
        });
	}

    /**
     * Füllt das Dropdown für die Abteilungen
     */
    private void setDepartmentComboBox() {
        //setzen der Departments in die ComboBox
        List<Department> departmentList = getGui().getConcept().getAllDepartments();
        if(departmentList.size() == 0){
            Util.createDepartments(getGui().getConcept());
            departmentList = getGui().getConcept().getAllDepartments();
        }
        departmentComboBox.getItems().addAll(departmentList);
    }

    /**
     * Setzt die Stage
     *
     * @param stage
     */
	public void setStage(Stage stage){
		this.stage = stage;
	}

    /**
     * Ausgangswerte initialisieren
     */
	@FXML
	private void abortButtonAction(){
		firstnameField.setText("");
		lastnameField.setText("");
		birthdayField.setValue(null);
		cityField.setText("");
		zipCodeField.setText("");
		streetField.setText("");
		streetNrField.setText("");
        departmentComboBox.setValue(null);
		changeButton.setDisable(true);
		customerTable.getSelectionModel().clearSelection();
		firstnameField.requestFocus();
	}

    /**
     * Toggle der Erweiterten Suche
     */
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

    /**
     * Löschen eines Benutzers (aus Tabelle und Datenhaltung)
     *
     * Wird als onAction in main.fxml benutzt
     */
	@FXML
	private void deleteCustomer(){
		User user = customerTable.getSelectionModel().getSelectedItem();
        this.gui.getConcept().deleteUser(user);
		searchInTable();
	}

    /**
     * Ändert einen Benutzer
     *
     * wird als onAction in main.fxml benutzt
     */
	@FXML
	private void changeButtonAction(){
		//index der Tabellenauswahl
		int index = customerTable.getSelectionModel().getSelectedIndex();
		
		//Tabellen Zeile zu Benutzer Objekt
		String firstname = firstnameField.getText();
		String lastname = lastnameField.getText();
		String city = cityField.getText();
		LocalDate bday = birthdayField.getValue();
		String street = streetField.getText();
		String streetNr = streetNrField.getText();
		int zipcode = Integer.parseInt(zipCodeField.getText());
        Department department = departmentComboBox.getValue();

        User newUser = new User(firstname, lastname, bday, city, street, streetNr, zipcode,new Department(department.getId(), department.getName()));
		
		//Neuer Benutzer oder Änderung?
		boolean isNew = false;
		
        if(!customerTable.getSelectionModel().isEmpty()){
            int id = customerTable.getSelectionModel().getSelectedItem().getId();
            newUser.setId(id);
            isNew = true;
        }

        this.gui.getConcept().upsertUser(newUser);
		
		//Benachrichtigung setzen
		this.setSuccedMessage(newUser + " erfolgreich" + (isNew ?  " bearbeitet" : " als User hinzugefügt"));
		
		this.searchInTable();
		
		changeButton.setDisable(true);
		customerTable.getSelectionModel().select(index);
	}

    /**
     * Erstellt zufällige Benutzer
     *
     * Wrid als onAction in main.fxml benutzt
     */
	@FXML
	private void createRandomCustomersAction(){

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                gui.getConcept().createRandomUsers(restServiceMainMenu.isSelected());
                return null;
            }
        };
        new Thread(task).start();
        StackPane glass = callProgress();
        task.setOnSucceeded(e -> {
            rootPane.getChildren().remove(glass);
            searchInTable();
            setSuccedMessage("Zufallsnutzer erfolgreich erstellt!");
        });
	}

    @FXML
    private void deleteAllCustomersAction(){

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<User> list = FXCollections.observableList(
                        gui.getConcept().getAllUser()
                );
                list.forEach(e -> gui.getConcept().deleteUser(e));
                return null;
            }
        };
        new Thread(task).start();
        StackPane glass = callProgress();
        task.setOnSucceeded(e -> {
            rootPane.getChildren().remove(glass);
            searchInTable();
            setSuccedMessage("Alle Nutzer erfolgreich gelöscht");
        });
    }

    @FXML
    private void newCustomerAction() {
        this.abortButtonAction();
        this.firstnameField.requestFocus();
    }

    @FXML
    private void closeAction(ActionEvent event) {
    	stage.close();
    }

    public Gui getGui() {
        return gui;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }


    /**
	 * Fuellt die Textfelder beim klick auf einen Kunden in der TableView mit den entsprechenden Werten
	 */
	private void fillControls() {
		customerTable.getSelectionModel().selectedItemProperty().addListener((obsValue, oldValue, newValue) -> {
			if(newValue != null){
				changeButton.setDisable(true);
				isNewSelection = true;
				firstnameField.setText(newValue.getFirstname());
				lastnameField.setText(newValue.getLastname());
				cityField.setText(newValue.getCity());
				zipCodeField.setText(new String("" + newValue.getZipcode()));
				
				streetField.setText(newValue.getStreet());
				streetNrField.setText(newValue.getStreetnr());
				birthdayField.setValue(newValue.getBirthday());
                departmentComboBox.setValue(newValue.getDepartment());

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
        List<User> ulist = this.gui.getConcept().getAllUser();

        if(ulist == null){
            return;
        }

        //kapsel user liste zum observieren
		ObservableList<User> masterDate = FXCollections.observableList(ulist);

		FilteredList<User> filteredDate = new FilteredList<>(masterDate, p -> true);

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredDate.setPredicate(user -> {
				if(newValue == null || newValue.isEmpty())
					return true;

				String lowerCaseFilter = newValue.toLowerCase();

				if(user.getFirstname().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(user.getLastname().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(user.getCity().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(user.getStreet().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(user.getStreetnr().toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(Integer.toString(user.getZipcode()).toLowerCase().indexOf(lowerCaseFilter) != -1)
					return true;
				else if(user.getBirthday().toString().toLowerCase().indexOf(lowerCaseFilter) != -1)
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
				firstnmaeColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));
				lastnameColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));
				cityColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));
				streetColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));
				streetNrColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));
				birthdayColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));
				zipcodeColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));
                departmentColumn.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.122));

				
				//Breiten Binding der TextFields an die Breite des Fensters
				firstnameField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				lastnameField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				birthdayField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
                departmentComboBox.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				
				cityField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				zipCodeField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
				streetField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				streetNrField.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
				
				//Breiten Binding der Buttons an die Breite des Fensters
				changeButton.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.25));
				abortButton.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
				
				//Breiten Binding der Suchleiste an die Breite des Fensters
				searchField.prefWidthProperty().bind(customerTable.widthProperty().multiply(1));
				
				//Binding der CheckMenuItems für den RestService
				restServiceContextMenu.selectedProperty().bindBidirectional(restServiceMainMenu.selectedProperty());
	}
	
	
	/**
	 * Set a Listener for the TextField Controls
	 */
	private void setListenerForControls(){
		//Definierung eines textListener für die jeweiligen TextFelder,
		//ausser PLZ-Feld, da dieses gesondert behandelt werden muss zwecks RestService
		ChangeListener<String> textListener =(observable, oldValue, newValue) -> {
			
			//serviceTown auf Standardwert zurücksetzen
			serviceTown = false;
				
			//Validierung der Länge und gegebenfalls Meldung löschen
			if(newValue.length() < 28){
				writeError = false;
				cleanInformationLabel();
			}
				
			//wenn Input länger als 28 Zeichen, alten Wert setzen und Fehler melden
			else if(newValue.length() > 28){
				writeError = true;
				((StringProperty) observable).setValue(oldValue);
			}
				
			//Ausgabe der Fehlermeldung
			if(writeError){
				setErrorMessage("Eingabe darf nicht länger als 30 Zeichen lang sein");
			}
				
			//Prüfen, ob der ÜbernehmenButton aktiviert werden darf
			activateChangeButton(oldValue, newValue, "");
		};
		
		ChangeListener<String> plzListener = (observable, oldValue, newValue) -> {
				
			//Validierung der Länge und gegebenfalls Meldung löschen
			if(newValue.length() < 5){
				writeError = false;
				cleanInformationLabel();
			}
			
			//wenn Input lönger als 5 Zeichen, alten Wert setzen und Fehler melden
			else if(newValue.length() > 5){
				writeError = true;
				((StringProperty) observable).setValue(oldValue);
			}

//            Pattern pattern = ;

				
			//Wenn RestService benutzt wird, soll der Ort beim löschen der 5 Stelle der PLZ auch der Ort geloescht werden
			if(restServiceMainMenu.isSelected() || serviceTown && newValue.length() == 4 && oldValue.length() == 5){
				cityField.setText("");
				serviceTown = false;
			}

			//Wenn RestService benutzt wird und 5 PLZ-Zahlen eingegeben wurden, soll nach dem jeweiligen Ort gesucht und gesetzt werden
			//Wenn keiner gefunden wird, Fehlermeldung ausgeben
			if(restServiceMainMenu.isSelected()){
				if(newValue.length() == 5){
					String town = service.getTown(zipCodeField.getText());
					if(!town.equals(""))
						cityField.setText(town);
					else
						setErrorMessage("Diese PLZ ist uns leider nicht bekannt");
					serviceTown = true;
				}
			}

            //Ausgabe der Fehlermeldung
            if(writeError){
                setErrorMessage("Eingabe darf nicht länger als 5 Zeichen lang sein");
            }
			
			//Prüfen, ob der ÜbernehmenButton aktiviert werden darf
			activateChangeButton(oldValue, newValue, "");
		};
		
		ChangeListener<LocalDate> dateListener = (observable, oldValue, newValue) -> {
			if(oldValue != null)	
				//Prüfen, ob der ÜbernehmenButton aktiviert werden darf
				activateChangeButton(oldValue, newValue, null);
		};

        ChangeListener<Department> departmentListener = (observable, oldValue, newValue) -> {
            if(oldValue != null)
                //Prüfen, ob der ÜbernehmenButton aktiviert werden darf
                activateChangeButton(oldValue, newValue, null);
        };
		
		//setzen der ebend definierten Listener
		firstnameField.textProperty().addListener(textListener);
		firstnameField.textProperty().addListener(textListener);
		lastnameField.textProperty().addListener(textListener);
		cityField.textProperty().addListener(textListener);
		streetField.textProperty().addListener(textListener);
		streetNrField.textProperty().addListener(textListener);
		zipCodeField.textProperty().addListener(plzListener);
        birthdayField.valueProperty().addListener(dateListener);
        departmentComboBox.valueProperty().addListener(departmentListener);
        birthdayField.getEditor().textProperty().addListener(textListener);
    }
	
	/**
	 * Prueft, ob alle TextFields + DatePicker gefuellt sind
	 * @return true, wenn alle TextFields + DatePicker gefuellt sind, ansonsten false
	 */
	private boolean isAllFieldsFillt(){
		if(		firstnameField.getText().equals("") ||
				lastnameField.getText().equals("") ||
				cityField.getText().equals("") ||
				streetField.getText().equals("") ||
				streetNrField.getText().equals("") ||
				zipCodeField.getText().equals("") ||
				birthdayField.getValue() == null ||
                departmentComboBox.getValue() == null
			) 
			return false;
		
		return true;
	}
	
	/**
	 * Prüft, ob der ÜbernehmeButton aktiviert werden darf
	 * Kriterien: Kein Control darf leer sein, der alte Inhalt darf nicht dem neuen gleich sein 
	 * und es darf keine neue Selektion sein
	 * @param oldValue 	- alter Wert eines Controls
	 * @param newValue 	- neuer Wert eines Controls
	 * @param value		- wogegen geprüft wird (in der regel null, oder bei Strings "")
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
     * säubert das Nachrichtenfeld
     */
    private void cleanInformationLabel(){
    	informationLabel.setText("");
    }

    /**
     * Erzeugt über der Gesamten Arbeitsfläche ein Halbdurchsitiges Pane mit einem
     * ProgressIndicator. Dient dazu die Arbeitsfläche zu sperren, wenn im Hintergrund
     * Operationen wie das lesen oder schreiben in eine Datenbank vollzogen wird, was unter anderem
     * länger dauern kann (z. B. Erzeuge Zufallskunden oder Lösche alle Kunden).
     * Zum löschen dieses Pane´s muss rootPane.getChildren().add(returnObject); aufgerufen werden.
     * 'returnObject' ist das Objekt, was von dieser Methode zurückgegeben wird
     * @return Das erstelle Halbdurchsichtige Pane
     */
    private StackPane callProgress(){
        //Ersellen eines neuen Pane´s
        StackPane glass = new StackPane();

        //setzt die Hintergrudfarbe des Pane auf ein helles Grau mit einem Durchlässigkeitswert von 60%
        glass.setStyle("-fx-background-color: rgba(200, 200, 200, 0.6);");

        //Erstellt einen ProgressIndicator und setzt seine maximale Größe
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setMaxSize(100, 100);

        //fügt dem durchlässigen Pane den ProgressIndicator hinzu
        glass.getChildren().add(indicator);

        //fügt der Gesamtoberfläche das neue Pane hinzu
        rootPane.getChildren().add(glass);

        return glass;
    }
}
