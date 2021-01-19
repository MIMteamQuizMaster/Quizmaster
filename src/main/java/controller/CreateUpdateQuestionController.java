package controller;

import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import launcher.Main;
import model.Question;

public class CreateUpdateQuestionController {

    private final QuestionDAO questionDAO;
    private final DBAccess dBaccess;
    private Question question;

    @FXML
    private TextArea questionText;
    @FXML
    private TextArea correctAnswer;
    @FXML
    private TextArea wrongAnswer1;
    @FXML
    private TextArea wrongAnswer2;
    @FXML
    private TextArea wrongAnswer3;
    @FXML
    private Button saveQuestion;
    @FXML
    private Button cancelQuestion;
    @FXML
    private Button nextQuestion;

    public CreateUpdateQuestionController() {
        super();
        this.dBaccess = Main.getDBaccess();
        this.questionDAO = new QuestionDAO(dBaccess);
    }



    private void createQuestion() {
        String questionInput = questionText.getText();
        String cA = correctAnswer.getText();
        String wA1 = wrongAnswer1.getText();
        String wA2 = wrongAnswer2.getText();
        String wA3 = wrongAnswer3.getText();



    }

    public void cancelQuestionAction(ActionEvent actionEvent) {
        dBaccess.closeConnection();
        System.out.println("Connection closed");
        Main.getSceneManager().setWindowTool();
    }

    public void nextQuestionAction(ActionEvent actionEvent) {
        dBaccess.closeConnection();
        System.out.println("Connection closed");
        Main.getSceneManager().showEnterQuestionScene();
    }

    public void saveQuestionAction(ActionEvent actionEvent) {
    }
}
