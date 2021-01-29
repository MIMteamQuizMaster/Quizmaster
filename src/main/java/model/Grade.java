package model;

import java.time.LocalDate;

public class Grade {

    private int quizId;
    private double grade;
    private int studentId;
    private String quizName;
    private LocalDate date;
    private double succesDefinition;
    private int gradeId;
    private boolean studentFailed;

    public Grade(int quizId, double grade, int studentId) {
        this.quizId = quizId;
        this.grade = grade;
        this.studentId = studentId;
    }

    public Grade(int quizId, double grade, int studentId, double succesDefinition) {
        this.quizId = quizId;
        this.grade = grade;
        this.studentId = studentId;
        this.succesDefinition = succesDefinition;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getSuccesDefinition() {
        return succesDefinition;
    }

    public void setSuccesDefinition(double succesDefinition) {
        this.succesDefinition = succesDefinition;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public boolean checkIfStudentFailed()
    {
        boolean returnValue = true;
        if (this.grade>=this.succesDefinition)
        {
            returnValue = false;
        }
        this.studentFailed = returnValue;
        return returnValue;
    }

}
