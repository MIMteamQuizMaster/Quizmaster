package controller;

import controller.fx.AnswerFormFX;
import database.mysql.RetriveQuizFromDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private List<Integer> countPossibleAnswers = new ArrayList<>();
    private List<Integer> countGivenAnswers = new ArrayList<>();

    private int questionNumber =1;

    public void initialize()
    {
        questionLabel.setText(String.format("Vraag %d", questionNumber));
        callCreateAndAddQuiz();
        createAndAddAnswerFormFX();
        initiateTextAreaProperty();
        initiateCheckBoxPropertie();
        setActionToButton();
        setUpTableView();
        fillPossibleAndGivenAnswers();
        this.questionTextAres.setText(this.questions.get(questionNumber-1).getQuestion());
    }

    public void onNextscreenUpdate()
    {
        questionLabel.setText(String.format("Vraag %d", questionNumber));
        this.questionTextAres.setText(this.questions.get(questionNumber-1).getQuestion());
        setUpTableView();
    }

    public void fillPossibleAndGivenAnswers()
    {
        for (Question question: this.questions)
        {
            this.countPossibleAnswers.add(1);
            this.countGivenAnswers.add(0);
        }
    }

    public void addOnePerQuestionToGivenAnswer()
    {
        this.countGivenAnswers.set(questionNumber-1,
                this.countGivenAnswers.get(questionNumber-1)+1);
    }

    public void substractOnePerQuestionToGivenAnswer()
    {
        this.countGivenAnswers.set(questionNumber-1,
                this.countGivenAnswers.get(questionNumber-1)-1);
    }

    private boolean compareGivenToPossibleAnswers()
    {
        boolean returnValue = true;
        if (countGivenAnswers.get(questionNumber-1)==countPossibleAnswers.get(questionNumber-1))
        {
            returnValue = false;
        }

        return returnValue;
    }

    public void setActionToButton() {
        for (int i = 0; i < answersFXListPerQuestion.size(); i++) {
            for (int j = 0; j < answersFXListPerQuestion.get(i).size(); j++) {
                int finalI = i;
                int finalJ = j;
                answersFXListPerQuestion.get(i).get(j).getButton().setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (!(answersFXListPerQuestion.get(finalI).get(finalJ).getAnswer().getIsGivenAnswer()) &&
                                compareGivenToPossibleAnswers()) {
                            answersFXListPerQuestion.get(finalI).get(finalJ).getAnswer().setGivenAnswer(true);
                            addOnePerQuestionToGivenAnswer();
                            initiateCheckBoxPropertie();
                            setUpTableView();
                        }
                        else if (answersFXListPerQuestion.get(finalI).get(finalJ).getAnswer().getIsGivenAnswer())
                        {
                            answersFXListPerQuestion.get(finalI).get(finalJ).getAnswer().setGivenAnswer(false);
                            substractOnePerQuestionToGivenAnswer();
                            initiateCheckBoxPropertie();
                            setUpTableView();
                        }
                    }
                });
            }
        }
    }

    public void initiateTextAreaProperty()
    {
        for (int i = 0; i < this.answersFXListPerQuestion.size(); i++) {
            for (int j = 0; j < this.answersFXListPerQuestion.get(i).size(); j++)
            {
                this.answersFXListPerQuestion.get(i).get(j).getTextArea().setEditable(false);
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
                if (this.answersFXListPerQuestion.get(i).get(j).getAnswer().getIsGivenAnswer())
                {
                    this.answersFXListPerQuestion.get(i).get(j).getCheckBox().setSelected(true);
                }
                else
                {
                    this.answersFXListPerQuestion.get(i).get(j).getCheckBox().setSelected(false);
                }
            }
        }
    }

    public void callCreateAndAddQuiz()
    {
        Quiz newQuiz = new Quiz("Math", 5.5);
        newQuiz.setIdquiz(3);
        RetriveQuizFromDatabase createQuiz = new RetriveQuizFromDatabase();
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
