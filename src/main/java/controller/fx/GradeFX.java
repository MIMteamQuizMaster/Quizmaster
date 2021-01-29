package controller.fx;

import javafx.beans.property.*;
import javafx.scene.control.Label;
import model.Grade;

import java.time.LocalDate;


public class GradeFX {

    private Grade grade;
    private SimpleObjectProperty<Label> failesPassedLabel;
    private boolean failedPassed;



    public GradeFX(Grade grade) {
        this.grade = grade;
        this.failesPassedLabel = new SimpleObjectProperty<Label>(new Label());
        this.failedPassed = this.grade.checkIfStudentFailed();
        setTextToLabel();

    }

    public Grade getClassObject(){
        return this.grade;
    }

    public String getQuizName() {
        return new SimpleStringProperty(this.grade.getQuizName()).get();
    }




    public StringProperty quizNameProperty() {
        return new SimpleStringProperty(this.grade.getQuizName());
    }

    public StringProperty dateProperty() {
        return new SimpleStringProperty(this.grade.getDate().toString());
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


    public double getSuccesDefinition() {
        return new SimpleDoubleProperty(this.grade.getSuccesDefinition()).get();
    }

    public SimpleDoubleProperty succesDefinitionProperty() {
        return new SimpleDoubleProperty(this.grade.getSuccesDefinition());
    }

    public void setSuccesDefinition(double succesDefinition) {
         new SimpleDoubleProperty(this.grade.getSuccesDefinition()).set(succesDefinition);
    }


    public Label getFailesPassedLabel() {
        return failesPassedLabel.get();
    }

    public SimpleObjectProperty<Label> failesPassedLabelProperty() {
        return failesPassedLabel;
    }

    public void setFailesPassedLabel(Label failesPassedLabel) {
        this.failesPassedLabel.set(failesPassedLabel);
    }

    public void setTextToLabel()
    {
        if (failedPassed)
        {
            getFailesPassedLabel().setText("Niet gehaald");
        }
        else
        {
            getFailesPassedLabel().setText("Geslaagd");
        }
    }
}
