package de.oszimt.ui.impl.gui;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.ui.templates.AdvancedSearch;
import de.oszimt.util.RestService;
import de.oszimt.util.SplitPaneDividerSlider;
import de.oszimt.util.Validation;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

    //Department Slide Part
    @FXML
    public BorderPane departmentPart;
    @FXML
    public Button departmentChangeButton;
    @FXML
    public Button departmentAbortButton;
    @FXML
    public TableColumn<Department, String> departmentNameColumn;
    @FXML
    public TableColumn<Department, Integer> amountColumn;
    @FXML
    public TextField searchFieldDepartment;
    @FXML
    public TableView<Department> departmentTableView;
    @FXML
    public TextField departmentTextField;



    /**
     * Schaltenvariablen um bestimmte Funktionalitäten
     * umzusetzten
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
    String splitPaneCSS = Controller.class.getResource("splitPane.css").toExternalForm();
    String errorCSS = Controller.class.getResource("errorTextField.css").toExternalForm();
    String changeCSS = Controller.class.getResource("button.css").toExternalForm();

    @FXML
    public SplitPane split;
    @FXML
    public ToggleButton slide;

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
		setListenerForControls();;
        //Hiermit ist es möglich, nach der Initialierung Code auszuführen. Notwendig, um in der initialize Methode auf das GUI Objekt zugreifen zu können
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Initialisiert die ComboBox
                departmentComboBox.getItems().addAll(getGui().getConcept().getAllDepartments());

                //Tabelle mit Daten fuellen und Livesuche ermöglichen
                searchInTable();
                searchInDepartmentTable();

                //Callback Methode, um jeder Row ein ContextMenu zu geben, und nicht der gesamten Table
                customerTable.setRowFactory(tableView -> {
                    //final Deklaration einer Row
                    final TableRow<User> row = new TableRow<>();
                    //final Deklaration des ContextMenu für die eine Row
                    final ContextMenu rowMenu = new ContextMenu();
                    //Erstellen des Menü-Eintrages
                    MenuItem removeItem = new MenuItem("Benutzer löschen");
                    //Wenn auf den Eintrag geklickt wird, wird der selektierte User gelöscht
                    removeItem.onActionProperty().set(e -> deleteUser());
                    //hinzufügen des Menü zur Row
                    rowMenu.getItems().add(removeItem);
                    //Binding zwischen der Row und dem ContextMenu
                    row.contextMenuProperty().bind(
                            Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                    .then(rowMenu)
                                    .otherwise((ContextMenu) null));
                    return row;
                });
                //Callback Methode, um jeder Row ein ContextMenu zu geben, und nicht der gesamten Table
                departmentTableView.setRowFactory(tableView -> {
                    //final Deklaration einer Row
                    final TableRow<Department> row = new TableRow<>();
                    //final Deklaration des ContextMenu für die eine Row
                    final ContextMenu rowMenu = new ContextMenu();
                    //Erstellen des Menü-Eintrages
                    MenuItem removeItem = new MenuItem("Abteilung löschen");
                    //Wenn auf den Eintrag geklickt wird, wird der selektierte User gelöscht
                    removeItem.onActionProperty().set(e -> deleteDepartment());
                    //hinzufügen des Menü zur Row
                    rowMenu.getItems().add(removeItem);
                    //Binding zwischen der Row und dem ContextMenu
                    row.contextMenuProperty().bind(
                            Bindings.when(Bindings.isNotNull(row.itemProperty()))
                                    .then(rowMenu)
                                    .otherwise((ContextMenu) null));
                    return row;
                });
            }
        });
        customerTable.getColumns().forEach(e -> {
            e.setMinWidth(50);
            e.setMaxWidth(400);
        });

        /**
         * ONLY TRY THE SPLITSLIDE SHIT
         */
        split.setDividerPositions(0.7);
        split.getStylesheets().addAll(splitPaneCSS);
        SplitPaneDividerSlider slider = new SplitPaneDividerSlider(split, 0, SplitPaneDividerSlider.Direction.RIGHT);
        slider.setAimContentVisible(false);
        slide.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            slider.setAimContentVisible(t1);
        });
        slide.setContentDisplay(ContentDisplay.RIGHT);
        slide.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
            ImageView leftArrow = new ImageView(new Image(this.getClass().getResourceAsStream("img/leftArrow.png")));
            leftArrow.setPreserveRatio(true);
            leftArrow.setFitWidth(21);

            ImageView rightArrow = new ImageView(new Image(this.getClass().getResourceAsStream("img/rightArrow.png")));
            rightArrow.setPreserveRatio(true);
            rightArrow.setFitWidth(21);
            if(t1){
                slide.setText("Zuklappen");
                slide.setGraphic(rightArrow);
            }
            else{
                slide.setText("Aufklappen");
                slide.setGraphic(leftArrow);
            }
        });
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
     * Ändert einen Benutzer
     *
     * wird als onAction in main.fxml benutzt
     */
	@FXML
	private void changeButtonAction(){
        removeAllStylesheets();

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

        User newUser = new User(firstname, lastname, bday, city, street, streetNr, zipcode, department);

        if(validateThisShit(newUser)){
            setErrorMessage("Falsche eingabe, bitte berichtigen");
            return;
        }

        //Neuer Benutzer oder Änderung?
        boolean isNew = true;

        if(!customerTable.getSelectionModel().isEmpty()){
            int id = customerTable.getSelectionModel().getSelectedItem().getId();
            newUser.setId(id);
            isNew = false;
        }

        this.gui.getConcept().upsertUser(newUser);
		
		//Benachrichtigung setzen
		this.setSuccedMessage(newUser + " erfolgreich" + (isNew ? " als User hinzugefügt" : " bearbeitet"));
		
		this.searchInTable();

        /*
            BUG Workaround: https://community.oracle.com/message/10240147#10240147
            Sonst wird die Abteilung in der Tabelle nicht geupdated
        */
		customerTable.getColumns().get(0).setVisible(false);
        customerTable.getColumns().get(0).setVisible(true);
        
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
            searchInDepartmentTable();
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
            this.abortButtonAction();
           removeAllStylesheets();
            searchInTable();

            setSuccedMessage("Alle Nutzer erfolgreich gelöscht");
        });
        abortButtonAction();
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

    @FXML
    public void useRestServiceAction() {
        if(restServiceContextMenu.isSelected() && service == null){
            service = new RestService();
        }
    }

    @FXML
    private void departmentAbortAction() {
        departmentTextField.setText("");
        searchFieldDepartment.setText("");
        departmentTableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void departmentChangeAction() {
        int index = -1;
        int departmentComboBoxIndex = -2;
        if(departmentTableView.getSelectionModel().getSelectedItem() != null) {
            index = departmentTableView.getSelectionModel().getSelectedItem().getId();
            if(departmentComboBox.getSelectionModel().getSelectedItem() != null) {
                departmentComboBoxIndex = departmentComboBox.getSelectionModel().getSelectedItem().getId();
            }
        }
        Department dep = new Department(departmentTextField.getText());
        boolean isNew = true;
        if(!departmentTableView.getSelectionModel().isEmpty()){
            dep.setId(index);
            isNew = false;
        }

        this.gui.getConcept().upsertDepartment(dep);
        //Benachrichtung setzen
        this.setSuccedMessage(dep + " erfolgreich" + (isNew ? " als Abteilung hinzugefügt" : " bearbeitet"));
        this.searchInDepartmentTable();
        this.searchInTable();
        customerTable.getColumns().get(0).setVisible(false);
        customerTable.getColumns().get(0).setVisible(true);
        List<Department> departmentList = this.gui.getConcept().getAllDepartments();
        this.departmentComboBox.getItems().setAll(departmentList);
        if(departmentComboBoxIndex == index){
            departmentComboBox.getSelectionModel().select(index - 1);
        }
        if(index != -1) {
            departmentTableView.getSelectionModel().select(index - 1);
        }
    }

    public Gui getGui() {
        return gui;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }


    private void removeAllStylesheets() {
        firstnameField.getStylesheets().removeAll(errorCSS);
        lastnameField.getStylesheets().removeAll(errorCSS);
        streetNrField.getStylesheets().removeAll(errorCSS);
        streetField.getStylesheets().removeAll(errorCSS);
        cityField.getStylesheets().removeAll(errorCSS);
        zipCodeField.getStylesheets().removeAll(errorCSS);
        birthdayField.getStylesheets().removeAll(errorCSS);
    }

    private boolean validateThisShit(User newUser) {
        boolean isFailed = false;
        if(!Validation.checkIfLetters(newUser.getFirstname())){
            firstnameField.getStylesheets().add(errorCSS);
            isFailed = true;
        }
        if(!Validation.checkIfLetters(newUser.getLastname())){
            lastnameField.getStylesheets().add(errorCSS);
            isFailed = true;
        }
        if(!Validation.checkIfCity(newUser.getCity())){
            cityField.getStylesheets().add(errorCSS);
            isFailed = true;
        }
        if(!Validation.checkIfStreet(newUser.getStreet())){
            streetField.getStylesheets().add(errorCSS);
            isFailed = true;
        }
        if(!Validation.checkIfStreetnr(newUser.getStreetnr())){
            streetNrField.getStylesheets().add(errorCSS);
            isFailed = true;
        }
        if(!Validation.checkIfZipCode(newUser.getZipcode() + "")){
            zipCodeField.getStylesheets().add(errorCSS);
            isFailed = true;
        }
        if(     newUser.getBirthday().isBefore(LocalDate.now().minusYears(115)) ||
                newUser.getBirthday().isAfter(LocalDate.now().minusYears(14))){
            birthdayField.getStylesheets().add(errorCSS);
            isFailed = true;
        }

        return isFailed;
    }

    /**
     * Löschen eines selektierten Benutzers (aus Tabelle und Datenhaltung)
     */
    private void deleteUser(){
        User user = customerTable.getSelectionModel().getSelectedItem();
        if(user != null) {
            this.gui.getConcept().deleteUser(user);
            searchInTable();
            if (isAdvancedSearch) {
                advancedSearcher.setOriginalList(FXCollections.observableArrayList(this.getGui().getConcept().getAllUser()));
            }
        }
    }
    /**
     * Löschen eines selektierten Benutzers (aus Tabelle und Datenhaltung)
     */
    private void deleteDepartment(){
        Department department = departmentTableView.getSelectionModel().getSelectedItem();
        if(department != null) {
            if(department.getAmount() > 0){
                //TODO - Department Teil Überdecken oder neues Fenster aufmachen ? das ist hier die Frage ...
                Parent root;
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("switchDepartmentsForUsers.fxml"));
                    root = loader.load();

                    EraseDepartmentController contr = loader.getController();
                    contr.setGui(this.gui);
                    contr.setEraseDepartment(department);

                    Stage stage = new Stage();
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(customerTable.getScene().getWindow());
                    stage.setTitle("Abteilungszuweisung");
                    stage.setScene(new Scene(root, 400, 450));
                    stage.setResizable(false);
                    stage.showAndWait();

                } catch (IOException e){
                    e.printStackTrace();
                }
                if(this.getGui().getConcept().getAllUser().stream().noneMatch(e -> e.getDepartment().equals(department)))
                    this.getGui().getConcept().deleteDepartment(department);

                //Daten neu aus DB laden um konsistenz gewährleisten zu können
                searchInDepartmentTable();
                searchInTable();
                customerTable.getColumns().get(0).setVisible(false);
                customerTable.getColumns().get(0).setVisible(true);
                this.setSuccedMessage("");

                return;
            }
            this.gui.getConcept().deleteDepartment(department);
            setSuccedMessage("Abteilung erfolgreich gelöscht");
            searchInTable();
            searchInDepartmentTable();
        }
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
				zipCodeField.setText("" + newValue.getZipcode());
				
				streetField.setText(newValue.getStreet());
				streetNrField.setText(newValue.getStreetnr());
				birthdayField.setValue(newValue.getBirthday());

                departmentComboBox.setValue(newValue.getDepartment());

				isNewSelection = false;
				serviceTown = false;
			}
		});

        departmentTableView.getSelectionModel().selectedItemProperty().addListener((obsValue, oldValue, newValue) -> {
            if (newValue != null) {
                departmentTextField.setText(newValue.getName());
            }
        });
	}

    private void searchInDepartmentTable() {
        List<Department> departmentList = this.gui.getConcept().getAllDepartments();

        if (departmentList == null) {
            return;
        }

        ObservableList<Department> masterData = FXCollections.observableArrayList(departmentList);
        FilteredList<Department> filteredData = new FilteredList<>(masterData, p -> true);
        searchFieldDepartment.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(department -> {
                if(newValue == null || newValue.isEmpty())
                    return true;
                if(department.getName().toLowerCase().contains(newValue.toLowerCase()))
                    return true;

                return false;
            });
        });
        SortedList<Department> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(departmentTableView.comparatorProperty());
        departmentTableView.setItems(filteredData);
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

                //Binding des Department Parts
                departmentChangeButton.prefWidthProperty().bind(departmentPart.widthProperty().multiply(0.5));
                departmentAbortButton.prefWidthProperty().bind(departmentPart.widthProperty().multiply(0.5));
                departmentNameColumn.prefWidthProperty().bind(departmentPart.widthProperty().multiply(0.5));
                amountColumn.prefWidthProperty().bind(departmentPart.widthProperty().multiply(0.5));
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

//            if(!newValue.matches("\\d+") && !oldValue.equals(newValue)){
//                zipCodeField.setText(oldValue);
//            }

            if (!isNewSelection) {
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
                birthdayField.getEditor().getText().length() != 10 ||
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
