package model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private List<Question> questions;
    private double succsesDefinition;
    private String name;
    private int idquiz;
    private int idcourse;
    private int timeLimit;

    public Quiz(String name,double succsesDefinition) {
        this.succsesDefinition = succsesDefinition;
        this.name = name;
        questions = new ArrayList<>();
    }

    public Quiz(String name,double succsesDefinition,  int idquiz, int idcourse, int timeLimit) {
        this.succsesDefinition = succsesDefinition;
        this.name = name;
        this.idquiz = idquiz;
        this.idcourse = idcourse;
        this.timeLimit = timeLimit;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }

    public double getSuccsesDefinition() {
        return succsesDefinition;
    }

    public void setSuccsesDefinition(double succsesDefinition) {
        this.succsesDefinition = succsesDefinition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdquiz() {
        return idquiz;
    }

    public void setIdquiz(int idquiz) {
        this.idquiz = idquiz;
    }

    public int getIdcourse() {
        return idcourse;
    }

    public void setIdcourse(int idcourse) {
        this.idcourse = idcourse;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**
     * @should return size of Questions list
     * @return
     */
    public int getTotal(){
        return getQuestions().size();
    }
    @Override

    public String toString() {
        return getName() + " (" + getTotal() + ")";
    }
}
