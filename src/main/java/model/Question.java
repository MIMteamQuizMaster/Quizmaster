package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Question {
    private int questionId;
    private int quizId;
    private String question;
    private List<Answer> answers = new ArrayList<>();
    private List<Answer> mixedAnswers;

    public Question(int questionId, String question) {
        this.questionId = questionId;
        this.question = question;
        answers = new ArrayList<>();
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


    public List<Answer> getMixedAnswers() {
        return mixedAnswers;
    }

    /**
     * @author Ismael Ben Cherif
     * @should mix answer and add them to mixedAnswers variable so
     * that the size of Answers and mixed Answer be the same
     *
     **/
    public void mixAnswers()
    {
        mixedAnswers = new ArrayList<>();
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
