package controller;

import controller.fx.CourseFx;
import controller.fx.QuizFx;
import controller.fx.UserFx;
import database.mysql.CoordinatorDAO;
import database.mysql.DBAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import launcher.Main;
import model.*;

public class CoordinatorPanelController {
    public TitledPane quizPane;
    public ListView<QuizFx> quizListView;
    public TitledPane questionPane;
    public ListView questionListView;
    public Label totalLabel;
    public Label quizLabel;
    @FXML
    private Label courseLabel;
    @FXML
    private TableColumn<CourseFx, String> col_sdate;
    @FXML
    private TableColumn<CourseFx, String> col_edate;
    @FXML
    private TableColumn<CourseFx, String> col_course_name;
    @FXML
    private TableView<CourseFx> courseTable;
    private User loggedInUser;
    private DBAccess dBaccess;
    private CoordinatorDAO dao;

    public void initialize() {
        this.dBaccess = Main.getDBaccess();
        this.loggedInUser = (User) Main.getPrimaryStage().getUserData();
        this.dao = new CoordinatorDAO(this.dBaccess);
        this.dao.setCoordinator(this.loggedInUser);
        System.out.println("initialize");
        fillCoursesTable();
    }

    /**
     * Convert User objects to UserFX objects
     * @param userObservableList list of User objects
     * @return a observableList UserFx objects
     */
    private ObservableList<CourseFx> convertUsertoUserFX(ObservableList<Course> userObservableList){
        ObservableList<CourseFx> listedCourses = FXCollections.observableArrayList();
        for (Course c:userObservableList) {
            listedCourses.add(new CourseFx(c));
        }
        return listedCourses;
    }
    /**
     * Convert User objects to UserFX objects
     * @param userObservableList list of User objects
     * @return a observableList UserFx objects
     */
    private ObservableList<QuizFx> convertQuiztoQUizFX(ObservableList<Quiz> userObservableList){
        ObservableList<QuizFx> listedQuizes = FXCollections.observableArrayList();
        for (Quiz q:userObservableList) {
            listedQuizes.add(new QuizFx(q));
        }
        return listedQuizes;
    }

    public void fillCoursesTable(){
        ObservableList<CourseFx> courses;
        courses = convertUsertoUserFX(dao.getMyCourses());
        col_course_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_sdate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_edate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        courseTable.setItems(courses);
    }


    public void courseTableClick(MouseEvent mouseEvent) {
        if (courseTable.getSelectionModel().getSelectedItem() != null) {
            CourseFx selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            ObservableList<QuizFx> quizes = convertQuiztoQUizFX(selectedCourse.getQuizzes());
            selectedCourse.setQuizzes(selectedCourse.getQuizzes());
            this.quizListView.setItems(quizes);
            this.courseLabel.setText(selectedCourse.getName());
            this.totalLabel.setText(String.valueOf(selectedCourse.getQuizzes().size()));


        }
    }

    public void quizListClick(MouseEvent mouseEvent) {
        if (quizListView.getSelectionModel().getSelectedItem() != null) {
            QuizFx quiz = quizListView.getSelectionModel().getSelectedItem();
            ObservableList<Question> questions  = quiz.getQuestions();
            this.questionListView.setItems(questions);
            this.quizLabel.setText(quiz.getName());
//            quizPane.setExpanded(false);

        }
    }
}
