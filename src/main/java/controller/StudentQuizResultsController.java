package controller;

import controller.fx.GradeFX2;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import launcher.Main;
import model.StudentQuizResults;
import model.User;

import java.time.LocalDate;

public class StudentQuizResultsController {
    public TableView<GradeFX2> tableVieuw;
    public TableColumn<GradeFX2, LocalDate> dateColumn;
    public TableColumn<GradeFX2, String> QuizColumn;
    public TableColumn<GradeFX2, Number> gradeColumn;
    public TableColumn extraInfoColumn;
    public TableColumn<GradeFX2, Number> gradeRequiredColumn;
    public TableColumn<GradeFX2, Label> passedFailedColumn;
    private User student;
    private StudentQuizResults studentQuizResults;

    public void initialize()
    {
        this.student = Main.getLoggedInUser();
        this.studentQuizResults = new StudentQuizResults(this.student, Main.getDBaccess());
        setupTableView();

    }

    public void setupTableView()
    {
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        QuizColumn.setCellValueFactory(cellData -> cellData.getValue().quizNameProperty());
        gradeColumn.setCellValueFactory(cellData -> cellData.getValue().gradeProperty());
        gradeRequiredColumn.setCellValueFactory(cellData->cellData.getValue().succesDefinitionProperty());
        passedFailedColumn.setCellValueFactory(cellData -> cellData.getValue().failesPassedLabelProperty());

        tableVieuw.setItems(this.studentQuizResults.observableListGardes());
    }

}
