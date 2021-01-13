package controller.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Answer;
import model.Question;


public class QuestionFx {
    private Question questionObject;


    public QuestionFx(Question q) {
        this.questionObject = q;
    }

    public Question getQuestionObject() {
        return questionObject;
    }

    public int getQuestionId() {
        return new SimpleIntegerProperty(this.questionObject.getQuestionId()).get();
    }

    public IntegerProperty questionIdProperty() {
        return new SimpleIntegerProperty(this.questionObject.getQuestionId());
    }

    public void setQuestionId(int questionId) {
        this.questionObject.setQuestionId(questionId);
    }

    public int getQuizId() {
        return new SimpleIntegerProperty(this.questionObject.getQuizId()).get();
    }

    public IntegerProperty quizIdProperty() {
        return new SimpleIntegerProperty(this.questionObject.getQuestionId());
    }

    public void setQuizId(int quizId) {
        this.setQuizId(quizId);
    }

    public String getQuestion() {
        return new SimpleStringProperty(this.questionObject.getQuestion()).get();
    }

    public StringProperty questionProperty() {
        return new SimpleStringProperty(this.questionObject.getQuestion());
    }

    public void setQuestion(String question) {
        this.questionObject.setQuestion(question);
    }

    public ObservableList<Answer> getAnswers() {
        return new SimpleListProperty<>(FXCollections.observableList(this.questionObject.getAnswers())).get();
    }

    public ListProperty<Answer> answersProperty() {
        return new SimpleListProperty<>(FXCollections.observableList(this.questionObject.getAnswers()));
    }

    public void setAnswers(ObservableList<Answer> answers) {
        this.questionObject.setAnswers(answers);
    }

    public void addAnswers(Answer answer) {
        this.questionObject.addAnswers(answer);
    }

    @Override
    public String toString() {
        return getQuestion();
    }
}
