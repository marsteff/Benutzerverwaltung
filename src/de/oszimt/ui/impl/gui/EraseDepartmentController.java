package de.oszimt.ui.impl.gui;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import de.oszimt.util.Validation;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by Marci on 01.11.2014.
 */
public class EraseDepartmentController extends StackPane{

    @FXML
    private  VBox verticalBox;
    @FXML
    private HBox widthPane;

    @FXML
    private TableView<User> userDepartmentTableView;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> firstnameColumn;
    @FXML
    private TableColumn<User, String> lastnameColumn;
    @FXML
    private TableColumn<User, ComboBox<String>> departmentColumn;

    @FXML
    public HBox tableEventPane;
    @FXML
    private StackPane tableRadioPane;
    @FXML
    private RadioButton tableRadioButton;

    @FXML
    private HBox allUsersEventPane;
    @FXML
    private RadioButton allUsersRadioButton;
    @FXML
    private HBox allUsersPane;
    @FXML
    private ComboBox<Department> allUsersComboBox;
    @FXML
    private Label allUsersLabl;

    @FXML
    private HBox newDepartmentEventPane;
    @FXML
    private RadioButton newDepartmentRadioButton;
    @FXML
    private StackPane newDepartmentPane;
    @FXML
    private TextField newDepartmentTextField;
    @FXML
    private Label newDepartmentLabel;

    @FXML
    private Button changeButton;
    @FXML
    private Button abortButton;
    @FXML
    private ToggleGroup choice;

    /**
     * Hält eine Instance des GUI Objects
     */
    private Gui gui;

    private Department eraseDepartment;

    private boolean isErrorField = false;
    private List<User> userList;

    @FXML
    public void initialize(){
        choice.selectedToggleProperty().addListener(e -> removeErrorField());
        tableRadioButton.selectedProperty().setValue(true);
        newDepartmentPane.disableProperty().bind(newDepartmentRadioButton.selectedProperty().not());
        tableRadioPane.disableProperty().bind(tableRadioButton.selectedProperty().not());
        allUsersPane.disableProperty().bind(allUsersRadioButton.selectedProperty().not());

        userDepartmentTableView.prefWidthProperty().bind(widthPane.widthProperty());
        idColumn.prefWidthProperty().bind(userDepartmentTableView.widthProperty().multiply(0.07));
        firstnameColumn.prefWidthProperty().bind(userDepartmentTableView.widthProperty().multiply(0.3));
        lastnameColumn.prefWidthProperty().bind(userDepartmentTableView.widthProperty().multiply(0.3));
        departmentColumn.prefWidthProperty().bind(userDepartmentTableView.widthProperty().multiply(0.3));

        abortButton.prefWidthProperty().bind(widthPane.widthProperty().divide(0.5));
        changeButton.prefWidthProperty().bind(widthPane.widthProperty().divide(0.5));

        ObservableList<String> cbValues = FXCollections.observableArrayList("1", "2", "3");

        //EventListener um den Teil auch auf der Schrift aktivieren zu können
        tableEventPane.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                choice.selectToggle(tableRadioButton);
            }
        });
        allUsersEventPane.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                choice.selectToggle(allUsersRadioButton);
            }
        });
        newDepartmentEventPane.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                choice.selectToggle(newDepartmentRadioButton);
            }
        });

        allUsersEventPane.setOnMouseEntered(e -> allUsersLabl.setTextFill(Color.DARKBLUE));
        allUsersEventPane.setOnMouseExited(e -> allUsersLabl.setTextFill(Color.BLACK));

        newDepartmentEventPane.setOnMouseEntered(e -> newDepartmentLabel.setTextFill(Color.DARKBLUE));
        newDepartmentEventPane.setOnMouseExited(e -> newDepartmentLabel.setTextFill(Color.BLACK));


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                userList = getGui().getConcept().getUsersByDepartment(eraseDepartment);
                //Blöder Workaround, um eine ComboBox in die Table zu bekommen
                //TODO - entfernen des erstellen der eigenen UserList
                departmentColumn.setEditable(true);

                List<Department> departmentList = getGui().getConcept().getAllDepartments();
                departmentList.remove(eraseDepartment);
                userList.removeIf(e -> !e.getDepartment().equals(eraseDepartment));

                for (User user : userList) {
                    ComboBox<Department> comboBox = new ComboBox<Department>();
                    comboBox.getItems().addAll(departmentList);
                    comboBox.setPromptText("Abteilung");
                    user.setCombobox(comboBox);
                }
                userDepartmentTableView.getItems().addAll(userList);
                departmentColumn.setCellValueFactory(new PropertyValueFactory<User, ComboBox<String>>("combobox"));

                allUsersComboBox.getItems().addAll(departmentList);
            }
        });
    }

    public Gui getGui() {
        return gui;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public Department getEraseDepartment() {
        return eraseDepartment;
    }

    public void setEraseDepartment(Department eraseDepartment) {
        this.eraseDepartment = eraseDepartment;
    }

    @FXML
    private void abortAction() {
        ((Stage)abortButton.getScene().getWindow()).close();
    }

    @FXML
    private void changeAction() {
        if(isErrorField) {
            removeErrorField();
        }
        if (tableRadioButton.isSelected()) {
            if(userList.parallelStream().anyMatch(e -> e.getCombobox().getValue() == null)){
                this.addErrorField("Jedem Benutzer muss eine Abteilung zugeordnet werden");
                this.isErrorField = true;
                return;
            }
            userList.forEach(e -> e.setDepartment(e.getCombobox().getValue()));
            userList.forEach(e -> getGui().getConcept().upsertUser(e));
        } else if (allUsersRadioButton.isSelected()) {
            List<User> userList = userDepartmentTableView.getItems();
            if(allUsersComboBox.getValue() == null){
                this.addErrorField("Abteilung muss gesetzt werden");
                this.isErrorField = true;
                return;
            }
            userList.forEach(e -> {
                e.setDepartment(allUsersComboBox.getValue());
                getGui().getConcept().upsertUser(e);
            });
        } else {
            String newDepartmentName = newDepartmentTextField.getText().trim();
            if(newDepartmentName.equals("")){
                this.addErrorField("Abteilungsname darf nicht leer sein");
                this.isErrorField = true;
                return;
            }
            else if(!Validation.checkIfDepartment(newDepartmentName)){
                this.addErrorField("Abteilungsname darf nur Buchstaben und Zahlen enthalten");
                this.isErrorField = true;
                return;
            }
            this.getGui().getConcept().createDepartment(newDepartmentName);
            List<Department> departmentList = this.getGui().getConcept().getAllDepartments();
            Department department = departmentList.get(departmentList.size() - 1);
            userList.forEach(e -> {
                e.setDepartment(department);
                this.getGui().getConcept().upsertUser(e);
            });
        }
        abortAction();
    }

    private void addErrorField(String message){
        Label label = new Label(message);
        label.setTextFill(Color.RED);
        verticalBox.getChildren().add(verticalBox.getChildren().size() - 1, label);
    }

    private void removeErrorField(){
        if(verticalBox.getChildren().size() == 5 && verticalBox.getChildren().get(verticalBox.getChildren().size()-2) instanceof Label){
            verticalBox.getChildren().remove(verticalBox.getChildren().size() - 2);
        }
    }
}
