package model;

public class Grade {

    private int quizId;
    private double grade;
    private int studentId;


    public Grade(int quizId, double grade, int studentId) {
        this.quizId = quizId;
        this.grade = grade;
        this.studentId = studentId;
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
}
