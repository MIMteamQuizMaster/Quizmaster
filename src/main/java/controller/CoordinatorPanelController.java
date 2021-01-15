package controller;

import controller.fx.CourseFx;
import controller.fx.QuestionFx;
import controller.fx.QuizFx;
import database.mysql.CoordinatorDAO;
import database.mysql.DBAccess;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import launcher.Main;
import model.*;

import static controller.fx.ObjectConvertor.*;

public class CoordinatorPanelController {
    public TitledPane quizPane;
    public TitledPane questionPane;

    public Button btnNewQuestion;
    public Button btnEditQuestion;
    public Button btnDeleteQuestion;
    public Button btnCancelQuestion;
    public Button btnNewAnswer;
    public Button btnDeleteAnswer;
    public Button btnNewQuiz;
    public Button btnDeleteQuiz;

    public CheckBox cBoxAnswerIsCorrect;

    public Label labelCourse;
    public Label labelTotalQuizen;
    public Label labelTotalGoodAnswers;
    public Label labelTotalAnswers;
    public Label labelMaxTotalAnswer;

    public TextArea textAnswer;
    public TextField textQuizName;
    public TextField textSuccessDefinite;
    public TextArea textQuestion;
    public TextField textTimeLimit;


    @FXML
    private TableView<CourseFx> courseTable;
    @FXML
    private TableColumn<CourseFx, String> col_sdate;
    @FXML
    private TableColumn<CourseFx, String> col_edate;
    @FXML
    private TableColumn<CourseFx, String> col_course_name;

    public ListView<QuizFx> quizFxListView;
    public ListView<QuestionFx> questionFxListView;
    public ListView<Answer> answersListView;

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
        emptyFieldsAndSelected();
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

    private void emptyFieldsAndSelected() {
//        selectedCourse = null;
        selectedQuiz = null;
        selectedQuestion = null;
        selectedAnswer = null;

        // empty sub quiz lists
        answersListView.getItems().clear();
        questionFxListView.getItems().clear();
        quizFxListView.getItems().clear();

        // hide warning label
        labelMaxTotalAnswer.setVisible(false);

        // hide extra buttons
        setQuestionEditMode(false);

        btnNewQuestion.setVisible(false);
        btnNewQuiz.setVisible(false);
        btnNewAnswer.setVisible(false);
        btnEditQuestion.setVisible(false);

        btnDeleteQuiz.setVisible(false);

        btnDeleteAnswer.setVisible(false);
        cBoxAnswerIsCorrect.setSelected(false);
        cBoxAnswerIsCorrect.setVisible(false);


        // empty text area and checkbox
        textQuestion.clear();
        textAnswer.clear();
        textTimeLimit.clear();
        textSuccessDefinite.clear();
        textQuizName.clear();
    }

    /**
     * Action method of Click on Course table
     * Which will fill add quizzes of selected course to de ListView
     */
    public void courseTableSelect() {
        if (courseTable.getSelectionModel().getSelectedItem() != null) {
            emptyFieldsAndSelected();

            // get selected Course
//            ObservableList<Quiz> quizzes = dao.getQuizOfCourse(this.selectedCourse.getCourseObject());
            selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            refeshCourseSubSections();

        }
    }

    /**
     * Quiz list select function
     */
    public void quizListSelect() {
        if (quizFxListView.getSelectionModel().getSelectedItem() != null) {
//            ObservableList<Question> questions = dao.getQuestions(this.selectedQuiz.getQuizObject()); // update from DB
            selectedQuiz = quizFxListView.getSelectionModel().getSelectedItem();
            refreshQuizSubSection();


        }
    }

    /**
     * Question list select function
     */
    public void questionListSelect() {
        if (questionFxListView.getSelectionModel().getSelectedItem() != null) {
            // set selected quiz
            selectedQuestion = questionFxListView.getSelectionModel().getSelectedItem();
            refreshQuestionSubSection();

        }

    }

    /**
     * Answer list select funtion
     */
    public void answerListSelect() {
        if (answersListView.getSelectionModel().getSelectedItem() != null) {
            selectedAnswer = answersListView.getSelectionModel().getSelectedItem();
            textAnswer.setText(selectedAnswer.getAnswer());
            cBoxAnswerIsCorrect.setSelected(selectedAnswer.isCorrect());
            btnDeleteAnswer.setVisible(true);

        } else {
            btnDeleteAnswer.setVisible(false);
        }

    }

    /**
     *  change or refresh the GUI in sub section of Course
     */
    public void refeshCourseSubSections() {
        if (selectedCourse != null) {
            this.quizFxListView.getItems().clear();
            // fill the quiz list
            this.quizFxListView.setItems(convertQuizToQuizFX(selectedCourse.getQuizzes()));
            // set the course information to labels
            this.labelCourse.setText(selectedCourse.getName());
            this.labelTotalQuizen.setText(String.valueOf(selectedCourse.getQuizzes().size()));

            // make New btn AND Delete btn available
            btnNewQuiz.setVisible(true);
            btnDeleteQuiz.setVisible(false);

            textQuizName.clear();
            textQuizName.setDisable(false);

            textSuccessDefinite.clear();
            textSuccessDefinite.setDisable(false);

            textTimeLimit.clear();
            textTimeLimit.setDisable(false);

        } else {

            emptyFieldsAndSelected();
        }
    }

    /**
     * Refresh the Question Section and add fill the question list if a quiz has been selected
     */
    private void refreshQuizSubSection() {
        if (selectedQuiz != null) {

            // fill questions
            this.questionFxListView.setItems(convertQuestionToQuestionFX(selectedQuiz.getQuestions()));

            // update the values in text Field according to selection
            textQuizName.setText(this.selectedQuiz.getName());
            textQuizName.setDisable(true);

            textSuccessDefinite.setText(String.valueOf(selectedQuiz.getSuccsesDefinition()));
            textSuccessDefinite.setDisable(true);

            textTimeLimit.setText(String.valueOf(selectedQuiz.getTimeLimit()));
            textTimeLimit.setDisable(true);

            /// and show delete btn
            btnDeleteQuiz.setVisible(true);

            btnNewQuiz.setVisible(false);

            setQuestionEditMode(false);

        } else {
            // if nothing is selected then no delete btn for quiz
            if(selectedCourse!= null){
                btnNewQuiz.setVisible(true);
            }

        }
        selectedQuestion = null;
        selectedAnswer = null;
        refreshQuestionSubSection();
    }

    /**
     * If a question has been selected it will update the fields
     * otherwise
     * wil refresh the list of answers and empty the fields
     **/
    private void refreshQuestionSubSection() {
        if (selectedQuestion != null) {
            answersListView.setItems(selectedQuestion.getAnswers());
            answersListView.refresh();

            // fill question textView
            textQuestion.setText(this.selectedQuestion.getQuestion());

            // fill available answer details
            labelTotalAnswers.setText("Aantal antwoorden: " + selectedQuestion.getAnswers().size());
            int coutGood = 0;
            for (Answer a : selectedQuestion.getAnswers()) {
                if (a.isCorrect()) {
                    coutGood++;
                }
            }
            labelTotalGoodAnswers.setText("Aantal juist: " + coutGood);
            // show add answer btn anc check box

            btnNewAnswer.setVisible(true);
            cBoxAnswerIsCorrect.setVisible(true);
            cBoxAnswerIsCorrect.setSelected(false);


            // show question btn
            btnNewQuestion.setVisible(true);
            btnEditQuestion.setVisible(true);


        } else {
            // empty fields and hide btns
            textQuestion.clear();
            cBoxAnswerIsCorrect.setVisible(false);
            btnNewAnswer.setVisible(false);
            btnDeleteAnswer.setVisible(false);
            btnNewQuestion.setVisible(false);
            btnEditQuestion.setVisible(false);
            setQuestionEditMode(false);
            labelTotalAnswers.setText("Aantal antwoorden: ?");
            labelTotalGoodAnswers.setText("Aantal juist: ?");
            answersListView.getItems().clear();

            if(selectedQuiz != null){
                btnNewQuestion.setVisible(true);
            }
        }
        textAnswer.clear();
        labelMaxTotalAnswer.setVisible(false);

    }


    /**
     * This method change visibilty and of keys to determine what user can do
     *
     * @param active a boolean to determine edit mode
     */
    private void setQuestionEditMode(boolean active) {
        if (active) {
            // set text area white and disable
            textQuestion.setDisable(false);
            textQuestion.setStyle("-fx-background-color: #90f869");

            // hide new btn
            btnNewQuestion.setVisible(false);

            // change edit btn text to "save" ans make it visible
            btnEditQuestion.setText("Opslaan");

            btnEditQuestion.setVisible(true);
            btnEditQuestion.setDefaultButton(true);

            // show delete btn and cancel btn
            btnDeleteQuestion.setVisible(true);
            btnDeleteQuestion.setCancelButton(true);

            btnCancelQuestion.setVisible(true);
            // set the mode on so the save btn works appropriate
            this.questionEditMode = true;

        } else {
            // set text area gray and disable
            textQuestion.setDisable(true);
            textQuestion.setStyle("-fx-background-color: gray");

            // show new btn
            btnNewQuestion.setVisible(true);
            // change edit btn text back  to "edit"
            btnEditQuestion.setText("Bijwerken");
            btnEditQuestion.setVisible(false);

            // hide delete btn and cancel btn
            btnDeleteQuestion.setVisible(false);
            btnCancelQuestion.setVisible(false);

            this.questionEditMode = false;
        }
    }


    /**
     * The action binded to New Question btn in GUI
     * which makes fields available for user to type a new Question
     */
    public void newQuestionBtnAction() {
        if (quizFxListView.getSelectionModel().getSelectedItem() != null) {
            refreshQuestionSubSection();
            /// empty the textArea and the selectedQuestion and listViewSelection
            this.selectedQuestion = null;
            questionFxListView.getSelectionModel().clearSelection();
            textQuestion.setText("");

            setQuestionEditMode(true);
            btnDeleteQuestion.setVisible(false); // hide the delete btn cause its useless for new entry

        }
    }

    /**
     * the action binded to Edit/Save buttom which behaves differently according to the QuestionEditMode boolean in class
     */
    public void editQuestionBtnAction() {
        Question question;
        String questionString = textQuestion.getText();
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
                quizFxListView.getSelectionModel().getSelectedItem().addQuestion(question); // add it to quizlist object
                selectedQuestion = new QuestionFx(question);
                questionFxListView.getItems().add(selectedQuestion);
                questionFxListView.getSelectionModel().select(selectedQuestion);
                answersListView.setItems(selectedQuestion.getAnswers());

            } else { /// UPDATE ITEM so we send update the selected question and send it to dao save method
                questionFxListView.getSelectionModel().getSelectedItem().setQuestion(questionString);

                selectedQuestion = questionFxListView.getSelectionModel().getSelectedItem();
                question = dao.saveQuestion(this.selectedQuestion.getQuestionObject());
                questionFxListView.getSelectionModel().getSelectedItem().setQuestionId(question.getQuestionId());

            }
            if (question != null) { // after succesfull add we disable the editMode
                setQuestionEditMode(false);
                cBoxAnswerIsCorrect.setVisible(true);
                btnNewAnswer.setVisible(true);
            }

        } else {
            /// then the edit method is not activated so the key will be act to active it
            setQuestionEditMode(true);
        }

        quizFxListView.refresh();
        questionFxListView.refresh();
    }

    /**
     * Delete the selected qustion from quiz
     */
    public void deleteQuestionBtnAction() {
        boolean result = dao.deleteQuestion(this.selectedQuestion.getQuestionObject());
        if (result) {
            this.selectedQuiz.removeQuestion(this.selectedQuestion.getQuestionObject());
        }
        questionFxListView.getItems().remove(questionFxListView.getSelectionModel().getSelectedIndex());
        quizFxListView.refresh();
        questionFxListView.refresh();

//        ObservableList<Question> questions = this.selectedQuiz.getQuestions();
//        this.questionListView.setItems(convertQuestionToQuestionFX(questions));

        textQuestion.setText("");
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
    public void addAnswerToQuestionAction() {
        // create the object fully with all needed information
        String answerString = textAnswer.getText();
        boolean isCorrect = cBoxAnswerIsCorrect.isSelected();
        Answer answer = new Answer(isCorrect, answerString);
        int questionId = this.selectedQuestion.getQuestionId();
        if (!limitAnswers(answer)) {
            labelMaxTotalAnswer.setVisible(true);
            System.out.println("max 4 answer and 1 good answer");
            return;
        } else {
            labelMaxTotalAnswer.setVisible(false);
        }
        answer.setQuestionId(questionId);

        /// Send to DAO
        selectedAnswer = dao.addAnswerToQuestion(answer);
        questionFxListView.getSelectionModel().getSelectedItem().addAnswer(selectedAnswer);
        textAnswer.clear();
        refreshQuestionSubSection();
    }


    public void answerDeleteBtnAction() {
        if (answersListView.getSelectionModel().getSelectedItem() != null) {
            Answer a = answersListView.getSelectionModel().getSelectedItem();
            if (dao.deleteAnswer(a)) {
                questionFxListView.getSelectionModel().
                        getSelectedItem().
                        removeAnswer(answersListView.getSelectionModel().getSelectedItem());

                refreshQuestionSubSection();
            }

        }
    }

    public boolean limitAnswers(Answer lastAnswer) {
        ObservableList<Answer> answers = questionFxListView.getSelectionModel().getSelectedItem().getAnswers();
        int coutGood = (lastAnswer.isCorrect() ? 1 : 0);
        for (Answer a : answers) {
            if (a.isCorrect()) {
                coutGood++;
            }
        }
        if(answers.size() <= 3 && coutGood <= 1){
            labelTotalAnswers.setText("Aantal antwoorden: " + answers.size());
            labelTotalGoodAnswers.setText("Aantal juist: " + coutGood);
            return true;
        }
        return false;
    }

    public void newQuizAction() {
        int course_id = courseTable.getSelectionModel().getSelectedItem().getDbId();
        String quizName = textQuizName.getText();
        String sd = textSuccessDefinite.getText();
        String tl = textTimeLimit.getText();
        if (!sd.equals("") && !tl.equals("") && !quizName.equals("")) {
            double succesDefinite = Double.parseDouble(sd);
            int timeLimit = Integer.parseInt(tl);
            Quiz q = new Quiz(quizName, succesDefinite);
            q.setIdcourse(course_id);
            q.setTimeLimit(timeLimit);
            q = dao.addQuiz(q);
            if (q != null) {
                courseTable.getSelectionModel().getSelectedItem().addQuiz(q);
                selectedCourse = courseTable.getSelectionModel().getSelectedItem();
                refeshCourseSubSections();

            }
        } else {
            new Alert(Alert.AlertType.ERROR, "AUB vull alle benodigde informatie").show();
        }

    }

    public void deletQuizSelectedAction() {
        boolean r = AlertHelper.confirmationDialog("Wilt u zeker Quiz " +
                quizFxListView.getSelectionModel().getSelectedItem().getName()
                + " verwijderen ?");
        if (r) {
            dao.deleteQuiz(quizFxListView.getSelectionModel().getSelectedItem().getQuizObject());
            courseTable.getSelectionModel().getSelectedItem().removeQuiz(quizFxListView.getSelectionModel().getSelectedItem().getQuizObject());
            emptyFieldsAndSelected();
            refeshCourseSubSections();
        }
    }
}
