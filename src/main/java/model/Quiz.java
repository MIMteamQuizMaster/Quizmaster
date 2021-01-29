package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private List<Question> questions;
    private double succsesDefinition;
    private String name;
    private int quizId;
    private int courseId;
    private int timeLimit;

    public Quiz(String name,double succsesDefinition) {
        this.succsesDefinition = succsesDefinition;
        this.name = name;
        questions = new ArrayList<>();
    }

    public Quiz(String name, double succsesDefinition, int quizId, int courseId, int timeLimit) {
        this.succsesDefinition = succsesDefinition;
        this.name = name;
        this.quizId = quizId;
        this.courseId = courseId;
        this.timeLimit = timeLimit;
    }

    public Quiz(String name, double succsesDefinition, int quizId, int idcourse) {
        this(name,succsesDefinition, quizId,idcourse,0);

    }

    public Quiz(int i) {
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

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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
