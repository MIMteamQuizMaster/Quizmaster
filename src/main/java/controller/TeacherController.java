package controller;

import controller.fx.GradeFX;
import controller.fx.GroupFX;
import controller.fx.UserFx;
import database.mysql.DBAccess;
import database.mysql.GradeDAO;
import database.mysql.GroupDAO;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import launcher.Main;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.fx.ObjectConvertor.*;
import static java.lang.String.valueOf;


/**
 * @author M.J Alden-Montague
 */
public class TeacherController implements Initializable {

    private DBAccess dBaccess;
    private GroupDAO groupDAO;
    private GradeDAO gradeDAO;
    private User loggedInUser;

    @FXML
    public TableView classTable;
    @FXML
    public TableView studentTable;
    @FXML
    public TableView quizTable;
    @FXML
    public TableView gradeTable;

    @FXML
    public TableColumn<GroupFX, String> groupColumnName;
    @FXML
    public TableColumn<GroupFX, Integer> groupColumnID;
    @FXML
    public TableColumn<UserFx, String> studentColumn;
    @FXML
    public TableColumn<UserFx, String> studentColumnVoornaam;
    @FXML
    public TableColumn<UserFx, String> studentColumnAchternaam;
    @FXML
    public TableColumn<GradeFX, String> quizColumn;
    @FXML
    public TableColumn<GradeFX, Double> gradeColumn;

    @FXML
    public TextField averageGrade;

    @FXML
    public TextField quizTotal;

    @FXML
    public ComboBox<GroupFX> groupComboBox;


    private ObservableList<GroupFX> groups = null;
    private ObservableList<UserFx> students = null;
    private ObservableList<GradeFX> grades = null;

    /**
     * fill initial table and start the combobox for selecting the group
     * @param url
     * @param resourceBundle
     * @author M.J Alden-Montague
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dBaccess = Main.getDBaccess();
        this.groupDAO = new GroupDAO(this.dBaccess);
        this.gradeDAO = new GradeDAO(this.dBaccess);
        loggedInUser = (User) Main.getPrimaryStage().getUserData();
        fillGroupTable();
        selectGroup();
    }

    /**
     * Fill quiz table with quiz items, depending on selected student from studentTable
     * @author M.J Alden-Montague
     */
    public void fillQuizTable() {
        TableView.TableViewSelectionModel<UserFx> selectionModel = studentTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<UserFx> selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener(new ListChangeListener<UserFx>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends UserFx> change) {
                System.out.println("Selection changed: " + change.getList());
                if(!selectedItems.isEmpty()) {
                    quizTable.getItems().clear();
                    gradeTable.getItems().clear();
                    grades = convertGradeToGradeFX(gradeDAO.getAllGrades(selectedItems.get(0).getUserObject()));
                    quizColumn.setCellValueFactory(cellData -> cellData.getValue().quizNameProperty());
                    quizTable.getItems().addAll(grades);
                    double total = 0;
                    int count = 0;
                    for(GradeFX grade: grades) {
                        count++;
                    }
                    quizTotal.setText("Totaal: " + String.valueOf(count));
                    fillGradeTable();
                } else {
                    quizTotal.clear();
                }
            }
        });
    }

    /**
     * Fill grade table with grade items, depending on selected quiz from quizTable
     * @author M.J Alden-Montague
     */
    public void fillGradeTable() {
        TableView.TableViewSelectionModel<GradeFX> selectionModel = quizTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        ObservableList<GradeFX> selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener(new ListChangeListener<GradeFX>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends GradeFX> change) {
                System.out.println("Selection changed: " + change.getList());
                if(!selectedItems.isEmpty()) {
                    gradeTable.getItems().clear();
                    grades = convertGradeToGradeFX(gradeDAO.getAllGradesPerQuiz(selectedItems.get(0).getStudentId(),selectedItems.get(0).getQuizId()));
                    gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty().asObject());
                    gradeTable.getItems().addAll(grades);
                    double total = 0;
                    int count = 0;
                    for(GradeFX grade: grades) {
                        total = total + grade.getGrade();
                        count++;
                    }
                    averageGrade.setText("Gemiddelde: " + String.valueOf(total / count));
                } else {
                    averageGrade.clear();
                }
            }
        });
    }

    /**
     * Fill group table with respective objects in TableColumn
     * @author M.J Alden-Montague
     */
    public void fillGroupTable() {
        groups = convertGroupToGroupFX(groupDAO.getAllGroups(loggedInUser));
        groupColumnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        groupColumnID.setCellValueFactory(cellData -> cellData.getValue().dbIdProperty().asObject());
        classTable.getItems().addAll(groups);
    }

    /**
     * setup the combobox to select a group
     * @author M.J Alden-Montague
     */
    public void selectGroup() {
        groupComboBox.setItems(groups);
        groupComboBox.setConverter(new StringConverter<GroupFX>() {
            @Override
            public String toString(GroupFX groupFX) {
                return "Groep ID: " + valueOf(groupFX.getDbId());
            }
            @Override
            public GroupFX fromString(String s) {
                return null;
            }
        });
        groupComboBox.setOnAction((event) -> {
            studentTable.getItems().clear();
            int selectedIndex = groupComboBox.getSelectionModel().getSelectedIndex();
            GroupFX selectedItem = groupComboBox.getSelectionModel().getSelectedItem();
            students = convertUserToUserFX(groupDAO.getStudentsPerGroup(selectedItem.getGroupObject()));
            studentColumnVoornaam.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
            studentColumnAchternaam.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
            studentTable.getItems().addAll(students);
            fillQuizTable();
        });
    }
}
