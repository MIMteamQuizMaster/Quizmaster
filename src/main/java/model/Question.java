package model;

public class Question {
    private String question;
    private String correctAnswer;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;

    public Question(String question, String correctAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
    }

    @Override
    public String toString() {
        return question;}
}
