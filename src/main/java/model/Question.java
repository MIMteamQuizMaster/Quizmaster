package model;

public class Question {
    private String question;
    private String correctAnswer;
    private String wrongAnswer1;
    private String wrongAnswer2;
    private String wrongAnswer3;
    private String givenAnswer;


    public Question(String question, String correctAnswer, String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.wrongAnswer1 = wrongAnswer1;
        this.wrongAnswer2 = wrongAnswer2;
        this.wrongAnswer3 = wrongAnswer3;
    }

    public boolean answerQuestion(String g){
        this.givenAnswer = g;
        return givenAnswer.equals(this.correctAnswer);
    }

    @Override
    public String toString() {
        return question;}
}
