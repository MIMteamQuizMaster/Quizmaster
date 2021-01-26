package controller;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.StudentSignInOutDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import launcher.Main;
import model.Course;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class studentSignInOutController{

    private List<Course> courseListToSignIn;
    private List<Course> courseListAlreadySignedIn;
    private StudentSignInOutDAO studentSignInOutDAO;
    private DBAccess dbAccess;
    private User student;


    @FXML
    private Button addCourse;
    @FXML
    private Button removeCourse;
    @FXML
    private ListView courseListAdd;
    @FXML
    private ListView courseListRemove;

    public void initialize()
    {
        this.dbAccess = Main.getDBaccess();
        this.student = (User) Main.getPrimaryStage().getUserData();//Gets the user that's sined in.
        this.studentSignInOutDAO = new StudentSignInOutDAO(this.dbAccess);
        this.studentSignInOutDAO.setStudent(this.student);
        this.courseListToSignIn = this.studentSignInOutDAO.returnCoursesToRegisterFor();
        this.courseListAlreadySignedIn = this.studentSignInOutDAO.returnCoursesAllreadyRegisterFor();
        for (Course c: courseListToSignIn)
        {
            courseListAdd.getItems().add(c);
        }
        for (Course c: courseListAlreadySignedIn)
        {
            courseListRemove.getItems().add(c);
        }
        courseListAdd.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        courseListRemove.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


    public void addCoursesToList(ActionEvent actionEvent) {
    }

    public void removeCoursesFromList(ActionEvent actionEvent) {
    }
}
