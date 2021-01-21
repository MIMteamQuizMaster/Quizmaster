package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import launcher.Main;
import model.Course;

import java.util.ArrayList;
import java.util.List;

public class studentSignInOutController {

    private List<Course> courseList = new ArrayList<>();
    private CourseDAO courseDAO;
    private DBAccess dbAccess;


    @FXML
    private Button addCourse;
    @FXML
    private Button removeCourse;
    @FXML
    private ListView courseListAdd;
    @FXML
    private ListView courseListRemove;

    public studentSignInOutController() {
        this.dbAccess = Main.getDBaccess();
    }

    private void initialize()
    {

    }


    public void addCoursesToList(ActionEvent actionEvent) {
    }

    public void removeCoursesFromList(ActionEvent actionEvent) {
    }
}
