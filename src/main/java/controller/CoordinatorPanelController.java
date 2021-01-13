package controller;

import controller.fx.CourseFx;
import controller.fx.QuestionFx;
import controller.fx.QuizFx;
import database.mysql.CoordinatorDAO;
import database.mysql.DBAccess;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import launcher.Main;
import model.*;

import static controller.fx.ObjectConvertor.*;

public class CoordinatorPanelController {
    public TitledPane quizPane;
    public TitledPane questionPane;

    public Button newQuestionBtn;
    public Button editQuestionBtn;
    public Button deleteQuestionBtn;
    public Button cancelQuestionBtn;

    public CheckBox answerIsCorrectCheckBox;

    public TextArea answerTextArea;
    public Button addAnswerToQuestionbtn;
    @FXML
    private TextArea questionTextArea;

    @FXML
    private Label courseLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private Label quizLabel;

    @FXML
    private TableView<CourseFx> courseTable;
    @FXML
    private TableColumn<CourseFx, String> col_sdate;
    @FXML
    private TableColumn<CourseFx, String> col_edate;
    @FXML
    private TableColumn<CourseFx, String> col_course_name;
    public ListView<QuizFx> quizListView;
    public ListView<QuestionFx> questionListView;
    public ListView<Answer> answerListView;

    private CoordinatorDAO dao;
    private QuestionFx selectedQuestion;
    private QuizFx selectedQuiz;
    private CourseFx selectedCourse;
    private Answer selectedAnswer;
    private boolean questionEditMode;

    public void initialize() {
        DBAccess dBaccess = Main.getDBaccess();
        User loggedInUser = (User) Main.getPrimaryStage().getUserData();
        this.dao = new CoordinatorDAO(dBaccess);
        this.dao.setCoordinator(loggedInUser);
        System.out.println("initialize");
        fillCoursesTable();
        setQuestionEditMode(false);
        emptyAllSelected();
    }

    /**
     * Fill the Course Table view Using CoursFX objects
     */
    public void fillCoursesTable() {
        ObservableList<CourseFx> courses;
        courses = convertCoursetoCourseFX(dao.getMyCourses());
        col_course_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_sdate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_edate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        courseTable.setItems(courses);
    }

    /**
     * Action method of Click on Course table
     * Which will fill add quizzes of selected course to de ListView
     */
    public void courseTableSelect() {
        if (courseTable.getSelectionModel().getSelectedItem() != null) {
            emptyAllSelected();
            // get selected Course
            this.selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            // get the quizes
//            ObservableList<Quiz> quizzes = dao.getQuizOfCourse(this.selectedCourse.getCourseObject());
            ObservableList<Quiz> quizzes = courseTable.getSelectionModel().getSelectedItem().getQuizzes();

            //

            ObservableList<QuizFx> quizFxes = convertQuizToQuizFX(quizzes);

            // set them to quiz list table
            this.quizListView.setItems(quizFxes);
            this.courseLabel.setText(selectedCourse.getName());
            this.totalLabel.setText(String.valueOf(selectedCourse.getQuizzes().size()));


        }
    }

    public void quizListSelect() {
        if (quizListView.getSelectionModel().getSelectedItem() != null) {

            emptyAllSelected();
            setQuestionEditMode(false);
            newQuestionBtn.setVisible(true);

            this.selectedQuiz = quizListView.getSelectionModel().getSelectedItem();

//            ObservableList<Question> questions = dao.getQuestions(this.selectedQuiz.getQuizObject());
            ObservableList<Question> questions = quizListView.getSelectionModel().getSelectedItem().getQuestions();


            this.questionListView.setItems(convertQuestionToQuestionFX(questions));
            this.quizLabel.setText(this.selectedQuiz.getName());


        }
    }

    public void questionListSelect() {
        if (questionListView.getSelectionModel().getSelectedItem() != null) {
            editQuestionBtn.setVisible(true);
            this.selectedQuestion = questionListView.getSelectionModel().getSelectedItem();

//            answerListView.setItems(dao.getAllAnswers(this.selectedQuestion.getQuestionObject()));
            answerListView.setItems(questionListView.getSelectionModel().getSelectedItem().getAnswers());

            questionTextArea.setText(this.selectedQuestion.getQuestion());
            editQuestionBtn.setVisible(true);
            addAnswerToQuestionbtn.setVisible(true);


        }
    }

    public void answerListSelect(MouseEvent mouseEvent) {
        if (answerListView.getSelectionModel().getSelectedItem() != null) {
            Answer answer = answerListView.getSelectionModel().getSelectedItem();
            answerTextArea.setText(answer.getAnswer());
            answerIsCorrectCheckBox.setSelected(answer.isCorrect());

        }
    }


    private void emptyAllSelected() {
        selectedQuestion = null;
        selectedQuiz = null;
        selectedAnswer = null;

        // empty sub quiz lists
        answerListView.getItems().clear();
        questionListView.getItems().clear();

        quizLabel.setText(" ");

        // hide extra buttons
        newQuestionBtn.setVisible(false);
        editQuestionBtn.setVisible(false);
        addAnswerToQuestionbtn.setVisible(false);

        // empty text area and checkbox
        questionTextArea.clear();
        answerTextArea.clear();
        answerIsCorrectCheckBox.setSelected(false);
    }

    /**
     * This method change visibilty and of keys to determine what user can do
     *
     * @param active a boolean to determine edit mode
     */
    private void setQuestionEditMode(boolean active) {
        if (active) {
            // set text area white and disable
            questionTextArea.setDisable(false);
            questionTextArea.setStyle("-fx-background-color: #90f869");

            // hide new btn
            newQuestionBtn.setVisible(false);

            // change edit btn text to "save" ans make it visible
            editQuestionBtn.setText("Opslaan");

            editQuestionBtn.setVisible(true);
            editQuestionBtn.setDefaultButton(true);

            // show delete btn and cancel btn
            deleteQuestionBtn.setVisible(true);
            deleteQuestionBtn.setCancelButton(true);

            cancelQuestionBtn.setVisible(true);
            // set the mode on so the save btn works appropriate
            this.questionEditMode = true;

        } else {
            // set text area gray and disable
            questionTextArea.setDisable(true);
            questionTextArea.setStyle("-fx-background-color: gray");

            // show new btn
            newQuestionBtn.setVisible(true);
            // change edit btn text back  to "edit"
            editQuestionBtn.setText("Bijwerken");
            editQuestionBtn.setVisible(false);

            // hide delete btn and cancel btn
            deleteQuestionBtn.setVisible(false);
            cancelQuestionBtn.setVisible(false);

            this.questionEditMode = false;
        }
    }


    /**
     * The action binded to New Question btn in GUI
     * which makes fields available for user to type a new Question
     */
    public void newQuestionBtnAction() {
        if (quizListView.getSelectionModel().getSelectedItem() != null) {

            /// empty the textArea and the selectedQuestion and listViewSelection
            this.selectedQuestion = null;
            questionListView.getSelectionModel().clearSelection();
            questionTextArea.setText("");

            setQuestionEditMode(true);
            deleteQuestionBtn.setVisible(false); // hide the delete btn cause its useless for new entry

        }
    }

    /**
     * the action binded to Edit/Save buttom which behaves differently according to the QuestionEditMode boolean in class
     */
    public void editQuestionBtnAction() {
        Question question;
        String questionString = questionTextArea.getText();
        if (questionEditMode) {
            // id the mode is On the the key will act like a Save btn
            if (this.selectedQuestion == null) {
                // if the user has chose new item then the selectionQuestion is empty so
                //  we creat an object with id 0 so in dao we can use it as a trick to call INSERT query
                // create new Question Object and send it to dao for saving function
                question = new Question(questionString);
                question.setQuizId(this.selectedQuiz.getIdquiz());
                question.setQuestionId(0);
                question = dao.saveQuestion(question); // save the question
                quizListView.getSelectionModel().getSelectedItem().addQuestion(question); // add it to quizlist object
                selectedQuestion = new QuestionFx(question);
                questionListView.getItems().add(selectedQuestion);
                questionListView.getSelectionModel().select(selectedQuestion);
                answerListView.setItems(selectedQuestion.getAnswers());

            } else { /// UPDATE ITEM so we send update the selected question and send it to dao save method
                questionListView.getSelectionModel().getSelectedItem().setQuestion(questionString);

                selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                question = dao.saveQuestion(this.selectedQuestion.getQuestionObject());
                questionListView.getSelectionModel().getSelectedItem().setQuestionId(question.getQuestionId());

            }
            if (question != null) { // after succesfull add we disable the editMode
                setQuestionEditMode(false);
                addAnswerToQuestionbtn.setVisible(true);
            }

        } else {
            /// then the edit method is not activated so the key will be act to active it
            setQuestionEditMode(true);
        }

        quizListView.refresh();
        questionListView.refresh();
    }

    /**
     * Delete the selected qustion from quiz
     */
    public void deleteQuestionBtnAction() {
        boolean result = dao.deleteQuestion(this.selectedQuestion.getQuestionObject());
        if (result) {
            this.selectedQuiz.removeQuestion(this.selectedQuestion.getQuestionObject());
        }
        questionListView.getItems().remove(questionListView.getSelectionModel().getSelectedIndex());
        quizListView.refresh();
        questionListView.refresh();

//        ObservableList<Question> questions = this.selectedQuiz.getQuestions();
//        this.questionListView.setItems(convertQuestionToQuestionFX(questions));

        questionTextArea.setText("");
        this.selectedQuestion = null;
        setQuestionEditMode(false);

    }

    /**
     * Cancel editing end disable the editMode
     */
    public void cancelQuestionBtnAction() {
        setQuestionEditMode(false);
//        questionListSelect();
    }

    /**
     * add answer to the databes according to the last selected question
     */
    public void addAnswerToQuestionAction(ActionEvent actionEvent) {
        // create the object fully with all needed information
        String answerString = answerTextArea.getText();
        Boolean isCorrect = answerIsCorrectCheckBox.isSelected();
        Answer answer = new Answer(isCorrect, answerString);
        int questionId = this.selectedQuestion.getQuestionId();
        answer.setQuestionId(questionId);

        /// Send to DAO
        answer = dao.addAnswerToQuestion(answer);
        questionListView.getSelectionModel().getSelectedItem().addAnswers(answer);

//        selectedQuestion.addAnswers(answer);
//        answerListView.getItems().add(answer);
        answerListView.refresh();
        answerTextArea.setText("");
    }


}
