package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private List<Question> questions = new ArrayList<>();
    private double succsesDefinition;
    private String name;
    private int idquiz;
    private int idcourse;

    public Quiz(double succsesDefinition, String name) {
        this.succsesDefinition = succsesDefinition;
        this.name = name;
    }

    public void addQuestion(Question q){
        questions.add(q);
    }
    public void addQuestion(List<Question> q){
        this.questions = q;
    }
    public Question showQuestion(int i){
        return questions.get(i);
    }
    public int getTotal(){
        return questions.size();
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void importQuestions(List<Question> questionList)
    {
        this.questions = questionList;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "questions=" + questions +
                ", name='" + name + '\'' +
                ", idquiz=" + idquiz +
                '}';
    }
}
