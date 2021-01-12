package controller.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Answer;
import model.Question;


public class QuestionFx {
    private Question question;


    public QuestionFx(Question q) {
        this.question = q;
    }

    public int getQuestionId() {
        return new SimpleIntegerProperty(this.question.getQuestionId()).get();
    }

    public IntegerProperty questionIdProperty() {
        return new SimpleIntegerProperty(this.question.getQuestionId());
    }

    public void setQuestionId(int questionId) {
        this.question.setQuestionId(questionId);
    }

    public int getQuizId() {
        return new SimpleIntegerProperty(this.question.getQuizId()).get();
    }

    public IntegerProperty quizIdProperty() {
        return new SimpleIntegerProperty(this.question.getQuestionId());
    }

    public void setQuizId(int quizId) {
        this.setQuizId(quizId);
    }

    public String getQuestion() {
        return new SimpleStringProperty(this.question.getQuestion()).get();
    }

    public StringProperty questionProperty() {
        return new SimpleStringProperty(this.question.getQuestion());
    }

    public void setQuestion(String question) {
        this.question.setQuestion(question);
    }

    public ObservableList<Answer> getAnswers() {
        return new SimpleListProperty<>(FXCollections.observableList(this.question.getAnswers())).get();
    }

    public ListProperty<Answer> answersProperty() {
        return new SimpleListProperty<>(FXCollections.observableList(this.question.getAnswers()));
    }

    public void setAnswers(ObservableList<Answer> answers) {
        this.question.setAnswers(answers);
    }

}
