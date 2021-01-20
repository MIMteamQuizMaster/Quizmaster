package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.TechnischBeheerderDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import launcher.Main;
import model.Coordinator;
import model.Course;
import model.Role;
import model.User;




import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import launcher.Main;
//import model.AlertHelper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TestCourseController implements Initializable {


//    private String name;
//    private Coordinator coordinator;
//    private String startDate;
//    private String endDate;
//

    private DBAccess dBaccess;
    private CourseDAO dao;

    @FXML private TableView<Course> table1;
//    @FXML private TableColumn<Course, String> c2;
//    @FXML private TableColumn<Course, String> c3;
//    @FXML private TableColumn<Course, String> c4;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.dBaccess = Main.getDBaccess();
        this.dao = new CourseDAO(this.dBaccess);
        Course course = dao.getCourseById(2);

        String name = course.getName();
        String startDate = course.getStartDate();
        String endDate = course.getEndDate();

        System.out.println(name + startDate + endDate);

//        TableColumn c2 = new TableColumn("Name");
//        TableColumn c3 = new TableColumn("Startdate");
//        TableColumn c4 = new TableColumn("Enddate");

        TableColumn<Course, String> c1 = new TableColumn<>("Course name");
        c1.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn c2 = new TableColumn("Start date");
        c2.setCellValueFactory(new PropertyValueFactory("startDate"));

        TableColumn c3 = new TableColumn("End date");
        c3.setCellValueFactory(new PropertyValueFactory("endDate"));


//        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
//        c3.setCellValueFactory(new PropertyValueFactory<>("startDate"));
//        c4.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        System.out.println(course.getName() + course.getStartDate());

        table1.getColumns().addAll(c1,c2,c3);
        table1.getItems().add(course);

    }
}
