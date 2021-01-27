package controller;
import controller.fx.CourseFx;
import controller.fx.QuizFx;
import database.mysql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import launcher.Main;
import model.Course;
import model.User;

import static controller.fx.ObjectConvertor.*;

public class selectQuizController {

    private DBAccess dbAccess;
    private User loggedInUser;
    private CourseDAO courseDAO;
    private QuizDAO quizDAO;

    private StudentSignInOutDAO studentSignInOutDAO;

    private ObservableList<CourseFx> courseList = FXCollections.observableArrayList();
    private ObservableList<QuizFx> quizList = FXCollections.observableArrayList();

    private CourseFx selectedCourse;
    private QuizFx selectedQuiz;
    
    @FXML
    private ListView<CourseFx> selectCourse;

    @FXML
    private ListView<QuizFx> selectQuiz;

    @FXML
    private Button startBtn;

    public void initialize() {
        this.dbAccess = Main.getDBaccess();
        this.courseDAO = new CourseDAO(this.dbAccess);
        this.quizDAO = new QuizDAO(this.dbAccess);
        this.studentSignInOutDAO = new StudentSignInOutDAO(this.dbAccess);
        loggedInUser = (User) Main.getPrimaryStage().getUserData();
        studentSignInOutDAO.setStudent(loggedInUser);
        populateCourseTable();
        populateQuizTable();
        initializeListeners();
    }

    public void populateCourseTable() {
        courseList = convertCoursetoCourseFX(studentSignInOutDAO.returnCoursesAllreadyRegisterFor());
        selectCourse.setCellFactory(param -> new ListCell<CourseFx>() {
            @Override
            protected void updateItem(CourseFx courseFx, boolean empty) {
                super.updateItem(courseFx, empty);
                if(empty || courseFx == null || courseFx.getName() == null) {
                    setText("");
                } else {
                    setText(courseFx.getName() + " ID: " + courseFx.getDbId());
                }
            }
        });
        selectCourse.setItems(courseList);
    }

    public void populateQuizTable() {
        selectQuiz.getItems().clear();

        if(selectedCourse != null) {
            quizList.addAll(convertQuizToQuizFX(quizDAO.getQuizOfCourse(selectedCourse.getCourseObject(),false)));
        }

        selectQuiz.setCellFactory(param -> new ListCell<QuizFx>() {
            @Override
            protected void updateItem(QuizFx quizFx, boolean empty) {
                super.updateItem(quizFx, empty);
                if(empty || quizFx == null || quizFx.getName() == null) {
                    setText("");
                } else {
                    setText(quizFx.getName());
                }
            }
        });

        selectQuiz.setItems(quizList);
    }

    public void initializeListeners() {
        selectCourse.setOnMouseClicked(mouseEvent -> {
            selectedCourse = selectCourse.getSelectionModel().getSelectedItem();
            if(selectedCourse != null) {
                System.out.println(selectedCourse.getName());
                populateQuizTable();
            }
        });

        selectQuiz.setOnMouseClicked(mouseEvent -> {
            selectedQuiz = selectQuiz.getSelectionModel().getSelectedItem();
            if(selectedCourse != null) {
                System.out.println(selectedQuiz.getName());

            }
        });
        startBtn.setOnAction((ActionEvent event) -> {
            if (selectedQuiz != null) {
                boolean userConfirm = AlertHelper.confirmationDialog("Do you want to start: " + selectedQuiz.getName() + " ?");
                if(userConfirm) {
                    Main.getPrimaryStage().setUserData(selectedQuiz);
                    Main.getSceneManager().showQuiz();
                }
            }
        });
    }
}


