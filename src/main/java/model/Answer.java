
package model;

public class Answer {

    private int answerId;
    private int questionId;

    private int quizQuestionNumber;
    private boolean isCorrect;
    private String answer;

    private boolean isGivenAnswer = false; // Geeft aan of dit antwoord is ingevuld
    //door de student.

    public Answer(boolean isCorrect, String answer) {
        this.isCorrect = isCorrect;
        this.answer = answer;
    }
    public int getQuizQuestionNumber() {
        return quizQuestionNumber;
    }

    public void setQuizQuestionNumber(int quizQuestionNumber) {
        this.quizQuestionNumber = quizQuestionNumber;
    }

    public Answer(int i) {
        this.answerId = 0;
    }

    public int getAnswerId() {
        return answerId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public boolean getIsGivenAnswer() {
        return isGivenAnswer;
    }

    public void setGivenAnswer(boolean givenAnswer) {
        isGivenAnswer = givenAnswer;
    }

    @Override
    public String toString() {
        if (isCorrect)
        {
            return String.format("%s: correct answer", answer);
        }
        else
        {
            return String.format("%s: wrong answer", answer);
        }
    }
}
