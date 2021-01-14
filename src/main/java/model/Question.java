package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Question {
    private int questionId;
    private int quizId;
    private String question;
    private List<Answer> answers = new ArrayList<>();
    private List<Answer> mixedAnswers;


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

    public void addAnswersToQuestion(List<Answer> newAnswers)
    {
        this.answers = newAnswers;
    }

    public List<Answer> getMixedAnswers() {
        return mixedAnswers;
    }

    public void setMixedAnswers(List<Answer> mixedAnswers) {
        this.mixedAnswers = mixedAnswers;
    }

    public int getQuestionId() {
        return questionId;
    }

    /**
     * @should return a mixed version of answers.
     */
    public void mixAnswers()
    {
        List<Answer> unmixedAnswers = new ArrayList<>(answers);
        Collections.shuffle(unmixedAnswers);
        mixedAnswers = unmixedAnswers;
    }

    @Override
    public String toString() {
        return question;}
}
