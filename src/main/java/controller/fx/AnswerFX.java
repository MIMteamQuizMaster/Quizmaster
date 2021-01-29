package controller.fx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Answer;

public class AnswerFX {


    private SimpleIntegerProperty questionNumberUI;
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty questionId;
    private SimpleBooleanProperty isCorrect;
    private SimpleStringProperty answer;
    private Answer answerObject;
    private Answer correctAnswerObject;

    public int getQuestionNumberUI() {
        return questionNumberUI.get();
    }

    public SimpleIntegerProperty questionNumberUIProperty() {
        return questionNumberUI;
    }

    public void setQuestionNumberUI(int questionNumberUI) {
        this.questionNumberUI.set(questionNumberUI);
    }


    public AnswerFX(Answer a) {
        this.answerObject = a;
    }

    public SimpleBooleanProperty isGivenProperty() {
        return new SimpleBooleanProperty(answerObject.getIsGivenAnswer());
    }

    public int getId() {
        return new SimpleIntegerProperty(answerObject.getId()).get();
    }

    public Answer getAnswerObject() {
        return answerObject;
    }

    public Answer getCorrectAnswerObject() {
        return correctAnswerObject;
    }

    public void setCorrectAnswerObject(Answer correctAnswer) {
        this.correctAnswerObject = correctAnswer;
    }


    public SimpleIntegerProperty idProperty() {
        return new SimpleIntegerProperty(answerObject.getId());
    }

    public void setId(int id) {
        this.answerObject.setId(id);
    }

    public int getQuestionId() {
        return new SimpleIntegerProperty(answerObject.getQuestionId()).get();
    }

    public SimpleIntegerProperty questionIdProperty() {
        return new SimpleIntegerProperty(answerObject.getQuestionId());
    }

    public void setQuestionId(int questionId) {
        this.answerObject.setQuestionId(questionId);
    }

    public boolean isIsCorrect() {
        return new SimpleBooleanProperty(answerObject.isCorrect()).get();
    }

    public SimpleBooleanProperty isCorrectProperty() {
        return new SimpleBooleanProperty(answerObject.isCorrect());
    }

    public void setIsCorrect(boolean isCorrect) {
        this.answerObject.setCorrect(isCorrect);
    }

    public String getAnswer() {
        return new SimpleStringProperty(answerObject.getAnswer()).get();
    }

    public SimpleStringProperty answerProperty() {
        return new SimpleStringProperty(answerObject.getAnswer());
    }

    public SimpleStringProperty correctAnswerProperty() {
        return new SimpleStringProperty(correctAnswerObject.getAnswer());
    }


    public void setAnswer(String answer) {
        this.answerObject.setAnswer(answer);
    }

}
