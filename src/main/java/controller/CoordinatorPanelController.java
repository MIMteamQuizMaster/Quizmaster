package controller;

import controller.fx.AnswerFx;
import controller.fx.CourseFx;
import controller.fx.QuestionFx;
import controller.fx.QuizFx;
import database.mysql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import launcher.Main;
import model.Answer;
import model.Question;
import model.Quiz;
import model.User;

import java.util.List;

import static controller.fx.ObjectConvertor.*;

public class CoordinatorPanelController {
    public TitledPane quizPane;
    public TitledPane questionPane;
    public TitledPane answerPane;
    public Button btnQuizPanelOpen;
    public Button btnQuestionPanelOpen;
    public Button btnAnswerPanelOpen;

    public CheckBox cBoxAnswerIsCorrect;

    public Label labelCourse;
    public Label labelTotalQuizen;

    public TextArea textAnswer;
    public TextField textQuizName;
    public TextField textSuccessDefinite;
    public TextArea textQuestion;
    public TextField textTimeLimit;

    public TableView<QuizFx> quizzesTable;
    public TableColumn<QuizFx, String> colNameQuizTable;
    public TableColumn<QuizFx, Double> colSuccessQuizTable;
    public TableColumn<QuizFx, Integer> colTimeLimitQuizTable;
    public TableColumn<QuizFx, Void> col_Delete_Quiz;

    public TableView<QuestionFx> questionTable;
    public TableColumn<QuestionFx, String> colQuestion;
    public TableColumn<QuestionFx, Integer> colTotalAnswer;
    public TableColumn<QuestionFx, Integer> colTotatlGood;
    public TableColumn<QuestionFx, Void> colActionQuestion;

    public TableView<AnswerFx> answerTable;
    public TableColumn<AnswerFx, String> col_Answer;
    public TableColumn<AnswerFx, Boolean> col_validity;
    public TableColumn<AnswerFx, Void> col_Delete_Answer;

    @FXML
    private TableView<CourseFx> courseTable;
    @FXML
    private TableColumn<CourseFx, String> col_sdate;
    @FXML
    private TableColumn<CourseFx, String> col_edate;
    @FXML
    private TableColumn<CourseFx, String> col_course_name;

    private CoordinatorDAO coordinatorDAO;
    private QuizDAO quizDAO;
    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;

    private CourseFx selectedCourse;
    private QuestionFx selectedQuestion;
    private QuizFx selectedQuiz;
    private AnswerFx selectedAnswer;

    public void initialize() {
        DBAccess dBaccess = Main.getDBaccess();
        User loggedInUser = (User) Main.getPrimaryStage().getUserData();
        this.coordinatorDAO = new CoordinatorDAO(dBaccess);
        this.quizDAO = new QuizDAO(dBaccess);
        this.questionDAO = new QuestionDAO(dBaccess);
        this.answerDAO = new AnswerDAO(dBaccess);
        this.coordinatorDAO.setCoordinator(loggedInUser);
        System.out.println("initialize");
        fillCoursesTable();
        emptyFieldsAndSelected();
    }

    /**
     * @author M.J. Moshiri
     * <p>
     * Empty and clear fields and selected objects to skip faulty usage of fields
     * and also check all the TitlePanes to be closed
     */
    private void emptyFieldsAndSelected() {
        //Close all open pane
        if (quizPane.isExpanded()) {
            expandTitledPane(quizPane);
        }
        if (questionPane.isExpanded()) {
            expandTitledPane(questionPane);
        }
        if (answerPane.isExpanded()) {
            expandTitledPane(answerPane);
        }
        btnAnswerPanelOpen.setDisable(true);
        btnQuestionPanelOpen.setDisable(true);
        btnQuizPanelOpen.setDisable(true);
        clearAll();
    }

    /**
     * @author M.J. Moshiri
     * empty all fields and selected items to skip faulty usage of them
     */
    private void clearAll() {
        selectedCourse = null;
        selectedQuiz = null;
        selectedQuestion = null;
        selectedAnswer = null;
        // empty sub quiz lists
        answerTable.getItems().clear();
        quizzesTable.getItems().clear();
        questionTable.getItems().clear();
        // empty text area and checkbox
        textQuestion.clear();
        textAnswer.clear();
        textTimeLimit.clear();
        textSuccessDefinite.clear();
        textQuizName.clear();
    }


    /**
     * @author M.J. Moshiri
     * <p>
     * Fill the Course Table view Using CourseFX objects
     */
    private void fillCoursesTable() {
        ObservableList<CourseFx> courses;
        courses = convertCoursetoCourseFX(coordinatorDAO.getMyCourses());
        col_course_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_sdate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_edate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        courseTable.setItems(courses);
    }

    /**
     * @author M.J. Moshiri
     *
     * Fill the Quiz Table view using QuizFX
     */
    private void fillQuizTable() {
        ObservableList<QuizFx> quizFxes;
        // fil table accodring to selectedCourse
        quizFxes = convertQuizToQuizFX(quizDAO.getQuizOfCourse(selectedCourse.getCourseObject()));
        colNameQuizTable.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSuccessQuizTable.setCellValueFactory(cellData -> cellData.getValue().succsesDefinitionProperty().asObject());
        colTimeLimitQuizTable.setCellValueFactory(cellData -> cellData.getValue().timeLimitProperty().asObject());
        addActionBtnToQuizTable();
        quizzesTable.setItems(quizFxes);

        this.labelCourse.setText(selectedCourse.getName());
        this.labelTotalQuizen.setText(String.valueOf(quizFxes.size()));
        btnQuizPanelOpen.setDisable(false);

    }

    /**
     * @author M.J. Moshiri
     *
     * Add 2 btn to Quiz table for delet or edit Row
     */
    private void addActionBtnToQuizTable() {
        col_Delete_Quiz.setCellFactory(cellData -> new TableCell<>() {
            private final Button editButton = new Button("bijwerken");
            private final Button deleteButton = new Button("Verwijderen");
            private final VBox pane = new VBox(deleteButton, editButton);

            {
                deleteButton.setOnAction(event -> {
                    quizzesTable.getSelectionModel().select(getIndex());
                    selectedQuiz = getTableView().getItems().get(getIndex());
                    deleteQuiz();
                });

                editButton.setOnAction(event -> {
                    quizzesTable.getSelectionModel().select(getIndex());
                    selectedQuiz = getTableView().getItems().get(getIndex());
                    editQuizPreSetup();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    /**
     * @author M.J. Moshiri
     *
     * Fill the Question Table view using QuestionFx
     */
    private void fillQuestionTable() {
        ObservableList<QuestionFx> questionFxes;
        // fil table accodring to selectedQuiz
        List<Question> qList = questionDAO.getQuestions(selectedQuiz.getQuizObject());
        questionFxes = convertQuestionToQuestionFX(qList);
        colQuestion.setCellValueFactory(cellData -> cellData.getValue().questionProperty());
        colTotalAnswer.setCellValueFactory(cellData -> cellData.getValue().getTotalAnswer().asObject());
        colTotatlGood.setCellValueFactory(cellData -> cellData.getValue().getTotalGoodAnswer().asObject());
        addActionBtnToQuestionTable();
        questionTable.setItems(questionFxes);

        btnQuestionPanelOpen.setDisable(false);
    }

    /**
     * @author M.J. Moshiri
     *
     * Add edit and delete btn to questionTable
     */
    private void addActionBtnToQuestionTable() {
        colActionQuestion.setCellFactory(cellData -> new TableCell<>() {
            private final Button editButton = new Button("bijwerken");
            private final Button deleteButton = new Button("Verwijderen");
            private final VBox pane = new VBox(deleteButton, editButton);


            {
                pane.setPrefWidth(90);
                editButton.setPrefWidth(90);
                deleteButton.setPrefWidth(90);
                deleteButton.setOnAction(event -> {
                    questionTable.getSelectionModel().select(getIndex());
                    selectedQuestion = getTableView().getItems().get(getIndex());

                    deleteQuestion();
                });

                editButton.setOnAction(event -> {
                    questionTable.getSelectionModel().select(getIndex());
                    selectedQuestion = getTableView().getItems().get(getIndex());

                    editQuestionPreSetup();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    /**
     * @author M.J. Moshiri
     *
     * Fill the Answer table using AnswerFX according to the selectedQuestion
     * which has prevoisly chosen in OnQuestionTable click
     */
    private void fillAnswerTable() {
        // fill questions
        ObservableList<AnswerFx> answerFxes;
        // fil table according to selectedQuestion
        answerFxes = convertAnswerToAnswerFX(answerDAO.getAllAnswers(this.selectedQuestion.getQuestionObject()));
        col_Answer.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        col_validity.setCellValueFactory(cellData -> cellData.getValue().isCorrectProperty().asObject());
        addColorOnBooleanToAnswerTable();
        addBtnToAnswerTable();
        answerTable.setItems(answerFxes);
        btnAnswerPanelOpen.setDisable(false);

    }

    /**
     * @author M.J. Moshiri
     *
     * Add color to row if appropriate to answer validity
     * green if the answer is a correct one and red if its not correct
     */
    private void addColorOnBooleanToAnswerTable() {
        col_validity.setCellFactory(cellData -> new TableCell<>() {

            @Override
            protected void updateItem(Boolean aBoolean, boolean b) {
                super.updateItem(aBoolean, b);

                if (!b) {
                    // Get fancy and change color based on data
                    if (aBoolean) {
                        this.getTableRow().setStyle("-fx-background-color: #ccffcc");
                    } else {
                        this.getTableRow().setStyle("-fx-background-color: #f6a3a3");
                    }

                }

            }
        });
    }

    /**
     * @author M.J. Moshiri
     *
     * Add edit and delete btn to answer table
     */
    private void addBtnToAnswerTable() {
        col_Delete_Answer.setCellFactory(cellData -> new TableCell<>() {
            private final Button editButton = new Button("bijwerken");
            private final Button deleteButton = new Button("wissen");
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                deleteButton.setOnAction(event -> {
                    answerTable.getSelectionModel().select(getIndex());
                    selectedAnswer = getTableView().getItems().get(getIndex());
                    deleteAnswer();
                });

                editButton.setOnAction(event -> {
                    answerTable.getSelectionModel().select(getIndex());
                    selectedAnswer = getTableView().getItems().get(getIndex());
                    editAnswerPreSetup();

                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    /**
     * @author M.J. Moshiri
     *
     * it will refresh the quiz table according to the selected course and if no course has been selected
     * it will clean and empty all fields
     * also close the panel if it is open
     */
    private void refreshQuizTable() {
        if (selectedCourse != null) {
            fillQuizTable();

        } else {
            emptyFieldsAndSelected();
        }
        if (quizPane.isExpanded()) {
            expandTitledPane(quizPane);
        }

        textQuizName.clear();
        textSuccessDefinite.clear();
        textTimeLimit.clear();
    }

    /**
     * @author M.J. Moshiri
     *
     * it will refresh the Question table according to the selected quiz and if no quiz has been selected
     * it will clean the quiz table
     * also close the panel if it is open
     */
    private void refreshQuestionTable() {
        if (selectedQuiz != null) {
            fillQuestionTable();

        } else {
            questionTable.getItems().clear();

        }
        if (questionPane.isExpanded()) {
            expandTitledPane(questionPane);
        }

        textQuestion.clear();

    }

    /**
     * @author M.J. Moshiri
     *
     * it will refresh the answer table according to the selected question and if no question has been selected
     * it will clean the answer table
     * also close the panel if it is open
     */
    private void refreshAnswerTable() {
        if (selectedQuestion != null) {
            fillAnswerTable();
        } else {
            answerTable.getItems().clear();
        }
        if (answerPane.isExpanded()) {
            expandTitledPane(answerPane);
        }
        textAnswer.clear();
    }

    /**
     * @author M.J. Moshiri
     *
     * it will open the quizPane for editing the selected quiz
     */
    private void editQuizPreSetup() {
        /// open panel  and fill selected item valuses to fields
        textQuizName.setText(selectedQuiz.getName());
        textSuccessDefinite.setText(String.valueOf(selectedQuiz.getSuccsesDefinition()));
        textTimeLimit.setText(String.valueOf(selectedQuiz.getTimeLimit()));
        expandTitledPane(quizPane);

    }

    /**
     * @author M.J. Moshiri
     *
     * Open the pane and set qustion ready to edit and save
     */
    private void editQuestionPreSetup() {
        textQuestion.setText(selectedQuestion.getQuestion());
        expandTitledPane(questionPane);
    }

    /**
     * @author M.J. Moshiri
     *
     * it will open the Pane and fill the data from the selected answer to the correct field
     */
    private void editAnswerPreSetup() {
        expandTitledPane(answerPane);
        textAnswer.setText(selectedAnswer.getAnswer());
        cBoxAnswerIsCorrect.setSelected(selectedAnswer.isIsCorrect());
    }

    /**
     * @author M.J. Moshiri
     *
     * Action method of Click on Course table
     * Which will fill add quizzes of selected course to de ListView
     */
    public void courseTableOnClick() {
        if (courseTable.getSelectionModel().getSelectedItem() != null) {
            emptyFieldsAndSelected();
            selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            refreshQuizTable();

        }
    }

    /**
     * @author M.J. Moshiri
     *
     * Quiz Table select function which is on table click action
     * it checks if the user has chosen a valid row then take the selected row object as selected quiz
     * and then fill the question table according to the selected row
     *
     */
    public void quizTableOnClick() {
        if (quizzesTable.getSelectionModel().getSelectedItem() != null) {
            selectedQuiz = quizzesTable.getSelectionModel().getSelectedItem();
            refreshQuestionTable();
            selectedQuestion = null;
            refreshAnswerTable();
        }
        if (quizPane.isExpanded()) {
            expandTitledPane(quizPane);
        }

    }

    /**
     * @author M.J. Moshiri
     * Question Table select function
     * which is a on table click action ,it will check if the user has chosen a valid row
     * then it will add the selected object to the selected question and also show and fill
     * the answer table according to the selected question
     */
    public void questionTableOnClick() {
        if (questionTable.getSelectionModel().getSelectedItem() != null) {
            // set selected quiz
            this.selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
            refreshAnswerTable();
        }
        if (questionPane.isExpanded()) {
            expandTitledPane(questionPane);
        }

    }

    /**
     * @author M.J. Moshiri
     * Answer table select funtion which is a on table click action
     * it will check if the user has selected a valid row then it will set the selected row
     * as selected answer for furthur actions
     */
    public void answerTableOnClick() {
        if (answerTable.getSelectionModel().getSelectedItem() != null) {
            selectedAnswer = answerTable.getSelectionModel().getSelectedItem();
        }
        if (answerPane.isExpanded()) {
            expandTitledPane(answerPane);
        }
    }

    /**
     * @author M.J. Moshiri
     *
     * Limit adding new answer if the given answer is extra or its the second correct answer
     * it take the given answer and count the answers that has already been dedicated to the question
     * and also count the number of corect answers and it the new giving answer is exceeding the limit it will
     * return false
     * @param lastAnswer is the answer which the user is trying to add
     * @return a boolean define possibility of adding given answer
     */
    public boolean limitAnswers(Answer lastAnswer) {
        List<Answer> answerList = selectedQuestion.getAnswers();
        ObservableList<Answer> answers = FXCollections.observableArrayList(answerList);
        int coutGood = (lastAnswer.isCorrect() ? 1 : 0);
        for (Answer a : answers) {
            if (a.isCorrect()) {
                coutGood++;
            }
        }
        return answers.size() <= 3 && coutGood <= 1;
    }

    /**
     * @author M.J. Moshiri
     *
     * Delete the selected quiz if the user Approve the confirmation
     */
    public void deleteQuiz() {
        QuizFx quizFx = quizzesTable.getSelectionModel().getSelectedItem();
        boolean r = AlertHelper.confirmationDialog("Wilt u zeker Quiz " +
                quizzesTable.getSelectionModel().getSelectedItem().getName()
                + " verwijderen ?");
        if (r) {
            coordinatorDAO.deleteQuiz(quizFx.getQuizObject());
            refreshQuizTable();
            selectedQuiz = null;
        }
    }

    /**
     * @author M.J. Moshiri
     *
     * Delete the selected Question if the user accept the confirmation
     */
    private void deleteQuestion() {
        boolean r = AlertHelper.confirmationDialog("Wilt u zeker Question " +
                selectedQuestion.getQuestion()
                + " verwijderen ?");
        if (r) {

            coordinatorDAO.deleteQuestion(selectedQuestion.getQuestionObject());
            selectedQuestion = null;
            refreshQuestionTable();
        }
    }

    /**
     * @author M.J. Moshiri
     *
     * Delete the selected Answer if the user accept the confirmation
     */
    private void deleteAnswer() {
        boolean r = AlertHelper.confirmationDialog("Wilt u zeker Answer" +
                selectedAnswer.getAnswer()
                + " verwijderen ?");
        if (r) {
            coordinatorDAO.deleteAnswer(selectedAnswer.getAnswerObject());
            selectedAnswer = null;
            int a = questionTable.getSelectionModel().getSelectedIndex();
            refreshQuestionTable();
            questionTable.getSelectionModel().select(a);
            refreshAnswerTable();
        }
    }

    /**
     * @author M.J. Moshiri
     * Save or update quiz
     * for new quiz it add an id of 0 to quiz object so that the dao take the INSERT query
     * otherwise it will take the update query with the valid id that is stored inside the object
     */
    public void btnSaveQuizAction() {
        if (selectedCourse != null) {
            int course_id = courseTable.getSelectionModel().getSelectedItem().getDbId();
            String quizName = textQuizName.getText();
            String sd = textSuccessDefinite.getText();
            String tl = textTimeLimit.getText();
            if (!sd.equals("") && !quizName.equals("")) {
                double succesDefinite = Double.parseDouble(sd);
                int timeLimit = Integer.parseInt(tl);
                Quiz quiz;
                if (this.selectedQuiz == null) {
                    quiz = quizDAO.saveQuiz(new Quiz(quizName, succesDefinite, 0, course_id, timeLimit));
                    // new QUiz
                } else {
                    // update Quiz
                    selectedQuiz.setName(quizName);
                    selectedQuiz.setSuccsesDefinition(succesDefinite);
                    selectedQuiz.setTimeLimit(timeLimit);
                    quiz = quizDAO.saveQuiz(selectedQuiz.getQuizObject());
                }
                if (quiz != null) {
                    courseTable.getSelectionModel().getSelectedItem().addQuiz(quiz);
                    refreshQuizTable();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "AUB vull alle benodigde informatie").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "AUB kies een cursus").show();
        }
    }

    /**
     * @author M.J. Moshiri
     * it saves the question in case of update and new
     * for new question it add an id of 0 to question object so that the dao take the INSERT query
     * otherwise it will take the update query with the valid id that is stored inside the object
     */
    public void btnSaveQuestionAction() {
        Question question;
        String questionString = textQuestion.getText();
        if (questionString.equals("")) {
            new Alert(Alert.AlertType.ERROR, "Schrijf de vraag aub").show();
            return;
        }
        if (this.selectedQuestion == null) {
            // if the user has chose new item then the selectionQuestion is empty so
            //  we creat an object with id 0 so in dao we can use it as a trick to call INSERT query
            // create new Question Object and send it to dao for saving function
            question = new Question(questionString);
            question.setQuizId(this.selectedQuiz.getIdquiz());
            question.setQuestionId(0);
            question = questionDAO.saveQuestion(question); // save the question

        } else {
            /// UPDATE ITEM so we send update the selected question and send it to dao save method
            selectedQuestion.setQuestion(questionString);
            question = questionDAO.saveQuestion(this.selectedQuestion.getQuestionObject());
        }
        if (question != null) { // after succesfull add we disable the editMode
            refreshQuestionTable();
            refreshAnswerTable();
            selectedQuestion = new QuestionFx(question);
        }
    }

    /**
     * send the answer to the the dao
     * if the user has chosen the new Answer the function will add a 0 id to answer so the DAO knows that
     * it is a new answer but in other case which the answer would have a valid id the dao will run the update query
     *
     * @author M.J. Moshiri
     */
    public void btnSaveAnswerAction() {
        String answerString = textAnswer.getText();
        boolean isCorrect = cBoxAnswerIsCorrect.isSelected();
        Answer answer;
        if (selectedAnswer != null) {
            //update
            answer = selectedAnswer.getAnswerObject();
            answer.setAnswer(answerString);
            answer.setCorrect(isCorrect);
        } else {
            // new
            int questionId = this.selectedQuestion.getQuestionId();
            answer = new Answer(isCorrect, answerString);
            answer.setQuestionId(questionId);
            answer.setId(0);
        }
        if (!limitAnswers(answer)) {
            new Alert(Alert.AlertType.ERROR, "max 4 answer and 1 good answer").show();
            return;
        }
        selectedQuestion.addAnswer(answer);
        /// Send to DAO
        answer = answerDAO.saveAnswer(answer);
        if (answer != null) { // after succesfull add we disable the editMode
            refreshQuestionTable();
            refreshAnswerTable();
            textAnswer.clear();
        }
    }


    /**
     * Close the New Quiz Pane and empty the fields
     *
     */
    public void cancelQuizBtnAction() {
        btnQuizPanelOpenAction();
        textQuizName.clear();
        textTimeLimit.clear();
        textSuccessDefinite.clear();
    }

    /**
     * @author M.J. Moshiri
     * <p>
     * cancel the workflow of adding new Question and clear the field and also close the pane
     */
    public void cancelQuestionBtnAction() {
        textQuestion.clear();
        expandTitledPane(questionPane);
    }

    /**
     * @author M.J. Moshiri
     * cancel the workflow of adding new answer and clear the field and also close the pane
     */
    public void cancelNewAnswerAction() {
        textAnswer.clear();
        cBoxAnswerIsCorrect.setSelected(false);
        expandTitledPane(answerPane);
    }

    /**
     * @author M.J. Moshiri
     * cancel the workflow of adding new Quiz and clear the field and also close the pane
     */
    public void btnQuizPanelOpenAction() {
        selectedQuiz = null;
        textTimeLimit.clear();
        textQuizName.clear();
        textSuccessDefinite.clear();
        expandTitledPane(quizPane);
    }

    /**
     * @author M.J. Moshiri
     * Opens the pane for adding new Question also clears the field
     */
    public void btnQuestionPanelAction() {
        selectedQuestion = null;
        textQuestion.clear();
        expandTitledPane(questionPane);
    }

    /**
     * @author M.J. Moshiri
     * <p>
     * Opens the titlepane for adding new answer to the selected Question
     */
    public void btnAnswerPanelOpenAction() {
        selectedAnswer = null;
        textAnswer.clear();
        cBoxAnswerIsCorrect.setSelected(false);
        expandTitledPane(answerPane);
    }

    /**
     * @param selectedPane is the pane that we need to change to expand or close it
     * @author M.J. Moshiri
     * <p>
     * this method is responsible for opening or closing a titledPane and changing the btn color and text
     * inside
     */
    private void expandTitledPane(TitledPane selectedPane) {
        HBox n = (HBox) selectedPane.getGraphic();
        Button b = new Button();
        if (n.getChildren().get(1) instanceof Button) {
            b = (Button) n.getChildren().get(1);
        }
        if (selectedPane.isExpanded()) {
            selectedPane.setCollapsible(true);
            selectedPane.setExpanded(false);
            selectedPane.setCollapsible(false);
            b.setText("Nieuw");
            b.setStyle("-fx-background-color: green");
        } else {
            selectedPane.setCollapsible(true);
            selectedPane.setExpanded(true);
            selectedPane.setCollapsible(false);
            b.setText("Afsluiten");
            b.setStyle("-fx-background-color: red");
        }
    }

}
