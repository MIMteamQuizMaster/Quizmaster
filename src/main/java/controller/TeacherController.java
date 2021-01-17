package controller;

import com.mysql.cj.conf.IntegerProperty;
import controller.fx.ClassFX;
import controller.fx.CourseFx;
import database.mysql.ClassDAO;
import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import launcher.Main;
import model.Class;
import model.Course;
import model.Teacher;
import model.User;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.fx.ObjectConvertor.convertClassToClassFX;



public class TeacherController implements Initializable {

    private DBAccess dBaccess;
    private ClassDAO dao;
    private Teacher loggedInUser;

    @FXML
    private TableView<ClassFX> leftTable;
    @FXML
    public TableColumn<ClassFX, Integer> classColumn;
    @FXML
    public TableColumn<ClassFX, IntegerProperty> studentColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dBaccess = Main.getDBaccess();
        this.dao = new ClassDAO(this.dBaccess);
        //loggedInUser = (User) Main.getPrimaryStage().getUserData();
        loggedInUser = new Teacher(10035,"piet","paulusma");

    }

    /**
     * Fill the Course Table view Using CoursFX objects
     */
    public void fillCoursesTable() {
        ObservableList<ClassFX> classes;
        classes = convertClassToClassFX(dao.getAllClasses(loggedInUser));

        classColumn.setCellValueFactory(cellData -> cellData.getValue().dbIdProperty().asObject());
        leftTable.setItems(classes);
    }
}
