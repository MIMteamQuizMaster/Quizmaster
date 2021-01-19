package controller.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import model.Question;
import model.Quiz;

import java.util.List;

public class QuizFx {
    private Quiz quiz;


    public QuizFx(Quiz q) {
        this.quiz = q;
    }

    public Quiz getQuizObject() {
        return quiz;
    }

    public ObservableList<Question> getQuestions() {
        return new SimpleListProperty<>(FXCollections.observableList(this.quiz.getQuestions())).get();
    }


    public ListProperty<Question> questionsProperty() {
        return new SimpleListProperty<>(FXCollections.observableList(this.quiz.getQuestions()));
    }

    public void setQuestions(ObservableList<Question> questions) {
        this.quiz.setQuestions(questions);
    }

    public void addQuestion(Question question) {
        this.quiz.addQuestion(question);
    }

    public void removeQuestion(Question question) {
        this.quiz.removeQuestion(question);
    }



    public double getSuccsesDefinition() {
        return new SimpleDoubleProperty(this.quiz.getSuccsesDefinition()).get();
    }

    public DoubleProperty succsesDefinitionProperty() {
        return new SimpleDoubleProperty(this.quiz.getSuccsesDefinition());
    }

    public void setSuccsesDefinition(double succsesDefinition) {
        this.quiz.setSuccsesDefinition(succsesDefinition);
    }

    public String getName() {
        return new SimpleStringProperty(this.quiz.getName()).get();
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(this.quiz.getName());
    }

    public void setName(String name) {
        this.quiz.setName(name);
    }

    public int getIdquiz() {
        return new SimpleIntegerProperty(this.quiz.getIdquiz()).get();
    }

    public IntegerProperty idquizProperty() {
        return new SimpleIntegerProperty(this.quiz.getIdquiz());
    }

    public void setIdquiz(int idquiz) {
        this.quiz.setIdquiz(idquiz);
    }

    public int getIdcourse() {
        return new SimpleIntegerProperty(this.quiz.getIdcourse()).get();
    }

    public IntegerProperty idcourseProperty() {
        return new SimpleIntegerProperty(this.quiz.getIdcourse());
    }

    public void setIdcourse(int idcourse) {
        this.quiz.setIdcourse(idcourse);
    }

    public int getTimeLimit() {
        return new SimpleIntegerProperty(this.quiz.getTimeLimit()).get();
    }

    public IntegerProperty timeLimitProperty() {
        return new SimpleIntegerProperty(this.quiz.getTimeLimit());
    }

    public void setTimeLimit(int timeLimit) {
        this.quiz.setTimeLimit(timeLimit);
    }

    @Override
    public String toString() {
        return this.quiz.getName() + " (" + this.quiz.getTotal() + ")";
    }

}
