package controller;

import controller.fx.AnswerFormFX;
import database.mysql.CreateQuizFromDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import model.Answer;
import model.Question;
import model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class FillOutFormMultipleAnswersController {

    public Label questionLabel;
    public TextArea questionTextAres;
    public TableView<AnswerFormFX> answerTableView;
    public TableColumn<AnswerFormFX, CheckBox> checkBoxColumn;
    public TableColumn<AnswerFormFX, Button> choiceButtonColumn;
    public TableColumn<AnswerFormFX, TextArea> answerTextColumn;
    public Button menuButton;
    public Button returnButton;
    public Button nextButton;
    private Quiz quiz;
    private List<Question> questions = new ArrayList<>();
    private List<List<Answer>> answersListPerQuestion = new ArrayList<List<Answer>>();
    private List<List<AnswerFormFX>> answersFXListPerQuestion = new ArrayList<List<AnswerFormFX>>();
    private List<Integer> possibleCorrectAnsers = new ArrayList<>();
    private int countAnswers = 0;

    private int questionNumber =1;
    private int answerFXPositionNumber = 0;

    public void initialize()
    {
        questionLabel.setText(String.format("Vraag %d", questionNumber));
        this.possibleCorrectAnsers.add(1);
        callCreateAndAddQuiz();
        createAndAddAnswerFormFX();
        initiateTextAreaProperty();
        setUpTableView();
        this.questionTextAres.setText(this.questions.get(questionNumber-1).getQuestion());
        setActionToButton();
    }

    public void onNextscreenUpdate()
    {
        questionLabel.setText(String.format("Vraag %d", questionNumber));
        this.questionTextAres.setText(this.questions.get(questionNumber-1).getQuestion());
        setUpTableView();
    }

    public void setActionToButton()
    {
        for (int i = 0; i < answersFXListPerQuestion.size(); i++) {
            for (int j = 0; j < answersFXListPerQuestion.get(i).size(); j++) {
                int finalJ = j;
                int finalI = i;
                answersFXListPerQuestion.get(i).get(j).getButton().setOnAction(e ->
                {
                    setActionButton(finalI, finalJ);
                });
            }
        }
    }

    public void setActionButton(int i, int j)
    {
        if (!answersFXListPerQuestion.get(i).get(j).getAnswer().getIsGivenAnswer() &&
        countAnswers == 0)
        {
            this.answersFXListPerQuestion.get(i).get(j).getAnswer().setGivenAnswer(true);
            countAnswers++;
            setUpTableView();
        }

        else
        {
            this.answersFXListPerQuestion.get(i).get(j).getAnswer().setGivenAnswer(true);
            countAnswers--;
            setUpTableView();
        }
    }

    public void initiateTextAreaProperty()
    {
        for (int i = 0; i < this.answersFXListPerQuestion.size(); i++) {
            for (int j = 0; j < this.answersFXListPerQuestion.get(i).size(); j++) {
                this.answersFXListPerQuestion.get(i).get(j).getTextArea().setText(
                        answersFXListPerQuestion.get(i).get(j).getAnswer().getAnswer()
                );
                this.answersFXListPerQuestion.get(i).get(j).getTextArea().setPrefHeight(45);
            }
        }
    }

    public void initiateCheckBoxPropertie()
    {
        for (int i = 0; i < this.answersFXListPerQuestion.size(); i++) {
            for (int j = 0; j < this.answersFXListPerQuestion.get(i).size(); j++) {
                this.answersFXListPerQuestion.get(i).get(j).getCheckBox().setDisable(true);
                if (this.answersFXListPerQuestion.get(i).get(j).getAnswer())
            }
        }
    }

    public void callCreateAndAddQuiz()
    {
        Quiz newQuiz = new Quiz("Math", 5.5);
        newQuiz.setIdquiz(1);
        CreateQuizFromDatabase createQuiz = new CreateQuizFromDatabase();
        this.quiz = createQuiz.returnQuizFromDatabase(newQuiz);
        for (Question question: this.quiz.getQuestions())
        {
            this.questions.add(question);
        }
        for (Question question: this.questions)
        {
            this.answersListPerQuestion.add(question.getMixedAnswers());
        }
    }

    public void createAndAddAnswerFormFX()
    {
        for (List<Answer> answers: this.answersListPerQuestion)
        {
            List<AnswerFormFX> answerFormFXList = new ArrayList<>();
            for (Answer answer: answers)
            {
                AnswerFormFX aFF= new AnswerFormFX(answer);
                answerFormFXList.add(aFF);
            }
            this.answersFXListPerQuestion.add(answerFormFXList);
        }
    }

    public void setUpTableView()
    {
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().checkBoxProperty());
        choiceButtonColumn.setCellValueFactory(cellData -> cellData.getValue().buttonProperty());
        answerTextColumn.setCellValueFactory(cellData -> cellData.getValue().textAreaProperty());

        answerTableView.setItems(answerFormFXObservableList());
    }

    public ObservableList<AnswerFormFX> answerFormFXObservableList()
    {
        ObservableList<AnswerFormFX> answerFormFXES = FXCollections.observableArrayList();
        List<AnswerFormFX> answersFXQuestion = this.answersFXListPerQuestion.get(questionNumber-1);
        for (AnswerFormFX answerFormFX: answersFXQuestion)
        {
            answerFormFXES.add(answerFormFX);
        }
        return answerFormFXES;
    }

    public void menuButtonAction(ActionEvent actionEvent) {

    }

    public void returnButtonAction(ActionEvent actionEvent) {
        if (this.questionNumber!=1)
        {
            questionNumber--;
            onNextscreenUpdate();
        }
        else
        {
            this.returnButton.isDisabled();
        }
    }

    public void nextButtonAction(ActionEvent actionEvent) {
        if (questionNumber<questions.size())
        {
            questionNumber++;
            onNextscreenUpdate();
        }
        else
        {
            this.nextButton.isDisabled();

        }
    }
}
