package model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int questionId;
    private int quizId;
    private String question;
    private List<Answer> answers;

    public Question(String question) {
        this.question = question;
    }

    public Question(int questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswers(Answer answer) {
        this.answers.add(answer);
    }

    @Override
    public String toString() {
        return question;
    }
}
