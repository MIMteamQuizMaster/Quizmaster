package controller;

import com.mysql.cj.conf.IntegerProperty;
import controller.fx.ClassFX;
import controller.fx.CourseFx;
import controller.fx.UserFx;
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
import model.*;
import model.Class;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.fx.ObjectConvertor.convertClassToClassFX;
import static controller.fx.ObjectConvertor.convertUserToUserFX;


public class TeacherController implements Initializable {

    private DBAccess dBaccess;
    private ClassDAO dao;
    private Teacher loggedInUser;

    @FXML
    public TableView leftTable;
    @FXML
    public TableColumn<ClassFX, Integer> classColumn;
    @FXML
    public TableColumn<UserFx, Integer> studentColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dBaccess = Main.getDBaccess();
        this.dao = new ClassDAO(this.dBaccess);
        //loggedInUser = (User) Main.getPrimaryStage().getUserData();
        loggedInUser = new Teacher(10035,"piet","paulusma");
        fillCoursesTable();
    }

    /**
     * Fill the Course Table view Using CoursFX objects
     */
    public void fillCoursesTable() {
        ObservableList<ClassFX> classes;
        ObservableList<UserFx> students;



        classes = convertClassToClassFX(dao.getAllClasses(loggedInUser));
        students = convertUserToUserFX(dao.getStudents(new Class(23,loggedInUser)));
        System.out.println(students.get(0).getUserId() + students.get(1).getUserId());
        //System.out.println(classes.get(0));
        //TODO: fix NullPointerException DONE
//        classColumn.setCellValueFactory(cellData -> cellData.getValue().dbIdProperty().asObject());
//
//        studentColumn.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
//
//        leftTable.getItems().addAll(classes);



    }
}
