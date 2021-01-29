package controller;

import controller.Interface.FillOutFormInterface;
import controller.fx.AnswerFormFX;
import controller.fx.QuizFx;
import database.mysql.RetriveQuizFromDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import launcher.Main;
import model.Answer;
import model.Question;
import model.Quiz;
import model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ismael Ben Cherif
 * This class is used to provide functionality to the U.I the student sees when
 * he or she choose a quiz to answer.
 */

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
    private List<List<AnswerFormFX>> answersFXListPerQuestion = new ArrayList<List<AnswerFormFX>>(); //need to get this to quizResults
    private List<Integer> countPossibleAnswers = new ArrayList<>();
    private List<Integer> countGivenAnswers = new ArrayList<>();
    private QuizFx selectedQuiz;

    private int questionNumber =1;

    /**
     * When initialized a text is set to the question label, the quiz is added, the answers are created
     * as objects in the AnswerFormFX so use of JavaFX atrributes can be used like buttons, text fields
     * and checkboxes.
     * Properties are assigned to the attributes.
     */
    public void initialize()
    {
        selectedQuiz = (QuizFx) Main.getPrimaryStage().getUserData();
        //this.quiz = selectedQuiz.getQuizObject();
        System.out.println("selected quiz" + selectedQuiz.getName());

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

    /**
     * @author Ismael Ben Cherif
     * When a student goes to the next question the label, textarea and tableview are updated
     * so the student sees the current questions and answers.
     */
    public void onNextscreenUpdate()
    {
        questionLabel.setText(String.format("Vraag %d", questionNumber));
        this.questionTextAres.setText(this.questions.get(questionNumber-1).getQuestion());
        setUpTableView();
    }

    /**
     * @author Ismael Ben Cherif
     * Is used to see how many answers a question has an set it to 1, because
     * every question can only have one correct answer.
     * An other array is filled to see if an answer is chosen.
     */
    public void fillPossibleAndGivenAnswers()
    {
        for (Question question: this.questions)
        {
            this.countPossibleAnswers.add(1);
            this.countGivenAnswers.add(0);
        }
    }

    /**
     * @author Ismael Ben Cherif
     * If the student selects an answer, 1 is added to the countgivenanswers.
     */
    public void addOnePerQuestionToGivenAnswer()
    {
        this.countGivenAnswers.set(questionNumber-1,
                this.countGivenAnswers.get(questionNumber-1)+1);
    }

    /**
     * @author Ismael Ben Cherif
     * If the student cklicks an already selected answer, 1 is substracted for the
     * countgivenanswes, so another answer can be selected.
     */
    public void substractOnePerQuestionToGivenAnswer()
    {
        this.countGivenAnswers.set(questionNumber-1,
                this.countGivenAnswers.get(questionNumber-1)-1);
    }

    /**
     * @author Ismael Ben Cherif
     * Checks if an answer has already been given to a question.
     * @return
     */
    private boolean compareGivenToPossibleAnswers()
    {
        boolean returnValue = true;
        if (countGivenAnswers.get(questionNumber-1)==countPossibleAnswers.get(questionNumber-1))
        {
            returnValue = false;
        }

        return returnValue;
    }

    /**
     * @author Ismael Ben Cherif.
     * Ads the following action to the button:
     * If a answer is not selected, the student can select one and the choice is saved in the answer
     * object.
     * A student can deselct an gevin answer, after wich the answer is decelected and the student can
     * pick another answer.
     */
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

    /**
     * @author IsmaelBen Cherif
     * The textarea's of the answers are filled with the answers of the answer objects
     * and are set to not editable
     */
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

    /**
     * @author Ismael Ben Cherif
     * Checkboxes are set to disabled.
     * If an answer is selected the checkbox will be checked, if not the checkbox
     * will be unchexked.
     */
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

    /**
     * @author Ismael Ben Cherif.
     * Retrieves question and answers data from the database and ads it to a quiz object.
     * From this object the mixed answers are entered in a List.
     */
    public void callCreateAndAddQuiz()
    {
        Quiz newQuiz = selectedQuiz.getQuizObject();
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

    /**
     * @author Ismael Ben Cherif
     * the answers are entered in rhe answerformFX so JavaFX functionality can be used
     * like buttons, textarea and checkbox.
     */
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

    /**
     * @author Ismael Ben Cherif
     * The tableview is loaded with the attributes it should display.
     */
    public void setUpTableView()
    {
        checkBoxColumn.setCellValueFactory(cellData -> cellData.getValue().checkBoxProperty());
        choiceButtonColumn.setCellValueFactory(cellData -> cellData.getValue().buttonProperty());
        answerTextColumn.setCellValueFactory(cellData -> cellData.getValue().textAreaProperty());

        answerTableView.setItems(answerFormFXObservableList());
    }

    /**
     * @author Ismael Ben Cherif
     * An list is created called observable list to make it possible for
     * the tablevieuw to read the attributes.
     * @return
     */
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
        if(AlertHelper.confirmationDialog("If you go back now, quiz answers will not be saved.")) {
            Main.getPrimaryStage().setUserData(Main.getLoggedInUser());
            Main.getSceneManager().showWelcome();
        }
    }

    /**
     * @author Ismael Ben Cherif
     * When the return button is pressed an number is substracted and the previous question
     * and answers are loaded, as long as you're not at question 1. Else the button is disabled.
     * @param actionEvent
     */
    public void returnButtonAction(ActionEvent actionEvent) {
        if (this.questionNumber!=1)
        {
            questionNumber--;
            onNextscreenUpdate();
            this.nextButton.setText("Volgende");
        }
        else
        {
            this.returnButton.isDisabled();
        }
    }

    /**
     * @author Ismael Ben Cherif
     * When the next button is pressed an number is added and the next question
     * and answers are loaded, as long as there are questions. Else the button is disabled.
     * @param actionEvent
     */
    public void nextButtonAction(ActionEvent actionEvent) {
        if (questionNumber<questions.size())
        {
            questionNumber++;
            onNextscreenUpdate();
            if (questionNumber==questions.size())
            {
                this.nextButton.setText("Inleveren.");
            }
        }
        else
        {
            if (AlertHelper.confirmationDialog("Weet je zeker dat je de antwoorden wilt " +
                    "opslaan?"))
            {
                FillOutFormInterface fillOutFormInterface = new FillOutFormInterface(this.quiz,
                        Main.getLoggedInUser(),this.answersFXListPerQuestion,
                        Main.getDBaccess());
                fillOutFormInterface.storeAnswers();
                quizResults.setAnswersFXListPerQuestion(answersFXListPerQuestion);
                Main.getSceneManager().showResults();
            }

            // transfer answersFXListPerQuestion to quizResults
        }
    }
}
