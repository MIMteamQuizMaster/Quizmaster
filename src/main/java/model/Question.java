package model;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int questionId;
    private int quizId;
    private String question;
    private List<Answer> answers = new ArrayList<>();


    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public void addAnswersToQuestion(Answer answer)
    {
        answers.add(answer);
    }


    @Override
    public String toString() {
        return question;}
}
