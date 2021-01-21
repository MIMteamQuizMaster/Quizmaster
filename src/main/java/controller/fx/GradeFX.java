package controller.fx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import model.Grade;


public class GradeFX {

    private Grade grade;

    public GradeFX(Grade grade) {
        this.grade = grade;
    }

    public Grade getClassObject(){
        return this.grade;
    }


    public int getQuizId() {
        return new SimpleIntegerProperty(this.grade.getQuizId()).get();
    }

    public IntegerProperty quizIdProperty() {
        return new SimpleIntegerProperty(this.grade.getQuizId());
    }

    public void setQuizId(int quizId) {
        this.grade.setQuizId(quizId);
    }

    public double getGrade() {
        return new SimpleDoubleProperty(this.grade.getGrade()).get();
    }

    public DoubleProperty gradeProperty() {
        return new SimpleDoubleProperty(this.grade.getGrade());
    }

    public void setGrade(int grade) {
        this.grade.setGrade(grade);
    }

    public int getStudentId() {
        return new SimpleIntegerProperty(this.grade.getStudentId()).get();
    }

    public IntegerProperty studentIdProperty() {
        return new SimpleIntegerProperty(this.grade.getStudentId());
    }

    public void setStudentId(int studentId) {
        this.grade.setStudentId(studentId);
    }


}
