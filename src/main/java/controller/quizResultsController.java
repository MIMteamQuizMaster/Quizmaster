package controller;

import controller.fx.AnswerFX;
import controller.fx.AnswerFormFX;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Answer;

import java.util.ArrayList;
import java.util.List;

import static controller.fx.ObjectConvertor.*;

public class quizResultsController {



    public static List<List<AnswerFormFX>> answersFXListPerQuestion = new ArrayList<List<AnswerFormFX>>();

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

    private ObservableList<AnswerFX> answerList;

    private List<Answer> givenAnswers = new ArrayList<>();
    private List<Answer> correctAnswers = new ArrayList<>();


    public void initialize() {


        fillTable();

    }


    public void fillTable() {

        int count = 0;

        for (int i = 0; i < answersFXListPerQuestion.size(); i++) {
            //boolean runOnce = false;

            for (int j = 0; j < answersFXListPerQuestion.get(i).size(); j++) {
                if (answersFXListPerQuestion.get(i).get(j).getAnswer().getIsGivenAnswer()) {
                    givenAnswers.add(answersFXListPerQuestion.get(i).get(j).getAnswer());
                    answerList = (convertAnswerToAnswerFX(givenAnswers));
                }
                if (answersFXListPerQuestion.get(i).get(j).getAnswer().isCorrect()) {
                    correctAnswers.add(answersFXListPerQuestion.get(i).get(j).getAnswer());
                    System.out.println(count++);

                }

            }
        }


        for (int i = 0; i < givenAnswers.size(); i++) {
            AnswerFX answerFX = answerList.get(i);
            for (int j = 0; j < correctAnswers.size(); j++) {
                if(answerFX.getQuestionId() == correctAnswers.get(j).getQuestionId()) {
                    answerFX.setCorrectAnswerObject(correctAnswers.get(j));
                }
            }
            if (answerFX.getCorrectAnswerObject() == null) {
                answerFX.setCorrectAnswerObject(new Answer(true, ""));
            }

//            if(answerFX.getQuestionId() == correctAnswers.get(i).getQuestionId()) {
//                answerFX.setCorrectAnswerObject(correctAnswers.get(i));
//            }
//
//            answerFX.setQuestionId(i+1);
//            if(i < correctAnswers.size()) {
//                answerFX.setCorrectAnswerObject(correctAnswers.get(i));
//            } else {
//                answerFX.setCorrectAnswerObject(new Answer(true,""));
//            }
            answerList.set(i,answerFX);
        }


        questionNumber.setCellValueFactory(cellData -> cellData.getValue().questionIdProperty().asObject());
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



}







