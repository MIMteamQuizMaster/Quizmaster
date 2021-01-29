package controller;

import database.mysql.AnswerDAO;
import database.mysql.RetriveQuizFromDatabase;
import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import launcher.Main;
import model.Answer;
import model.Question;
import model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class FillOutFormController {

    private DBAccess dBaccess;
    private QuestionDAO questionDAO;
    private List<Question> questions = new ArrayList<>();
    private AnswerDAO answerDAO;
    private List<Answer> answers = new ArrayList<>();
    private List<List<Answer>> answersToQuestions = new ArrayList<List<Answer>>();
    private Quiz quiz;
    private int questionNumber = 1;
    private int questionPositionNumber = 0;

    public FillOutFormController() {
        super();
        this.dBaccess = Main.getDBaccess();
    }

    public Label questionLabel;
    public TextArea answerFieldA;
    public TextArea answerFieldB;
    public TextArea answerFieldC;
    public TextArea answerFieldD;
    public Button answerAButton;
    public Button answerBButton;
    public Button answerCButton;
    public Button answerDButton;
    public TextArea questionTextField;
    public CheckBox checkboxB;
    public CheckBox checkboxA;
    public CheckBox checkboxD;
    public CheckBox checkboxC;
    public Button menuButton;
    public Button previousButton;
    public Button nextButton;

    public void initialize()
    {
        Quiz quiz = new Quiz("Math", 0.55);
        quiz.setQuizId(1);
        RetriveQuizFromDatabase createQuiz = new RetriveQuizFromDatabase();
        this.quiz = createQuiz.returnQuizFromDatabase(quiz);
        this.questions = this.quiz.getQuestions();
        for (Question question: this.questions)
        {
            this.answersToQuestions.add(question.getMixedAnswers());
        }
        nextQuestionAndAnsers();
    }

    public void nextQuestionAndAnsers()
    {
        questionLabel.setText(String.format("Vraag %s",questionNumber));
        questionTextField.setText(this.questions.get(questionNumber-1).getQuestion());
        this.answers = this.questions.get(questionNumber-1).getMixedAnswers();
        answerFieldA.setText(this.answersToQuestions.get(questionNumber-1).get(0).getAnswer());
        answerFieldB.setText(this.answersToQuestions.get(questionNumber-1).get(1).getAnswer());
        answerFieldC.setText(this.answersToQuestions.get(questionNumber-1).get(2).getAnswer());
        answerFieldD.setText(this.answersToQuestions.get(questionNumber-1).get(3).getAnswer());
        checkboxA.setSelected(this.answersToQuestions.get(questionNumber-1).get(0).getIsGivenAnswer());
        checkboxB.setSelected(this.answersToQuestions.get(questionNumber-1).get(1).getIsGivenAnswer());
        checkboxC.setSelected(this.answersToQuestions.get(questionNumber-1).get(2).getIsGivenAnswer());
        checkboxD.setSelected(this.answersToQuestions.get(questionNumber-1).get(3).getIsGivenAnswer());
    }

    public void answerAButtonAction(ActionEvent actionEvent) {
        if (!this.answersToQuestions.get(questionNumber-1).get(0).getIsGivenAnswer())
        {
            this.answersToQuestions.get(questionNumber - 1).get(0).setGivenAnswer(true);
            for (int i = 0; i < this.answersToQuestions.get(questionNumber-1).size(); i++) {
                if (i==0){continue;}
                this.answersToQuestions.get(questionNumber-1).get(i).setGivenAnswer(false);
            }
            nextQuestionAndAnsers();
        }
        else
        {
            this.answersToQuestions.get(questionNumber - 1).get(0).setGivenAnswer(false);
            nextQuestionAndAnsers();
        }
    }

    public void answerBButtonAction(ActionEvent actionEvent) {
        if (!this.answersToQuestions.get(questionNumber-1).get(1).getIsGivenAnswer())
        {
            this.answersToQuestions.get(questionNumber - 1).get(1).setGivenAnswer(true);
            for (int i = 0; i < this.answersToQuestions.get(questionNumber-1).size(); i++) {
                if (i==1){continue;}
                this.answersToQuestions.get(questionNumber-1).get(i).setGivenAnswer(false);
            }
            nextQuestionAndAnsers();
        }
        else
        {
            this.answersToQuestions.get(questionNumber - 1).get(1).setGivenAnswer(false);
            nextQuestionAndAnsers();
        }
    }

    public void answerCButtonAction(ActionEvent actionEvent) {
        if (!this.answersToQuestions.get(questionNumber-1).get(2).getIsGivenAnswer())
        {
            this.answersToQuestions.get(questionNumber - 1).get(2).setGivenAnswer(true);
            for (int i = 0; i < this.answersToQuestions.get(questionNumber-1).size(); i++) {
                if (i==2){continue;}
                this.answersToQuestions.get(questionNumber-1).get(i).setGivenAnswer(false);
            }
            nextQuestionAndAnsers();
        }
        else
        {
            this.answersToQuestions.get(questionNumber - 1).get(2).setGivenAnswer(false);
            nextQuestionAndAnsers();
        }
    }

    public void answerDButtonAction(ActionEvent actionEvent) {
        if (!this.answersToQuestions.get(questionNumber-1).get(3).getIsGivenAnswer())
        {
            this.answersToQuestions.get(questionNumber - 1).get(3).setGivenAnswer(true);
            for (int i = 0; i < this.answersToQuestions.get(questionNumber-1).size(); i++) {
                if (i==3){continue;}
                this.answersToQuestions.get(questionNumber-1).get(i).setGivenAnswer(false);
            }
            nextQuestionAndAnsers();
        }
        else
        {
            this.answersToQuestions.get(questionNumber - 1).get(3).setGivenAnswer(false);
            nextQuestionAndAnsers();
        }
    }

    public void menuButtonAction(ActionEvent actionEvent) {
    }

    public void previousButtonAction(ActionEvent actionEvent) {
        if (questionNumber>1)
        {
            questionNumber--;
            nextQuestionAndAnsers();
        }
        else
        {
            previousButton.isDisabled();
        }
    }

    public void nextButtonAction(ActionEvent actionEvent) {
        if (questionNumber<quiz.getQuestions().size())
        {
            questionNumber++;
            nextQuestionAndAnsers();
        }
        else
        {
            nextButton.isDisabled();
        }
    }
}
