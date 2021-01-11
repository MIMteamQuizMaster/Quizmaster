package controller;

import database.mysql.CoordinatorDao;
import database.mysql.DBAccess;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import launcher.Main;
import model.Course;
import model.User;

import java.sql.Date;

public class CoordinatorPanelController {
    @FXML
    private TableColumn<Course, String> col_sdate;
    @FXML
    private TableColumn<Course, String> col_edate;
    @FXML
    private TableColumn<Course, String> col_course_name;
    @FXML
    private TableView<Course> courseTable;
    private User loggedInUser;
    private DBAccess dBaccess;
    private ObservableList<Course> courses;
    private CoordinatorDao dao;

    public void initialize() {
        this.dBaccess = Main.getDBaccess();
        this.loggedInUser = (User) Main.getPrimaryStage().getUserData();
        this.dao = new CoordinatorDao(this.dBaccess);
        this.dao.setCoordinator(this.loggedInUser);
        System.out.println("initialize");
        fillCoursesTable();
    }

    public void fillCoursesTable(){
        courses = dao.getMyCourses();
        col_course_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_sdate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_edate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        courseTable.setItems(courses);
    }


}
