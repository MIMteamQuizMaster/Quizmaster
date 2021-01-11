package model;

import javafx.beans.property.*;

public class Answer {

    private IntegerProperty id;
    private IntegerProperty questionId;
    private BooleanProperty isCorrect;
    private StringProperty answer;

    public Answer( boolean isCorrect, String answer) {
        this.isCorrect = new SimpleBooleanProperty(isCorrect);
        this.answer = new SimpleStringProperty(answer);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getQuestionId() {
        return questionId.get();
    }

    public IntegerProperty questionIdProperty() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId.set(questionId);
    }

    public boolean isIsCorrect() {
        return isCorrect.get();
    }

    public BooleanProperty isCorrectProperty() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect.set(isCorrect);
    }

    public String getAnswer() {
        return answer.get();
    }

    public StringProperty answerProperty() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer.set(answer);
    }

    @Override
    public String toString() {
        if (isIsCorrect())
        {
            return String.format("%s: correct answer", answer);
        }
        else
        {
            return String.format("%s: wrong answer", answer);
        }
    }
}
