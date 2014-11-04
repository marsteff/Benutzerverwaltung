package de.oszimt.ui.impl.gui;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<User, String> firstnameColumn;
    @FXML
    private TableColumn<User, String> lastnameColumn;
    @FXML
    private TableColumn<User, ComboBox<String>> departmentColumn;

    @FXML
    private StackPane tableRadioPane;
    @FXML
    private RadioButton tableRadioButton;

    @FXML
    private RadioButton allUsersRadioButton;
    @FXML
    private HBox allUsersPane;
    @FXML
    private ComboBox<Department> allUsersComboBox;

    @FXML
    private RadioButton newDepartmentRadioButton;
    @FXML
    private StackPane newDepartmentPane;
    @FXML
    private TextField newDepartmentTextField;

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

    @FXML
    public void initialize(){
        choice.selectedToggleProperty().addListener(e -> removeErrorField());
        tableRadioButton.selectedProperty().setValue(true);
        newDepartmentPane.disableProperty().bind(newDepartmentRadioButton.selectedProperty().not());
        tableRadioPane.disableProperty().bind(tableRadioButton.selectedProperty().not());
        allUsersPane.disableProperty().bind(allUsersRadioButton.selectedProperty().not());

        userDepartmentTableView.prefWidthProperty().bind(widthPane.widthProperty());
        firstnameColumn.prefWidthProperty().bind(userDepartmentTableView.widthProperty().multiply(0.315));
        lastnameColumn.prefWidthProperty().bind(userDepartmentTableView.widthProperty().multiply(0.315));
        departmentColumn.prefWidthProperty().bind(userDepartmentTableView.widthProperty().multiply(0.315));

        abortButton.prefWidthProperty().bind(widthPane.widthProperty().divide(0.5));
        changeButton.prefWidthProperty().bind(widthPane.widthProperty().divide(0.5));

        ObservableList<String> cbValues = FXCollections.observableArrayList("1", "2", "3");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //Blöder Workaround, um eine ComboBox in die Table zu bekommen
                //TODO - entfernen des erstellen der eigenen UserList
                departmentColumn.setEditable(true);
                List<User> userList = getGui().getConcept().getAllUser();

                List<Department> list = getGui().getConcept().getAllDepartments();
                list.remove(eraseDepartment);
                userList.removeIf(e -> !e.getDepartment().equals(eraseDepartment));

                for (User user : userList) {
                    ComboBox<Department> comboBox = new ComboBox<Department>();
                    comboBox.getItems().addAll(list);
                    comboBox.setPromptText("Abteilung");
                    user.setCombobox(comboBox);
                }
                userDepartmentTableView.getItems().addAll(userList);
                departmentColumn.setCellValueFactory(new PropertyValueFactory<User, ComboBox<String>>("combobox"));

                allUsersComboBox.getItems().addAll(list);
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
        removeErrorField();
        if (tableRadioButton.isSelected()) {
            List<User> userList = userDepartmentTableView.getItems();
            if(userList.parallelStream().anyMatch(e -> e.getCombobox().getValue() == null)){
                this.addErrorField("Jedem Benutzer muss eine Abteilung zugeordnet werden");
                return;
            }
            userList.forEach(e -> e.setDepartment(e.getCombobox().getValue()));
            userList.forEach(e -> getGui().getConcept().upsertUser(e));
        } else if (allUsersRadioButton.isSelected()) {
            List<User> userList = userDepartmentTableView.getItems();
            userList.forEach(e -> {
                e.setDepartment(allUsersComboBox.getValue());
                getGui().getConcept().upsertUser(e);
            });
        } else {

        }
        abortAction();
    }

    private void addErrorField(String message){
        Label label = new Label(message);
        label.setTextFill(Color.RED);
        verticalBox.getChildren().add(verticalBox.getChildren().size() - 2, label);
    }

    private void removeErrorField(){
        if(verticalBox.getChildren().get(verticalBox.getChildren().size()) instanceof Label){
            verticalBox.getChildren().remove(verticalBox.getChildren().size() - 2);
        }
    }
}
