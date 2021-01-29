package controller.fx;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import model.Grade;

import java.time.LocalDate;

public class GradeFX2 {

    private SimpleIntegerProperty quizId;
    private SimpleDoubleProperty grade;
    private SimpleIntegerProperty studentId;
    private SimpleStringProperty quizName;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleDoubleProperty succesDefinition;
    private SimpleObjectProperty<Label> failesPassedLabel;
    private boolean failedPassed;

    private Grade gradeObject;

    public GradeFX2(Grade gradeObject) {
        this.gradeObject = gradeObject;
        this.quizId = new SimpleIntegerProperty(this.gradeObject.getQuizId());
        this.grade = new SimpleDoubleProperty(this.gradeObject.getGrade());
        this.studentId = new SimpleIntegerProperty(this.gradeObject.getStudentId());
        this.quizName = new SimpleStringProperty(this.gradeObject.getQuizName());
        this.date = new SimpleObjectProperty<LocalDate>(this.gradeObject.getDate());
        this.succesDefinition = new SimpleDoubleProperty(this.gradeObject.getSuccesDefinition());
        this.failesPassedLabel = new SimpleObjectProperty<Label>(new Label());
        this.failedPassed = this.gradeObject.checkIfStudentFailed();
        setTextToLabel();
    }

    public int getQuizId() {
        return quizId.get();
    }

    public SimpleIntegerProperty quizIdProperty() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId.set(quizId);
    }

    public double getGrade() {
        return grade.get();
    }

    public SimpleDoubleProperty gradeProperty() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade.set(grade);
    }

    public int getStudentId() {
        return studentId.get();
    }

    public SimpleIntegerProperty studentIdProperty() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId.set(studentId);
    }

    public String getQuizName() {
        return quizName.get();
    }

    public SimpleStringProperty quizNameProperty() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName.set(quizName);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public Grade getGradeObject() {
        return gradeObject;
    }

    public void setGradeObject(Grade gradeObject) {
        this.gradeObject = gradeObject;
    }

    public double getSuccesDefinition() {
        return succesDefinition.get();
    }

    public SimpleDoubleProperty succesDefinitionProperty() {
        return succesDefinition;
    }

    public void setSuccesDefinition(double succesDefinition) {
        this.succesDefinition.set(succesDefinition);
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
