package model;

import java.time.LocalDate;

public class Grade {

    private int quizId;
    private double grade;
    private int studentId;
    private String quizName;
    private LocalDate date;

    public Grade(int quizId, double grade, int studentId) {
        this.quizId = quizId;
        this.grade = grade;
        this.studentId = studentId;
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
}
