package controller;

import controller.fx.AnswerFX;
import controller.fx.AnswerFormFX;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Answer;

import java.util.ArrayList;
import java.util.List;

import static controller.fx.ObjectConvertor.*;

public class quizResults {

//                <TableView fx:id="resultTable" prefHeight="200.0" prefWidth="366.0">
//              <columns>
//                <TableColumn fx:id="questionNumber" prefWidth="75.0" text="Vraag" />
//                <TableColumn fx:id="userAnswer" prefWidth="113.0" text="Jouw antwoord" />
//                  <TableColumn fx:id="correctBoolean" prefWidth="70.0" text="Correct" />
//                  <TableColumn fx:id="correctAnswer"





    public static List<List<AnswerFormFX>> answersFXListPerQuestion = new ArrayList<List<AnswerFormFX>>();

    public static List<List<AnswerFormFX>> getAnswersFXListPerQuestion() {
        return answersFXListPerQuestion;
    }

    public static void setAnswersFXListPerQuestion(List<List<AnswerFormFX>> answersFXListPerQuestion) {
        quizResults.answersFXListPerQuestion = answersFXListPerQuestion;
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

    private ObservableList<AnswerFX> answerList;

    private List<Answer> givenAnswers = new ArrayList<>();
    private List<Answer> correctAnswers = new ArrayList<>();


    public void initialize() {

//        for (int i = 0; i < answersFXListPerQuestion.size(); i++) {
//            for (int j = 0; j < answersFXListPerQuestion.get(i).size(); j++) {
//                int finalI = i;
//                int finalJ = j;
//                if(answersFXListPerQuestion.get(i).get(j).getAnswer().getIsGivenAnswer()) {
//                    System.out.println(i + " " + j + " " + answersFXListPerQuestion.get(i).get(j).getAnswer().getAnswer() + " USER ANSWER: " + getAnswersFXListPerQuestion().get(i).get(j).getAnswer().getIsGivenAnswer() + " CORRECT ANSWER: " + getAnswersFXListPerQuestion().get(i).get(j).getAnswer().isCorrect());
//                }
//            }
//        }

        fillTable();

    }


    public void fillTable() {

        for (int i = 0; i < answersFXListPerQuestion.size(); i++) {
            for (int j = 0; j < answersFXListPerQuestion.get(i).size(); j++) {


                if (answersFXListPerQuestion.get(i).get(j).getAnswer().getIsGivenAnswer()) {
                    givenAnswers.add(answersFXListPerQuestion.get(i).get(j).getAnswer());
                    System.out.println("GIVEN: " + answersFXListPerQuestion.get(i).get(j).getAnswer().toString());
                    answerList = (convertAnswerToAnswerFX(givenAnswers));
                   // System.out.println(i + " " + j + " " + answersFXListPerQuestion.get(i).get(j).getAnswer().getAnswer() + " USER ANSWER: " + getAnswersFXListPerQuestion().get(i).get(j).getAnswer().getIsGivenAnswer() + " CORRECT ANSWER: " + getAnswersFXListPerQuestion().get(i).get(j).getAnswer().isCorrect());
                }
                if (answersFXListPerQuestion.get(i).get(j).getAnswer().isCorrect()) {
                    correctAnswers.add(answersFXListPerQuestion.get(i).get(j).getAnswer());
                   // System.out.println("GIVEN: " + answersFXListPerQuestion.get(i).get(j).getAnswer().toString());
                    //answerList = (convertAnswerToAnswerFX(givenAnswers));
                }

            }
        }
//        for (int i = 0; i < answerList.size(); i++) {
//
//            AnswerFX alreadyExist;
//            alreadyExist = answerList.get(i);
//            System.out.println(alreadyExist.getCorrectAnswerObject().getAnswer());
//            //answerList.set(i,alreadyExist);
//        }


//        for (int i = 0; i < answerList.size(); i++) {
//            for (int j = 0; j < answersFXListPerQuestion.get(i).size(); j++) {
//                if (answersFXListPerQuestion.get(i).get(j).getAnswer().isCorrect()) {
//                    AnswerFX alreadyExist;
//                    alreadyExist = answerList.get(i);
//                    alreadyExist.setCorrectAnswerObject(answersFXListPerQuestion.get(i).get(j).getAnswer());
//                    System.out.println(alreadyExist.getCorrectAnswerObject().getAnswer());
//                    answerList.set(i,alreadyExist);
//                }
//            }
//        }

        questionNumber.setCellValueFactory(cellData -> cellData.getValue().questionIdProperty().asObject());
        userAnswer.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        correctBoolean.setCellValueFactory(cellData -> cellData.getValue().isCorrectProperty());
        correctAnswer.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        resultTable.getItems().addAll(answerList);
    }



}







