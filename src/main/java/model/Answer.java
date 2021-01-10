package model;

public class Answer {

    private int id;
    private int questionId;
    private boolean isCorrect;
    private String answer;

    public Answer(boolean isCorrect, String answer) {
        this.isCorrect = isCorrect;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
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