package controller.fx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.Answer;

public class AnswerFx {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty questionId;
    private SimpleBooleanProperty isCorrect;
    private SimpleStringProperty answer;
    private Answer answerObject;


    public AnswerFx(Answer a) {
        this.answerObject = a;
    }

    public int getId() {
        return new SimpleIntegerProperty(answerObject.getId()).get();
    }

    public Answer getAnswerObject() {
        return answerObject;
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

    public void setAnswer(String answer) {
        this.answerObject.setAnswer(answer);
    }

}
