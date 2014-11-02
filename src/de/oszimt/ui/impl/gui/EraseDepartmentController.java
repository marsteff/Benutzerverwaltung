package de.oszimt.ui.impl.gui;

import de.oszimt.model.Department;
import de.oszimt.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Marci on 01.11.2014.
 */
public class EraseDepartmentController extends StackPane{

    @FXML
    private HBox widthPane;

    @FXML
    private TableView<User> userDepartmentTableView;
    @FXML
    private TableColumn<User, String> firstnameColumn;
    @FXML
    private TableColumn<User, String> lastnameColumn;
    @FXML
    private TableColumn<String, String> departmentColumn;

    @FXML
    private StackPane tableRadioPane;
    @FXML
    private RadioButton tableRadioButton;

    @FXML
    private RadioButton allUsersRadioButton;
    @FXML
    private HBox allUsersPane;

    @FXML
    private RadioButton eraseRadioButton;
    @FXML
    private StackPane erasePane;

    @FXML
    private Button changeButton;
    @FXML
    private Button abortButton;

    /**
     * Hält eine Instance des GUI Objects
     */
    private Gui gui;

    @FXML
    public void initialize(){
        tableRadioButton.selectedProperty().setValue(true);
        erasePane.disableProperty().bind(eraseRadioButton.selectedProperty().not());
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
                List<User> userList = getGui().getConcept().getAllUser();
                userDepartmentTableView.getItems().addAll(userList);

                List<Department> list = getGui().getConcept().getAllDepartments();
                List<String> department = new ArrayList<String>();
                for (Department dep : list) {
                    department.add(dep.getName());
                }
                ObservableList<String> departments = FXCollections.observableArrayList(department);

                departmentColumn.setCellFactory(ComboBoxTableCell.<String, String>forTableColumn(departments));
            }
        });


//        departmentColumn.setCellFactory(ComboBoxTableCell.forTableColumn(new DefaultStringConverter(), cbValues));
    }

    public Gui getGui() {
        return gui;
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

}
