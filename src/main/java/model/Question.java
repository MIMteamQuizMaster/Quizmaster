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

    public Question(int questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }

    public Question(String question) {
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public int getQuizId() {
        return quizId;
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

    public List<Answer> getMixedAnswers() {
        return mixedAnswers;
    }

    public void setMixedAnswers(List<Answer> mixedAnswers) {
        this.mixedAnswers = mixedAnswers;
    }

    public void mixAnswers()
    {
        List<Answer> unmixedAnswers = new ArrayList<>(answers);
        Collections.shuffle(unmixedAnswers);
        mixedAnswers = unmixedAnswers;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void removeAnswer(Answer answer) {
        this.answers.remove(answer);
    }


    @Override
    public String toString() {
        return question;}


}
