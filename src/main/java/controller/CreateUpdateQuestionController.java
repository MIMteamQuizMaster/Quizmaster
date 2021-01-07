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

    public CreateUpdateQuestionController() {
        super();
        this.dBaccess = Main.getDBaccess();
        this.questionDAO = new QuestionDAO(dBaccess);
    }

    public void saveQuestionAction(ActionEvent actionEvent)
    {
        createQuestion();
        if (question != null) {
                questionDAO.storeCustomer(question);
                Alert opgeslagen = new Alert(Alert.AlertType.INFORMATION);
                opgeslagen.setContentText("Vraag opgeslagen");
                opgeslagen.show();
        }
    }

    private void createQuestion() {
        String questionInput = questionText.getText();
        String cA = correctAnswer.getText();
        String wA1 = wrongAnswer1.getText();
        String wA2 = wrongAnswer2.getText();
        String wA3 = wrongAnswer3.getText();


        question = new Question(questionInput, cA, wA1,
                wA2, wA3);

    }

    public void cancelQuestionAction(ActionEvent actionEvent) {
        dBaccess.closeConnection();
        System.out.println("Connection closed");
        Main.getSceneManager().setWindowTool();
    }
}
