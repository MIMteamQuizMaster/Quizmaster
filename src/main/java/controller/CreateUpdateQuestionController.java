package controller;

import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import model.Question;

public class CreateUpdateQuestionController {

    private QuestionDAO questionDAO;
    private DBAccess dBaccess;
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

    public void saveQuestionAction(ActionEvent actionEvent) {
    }

    public void cancelQuestionAction(ActionEvent actionEvent) {
    }
}
