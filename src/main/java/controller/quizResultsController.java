package controller;

import controller.fx.AnswerFX;
import controller.fx.AnswerFormFX;
import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.GradeDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import launcher.Main;
import model.Answer;
import model.User;

import java.util.ArrayList;
import java.util.List;

import static controller.fx.ObjectConvertor.*;

public class quizResultsController {



    public static List<List<AnswerFormFX>> answersFXListPerQuestion = new ArrayList<List<AnswerFormFX>>();
    public Button menuBtn;

    public static List<List<AnswerFormFX>> getAnswersFXListPerQuestion() {
        return answersFXListPerQuestion;
    }

    public static void setAnswersFXListPerQuestion(List<List<AnswerFormFX>> answersFXListPerQuestion) {
        quizResultsController.answersFXListPerQuestion = answersFXListPerQuestion;
    }
    @FXML
    private TableView resultTable;

    @FXML
    private TableColumn<AnswerFX, Integer> questionNumber;

    @FXML
    private TableColumn<AnswerFX, String> userAnswer;

    @FXML
    private TableColumn<AnswerFX, Boolean> correctBoolean;

    @FXML
    private TableColumn<AnswerFX, String> correctAnswer;

    @FXML
    private Label quizName;

    @FXML
    private Label quizGrade;


    private ObservableList<AnswerFX> answerList;

    private List<Answer> givenAnswers = new ArrayList<>();
    private List<Answer> correctAnswers = new ArrayList<>();

    private DBAccess dbAccess;

    private GradeDAO gradeDAO;
    private User loggedInUser;


    /**
     * fill the table
     * @author M.J Alden-Montague
     */
    public void initialize() {
        this.dbAccess = Main.getDBaccess();
        this.gradeDAO = new GradeDAO(this.dbAccess);

        fillTable();

        loggedInUser = Main.getLoggedInUser();
        quizName.setText(gradeDAO.selectLastGradeForUser(loggedInUser).getQuizName());
        quizGrade.setText(String.valueOf(gradeDAO.selectLastGradeForUser(loggedInUser).getGrade()));

        menuBtn.setOnAction(event -> menuButtonAction());
    }

    /**
     * Go through list of list answerFXListPerQuestion and extract user GivenAnswer to new list givenAnswers
     * Do the same but for the correctAnswers list for isCorrect answers (answers which are marked as being correct)
     * Setup table and tablecolumns, use a custom CellFactory for display the boolean isCorrect as Juist or Onjuist.
     * @author M.J Alden-Montague
     */
    public void fillTable() {
        for (int i = 0; i < answersFXListPerQuestion.size(); i++) {
            for (int j = 0; j < answersFXListPerQuestion.get(i).size(); j++) {
                if (answersFXListPerQuestion.get(i).get(j).getAnswer().getIsGivenAnswer()) {
                    givenAnswers.add(answersFXListPerQuestion.get(i).get(j).getAnswer());
                    answerList = (convertAnswerToAnswerFX(givenAnswers));
                }
                if (answersFXListPerQuestion.get(i).get(j).getAnswer().isCorrect()) {
                    correctAnswers.add(answersFXListPerQuestion.get(i).get(j).getAnswer());
                }
            }
        }

        for (int i = 0; i < givenAnswers.size(); i++) {
            AnswerFX answerFX = answerList.get(i);
            answerFX.setNumber(i+1);
            for (int j = 0; j < correctAnswers.size(); j++) {
                if(answerFX.getQuestionId() == correctAnswers.get(j).getQuestionId()) {
                    answerFX.setCorrectAnswerObject(correctAnswers.get(j));
                }
            }
            if (answerFX.getCorrectAnswerObject() == null) {
                answerFX.setCorrectAnswerObject(new Answer(true, ""));
            }
            answerList.set(i,answerFX);
        }

        questionNumber.setCellValueFactory(cellData -> cellData.getValue().getNumber().asObject());
        userAnswer.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        correctBoolean.setCellValueFactory(cellData -> cellData.getValue().isCorrectProperty());

        correctBoolean.setCellFactory(tc -> new TableCell<AnswerFX, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null :
                        item.booleanValue() ? "Juist" : "Onjuist");
            }
        });

        correctAnswer.setCellValueFactory(cellData -> cellData.getValue().correctAnswerProperty());
        resultTable.getItems().addAll(answerList);
    }

    public void menuButtonAction() {
        if(AlertHelper.confirmationDialog("Terug naar het hoofdmenu?")) {
            Main.getPrimaryStage().setUserData(Main.getLoggedInUser());
            Main.getSceneManager().showWelcome();
        }
    }

}







