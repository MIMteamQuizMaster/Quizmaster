package controller;

import controller.fx.ClassFX;
import controller.fx.GradeFX;
import controller.fx.UserFx;
import database.mysql.GroupDAO;
import database.mysql.DBAccess;
import database.mysql.GradeDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import launcher.Main;
import model.*;
import model.Class;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.fx.ObjectConvertor.*;
import static java.lang.String.valueOf;


public class TeacherController implements Initializable {

    private DBAccess dBaccess;
    private GroupDAO dao;
    private GradeDAO gdao;
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
    public TableColumn<ClassFX, Integer> classColumn;
    @FXML
    public TableColumn<UserFx, String> studentColumn;
    @FXML
    public TableColumn<UserFx, String> studentColumnVoornaam;
    @FXML
    public TableColumn<UserFx, String> studentColumnAchternaam;
    @FXML
    public TableColumn<GradeFX, Integer> quizColumn;
    @FXML
    public TableColumn<GradeFX, Double> gradeColumn;

    @FXML
    public ComboBox<ClassFX> groupComboBox;

    private ObservableList<ClassFX> classes = null;

    //TODO: create StudentFX, use instead of UserFx
    private ObservableList<UserFx> students = null;
    private ObservableList<GradeFX> grades = null;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dBaccess = Main.getDBaccess();
        this.dao = new GroupDAO(this.dBaccess);
        this.gdao = new GradeDAO(this.dBaccess);
        //loggedInUser = (User) Main.getPrimaryStage().getUserData();
        loggedInUser = new User(10040,"piet","paulusma");

        fillTable();

        selectStudents();
    }

    /**
     * Fill each table with respective objects in TableColumn
     */
    public void fillTable() {

        //TODO: create StudentFX, use instead of UserFx

        classes = convertClassToClassFX(dao.getAllClasses(loggedInUser));
        students = convertUserToUserFX(dao.getAllStudents(loggedInUser));
        grades = convertGradeToGradeFX(gdao.getAllGradesPerTeacher(loggedInUser));
        //System.out.println(grades.get(0).getGrade());
        classColumn.setCellValueFactory(cellData -> cellData.getValue().dbIdProperty().asObject());

        quizColumn.setCellValueFactory(cellData -> cellData.getValue().quizIdProperty().asObject());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty().asObject());



        classTable.getItems().addAll(classes);
        quizTable.getItems().addAll(grades);
        gradeTable.getItems().addAll(grades);


    }



    public void selectStudents() {
        groupComboBox.setItems(classes);

        groupComboBox.setConverter(new StringConverter<ClassFX>() {
            @Override
            public String toString(ClassFX classFX) {
                return valueOf(classFX.getDbId());
            }

            @Override
            public ClassFX fromString(String s) {
                return null;
            }
        });

        groupComboBox.setOnAction((event) -> {
            studentTable.getItems().clear();
            int selectedIndex = groupComboBox.getSelectionModel().getSelectedIndex();
            ClassFX selectedItem = groupComboBox.getSelectionModel().getSelectedItem();
            System.out.println("bla"+ selectedItem.getDbId());
            students = convertUserToUserFX(dao.getStudentsPerClass(selectedItem.getClassObject()));
            System.out.println("bla0"+students.get(0).getFirstName());
            studentColumnVoornaam.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
            studentColumnAchternaam.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
            //studentColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
            studentTable.getItems().addAll(students);
            System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
            System.out.println("   ComboBox.getValue(): " + groupComboBox.getValue());
        });

    }
}
