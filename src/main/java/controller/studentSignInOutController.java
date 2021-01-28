package controller;

import controller.Interface.StudentSignInOutInterface;
import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.StudentSignInOutDAO;
import database.mysql.User_has_groupDAO;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.util.Duration;
import launcher.Main;
import model.Course;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class studentSignInOutController{

    public Label extraInfoLabel;
    private List<Course> courseListToSignIn;
    private List<Course> courseListAlreadySignedIn;
    private StudentSignInOutDAO studentSignInOutDAO;
    private DBAccess dbAccess;
    private User student;
    private StudentSignInOutInterface myInterface;

    /**
     * @author Ismael Ben Cherif
     * Takes care of the functionality of the U.I. studentSignInSignOut.
     */

    @FXML
    private Button addCourse;
    @FXML
    private Button removeCourse;
    @FXML
    private ListView courseListAdd;
    @FXML
    private ListView courseListRemove;

    /**
     * @author Ismael Ben Cherif
     * Initializes the ListView and set it to multi select.
     */
    public void initialize()
    {
        this.dbAccess = Main.getDBaccess();
        extraInfoBehaviour();
        this.student = Main.getLoggedInUser();//Gets the user that's signed in.
        this.myInterface = new StudentSignInOutInterface(this.dbAccess, this.student);
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

    /**
     * @author Ismael Ben Cherif
     * Shows the user a text displaying how to select multiple
     * items of the list and message disapears after 8 seconds.
     */
    public void extraInfoBehaviour()
    {
        PauseTransition visibleLabel = new PauseTransition(Duration.seconds(8));
        visibleLabel.setOnFinished(event ->
                extraInfoLabel.setVisible(false));
        visibleLabel.play();
    }

    /**
     * @author Ismael Ben Cherif
     * Deletes dthe items in the listview and refils them with up to date info.
     */
    public void refresh()
    {
        this.courseListAdd.getItems().clear();
        this.courseListRemove.getItems().clear();
        this.courseListAdd.refresh();
        this.courseListRemove.refresh();
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
    }


    /**
     * @author Ismael Ben Cherif
     * Ads students to a class and course of the courses the user has selected.
     */
    public void addCoursesToList(ActionEvent actionEvent) {
        List<Course> selectedCourses = this.courseListAdd.getSelectionModel().getSelectedItems();
        myInterface.addStudentsToGroupAndClass(selectedCourses);
        refresh();

    }

    /**
     * @author Ismael Ben Cherif
     * Removes students from a class and course of the courses the user has selected.
     */
    public void removeCoursesFromList(ActionEvent actionEvent) {
        List<Course> selectedCourses = this.courseListRemove.getSelectionModel().getSelectedItems();
        myInterface.deleteStudentFromCourseAndGroup(selectedCourses);
        refresh();
    }
}
