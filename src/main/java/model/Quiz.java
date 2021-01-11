package model;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private List<Question> questions = new ArrayList<>();
    private DoubleProperty succsesDefinition;
    private StringProperty name;
    private IntegerProperty idquiz;
    private IntegerProperty idcourse;

    public Quiz(double succsesDefinition, String name) {
        this.succsesDefinition = new SimpleDoubleProperty(succsesDefinition);
        this.name = new SimpleStringProperty(name);
    }

    public void setIdquiz(int idquiz) {
        this.idquiz.set(idquiz);
    }

    public void setIdcourse(int idcourse) {
        this.idcourse.set(idcourse);
    }

    public void addQuestion(Question q){
        questions.add(q);
    }
    public Question showQuestion(int i){
        return questions.get(i);
    }
    public int getTotal(){
        return questions.size();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public double getSuccsesDefinition() {
        return succsesDefinition.get();
    }

    public DoubleProperty succsesDefinitionProperty() {
        return succsesDefinition;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getIdquiz() {
        return idquiz.get();
    }

    public IntegerProperty idquizProperty() {
        return idquiz;
    }

    public int getIdcourse() {
        return idcourse.get();
    }

    public IntegerProperty idcourseProperty() {
        return idcourse;
    }
}
