package controller;

import controller.fx.AnswerFx;
import controller.fx.CourseFx;
import controller.fx.QuestionFx;
import controller.fx.QuizFx;
import database.mysql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    public TableColumn<QuestionFx, Void> colDelQuestion;
    public TableColumn<QuestionFx, Void> colEditQuestion;

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
    private boolean questionEditMode;

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
     * Empty and clear fields and selected objects
     */
    private void emptyFieldsAndSelected() {
        selectedCourse = null;
        selectedQuiz = null;
        selectedQuestion = null;
        selectedAnswer = null;

        //Close all open pane
        if(quizPane.isExpanded()){
            expandTitledPane(new ActionEvent(),quizPane);
        }
        if(questionPane.isExpanded())
        {
            expandTitledPane(new ActionEvent(),questionPane);
        }
        if(answerPane.isExpanded()){
            expandTitledPane(new ActionEvent(),answerPane);
        }

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

        btnAnswerPanelOpen.setDisable(true);
        btnQuestionPanelOpen.setDisable(true);
        btnQuizPanelOpen.setDisable(true);
    }


    /**
     * Fill the Course Table view Using CoursFX objects
     */
    public void fillCoursesTable() {
        ObservableList<CourseFx> courses;
        courses = convertCoursetoCourseFX(coordinatorDAO.getMyCourses());
        col_course_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_sdate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_edate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        courseTable.setItems(courses);
    }

    /**
     * Fill the Quiz Table view using QuizFX
     */
    public void fillQuizTable() {
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
     * Add 2 btn to Quiz table for delet or edit Row
     */
    private void addActionBtnToQuizTable() {
        col_Delete_Quiz.setCellFactory(cellData -> new TableCell<QuizFx, Void>() {
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
     * Add edit and delete btn to questionTable
     */
    private void addActionBtnToQuestionTable() {
        colDelQuestion.setCellFactory(cellData -> new TableCell<QuestionFx, Void>() {
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
     * Fill the Answer table using AnswerFX
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
     * Add color to row if appropriate to answer validity
     */
    private void addColorOnBooleanToAnswerTable() {
        col_validity.setCellFactory(cellData -> new TableCell<AnswerFx, Boolean>() {

            @Override
            protected void updateItem(Boolean aBoolean, boolean b) {
                super.updateItem(aBoolean, b);

                if (!b) {
                    // Get fancy and change color based on data
                    if (aBoolean)
                    {
                        this.getTableRow().setStyle("-fx-background-color: #ccffcc");
                    }else {
                        this.getTableRow().setStyle("-fx-background-color: #f6a3a3");
                    }

                }

            }
        });
    }

    /**
     * Add edit and delete btn to answer table
     */
    private void addBtnToAnswerTable() {
        col_Delete_Answer.setCellFactory(cellData -> new TableCell<AnswerFx, Void>() {
            private final Button editButton = new Button("bijwerken");
            private final Button deleteButton = new Button("wissen");
            private final HBox pane = new HBox(editButton,deleteButton);

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
     * change or refresh the GUI in sub section of Course
     */
    public void refreshQuizTable() {
        if (selectedCourse != null) {
            fillQuizTable();

        } else {
            emptyFieldsAndSelected();
        }
        if(quizPane.isExpanded()){
            expandTitledPane(new ActionEvent(), quizPane);
        }

        textQuizName.clear();
        textSuccessDefinite.clear();
        textTimeLimit.clear();
    }

    public void refreshQuestionTable() {
        if (selectedQuiz != null) {
            fillQuestionTable();

        } else {
            questionTable.getItems().clear();

        }
        if(questionPane.isExpanded()){
            expandTitledPane(new ActionEvent(), questionPane);
        }

        textQuestion.clear();

    }

    private void refreshAnswerTable() {
        if (selectedQuestion != null) {
            fillAnswerTable();
        } else {
            answerTable.getItems().clear();
        }
        if(answerPane.isExpanded()){
            expandTitledPane(new ActionEvent(), answerPane);
        }
        textAnswer.clear();
    }

    private void editQuizPreSetup() {
        /// open panel  and fill selected item valuses to fields
        textQuizName.setText(selectedQuiz.getName());
        textSuccessDefinite.setText(String.valueOf(selectedQuiz.getSuccsesDefinition()));
        textTimeLimit.setText(String.valueOf(selectedQuiz.getTimeLimit()));
        expandTitledPane(new ActionEvent(), quizPane);

    }

    /**
     * Open the pane and set qustion ready to edit and save
     */
    private void editQuestionPreSetup() {
        textQuestion.setText(selectedQuestion.getQuestion());
        expandTitledPane(new ActionEvent(), questionPane);
    }

    private void editAnswerPreSetup() {
        expandTitledPane(new ActionEvent(), answerPane);
        textAnswer.setText(selectedAnswer.getAnswer());
        cBoxAnswerIsCorrect.setSelected(selectedAnswer.isIsCorrect());
    }

    /**
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
     * Quiz Table select function
     */
    public void quizTableOnClick(MouseEvent mouseEvent) {
        if (quizzesTable.getSelectionModel().getSelectedItem() != null) {
            selectedQuiz = quizzesTable.getSelectionModel().getSelectedItem();
            refreshQuestionTable();
            selectedQuestion=null;
            refreshAnswerTable();
        }
        if(quizPane.isExpanded()){
            expandTitledPane(new ActionEvent(),quizPane);
        }

    }

    /**
     * Question Table select function
     */
    public void questionTableOnClick(MouseEvent mouseEvent) {
        if (questionTable.getSelectionModel().getSelectedItem() != null) {
            // set selected quiz
            this.selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
            refreshAnswerTable();
        }
        if(questionPane.isExpanded())
        {
            expandTitledPane(new ActionEvent(),questionPane);
        }

    }

    /**
     * Answer table select funtion
     */

    public void answerTableOnClick(MouseEvent mouseEvent) {
        if (answerTable.getSelectionModel().getSelectedItem() != null) {
            selectedAnswer = answerTable.getSelectionModel().getSelectedItem();
        }
        if(answerPane.isExpanded()){
            expandTitledPane(new ActionEvent(),answerPane);
        }
    }

    /**
     * Limit adding new answer if the given answer is extra or its the second correct answer
     *
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
     * Delete the quiz if the user Approve the confirmation
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
     * Delete the Question if the user accept the confirmation
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
     * Delete Answer if the user accept the confirmation
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
     * Save or update quiz
     *
     * @param actionEvent
     */
    public void btnSaveQuizAction(ActionEvent actionEvent) {
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
                    quiz = new Quiz(quizName, succesDefinite);
                    quiz.setIdcourse(course_id);
                    quiz.setTimeLimit(timeLimit);
                    quiz.setIdquiz(0);
                    quiz = quizDAO.saveQuiz(quiz);
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
//                    expandTitledPane(new ActionEvent(), quizPane);

                }
            } else {
                new Alert(Alert.AlertType.ERROR, "AUB vull alle benodigde informatie").show();
            }
        } else {

            new Alert(Alert.AlertType.ERROR, "AUB kies een cursus").show();
        }
    }

    /**
     * Save or update Question
     *
     * @param actionEvent
     */
    public void btnSaveQuestionAction(ActionEvent actionEvent) {
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
     * add answer to the databes according to the last selected question
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
     * @param actionEvent
     */
    public void cancelQuizBtnAction(ActionEvent actionEvent) {
        btnQuizPanelOpenAction(new ActionEvent());
        textQuizName.clear();
        textTimeLimit.clear();
        textSuccessDefinite.clear();
    }

    /**
     * Cancel editing end disable the editMode
     *
     * @param actionEvent
     */
    public void cancelQuestionBtnAction(ActionEvent actionEvent) {
        textQuestion.clear();
        expandTitledPane(new ActionEvent(), questionPane);
    }

    public void cancelNewAnswerAction(ActionEvent actionEvent) {
        textAnswer.clear();
        cBoxAnswerIsCorrect.setSelected(false);
        expandTitledPane(new ActionEvent(), answerPane);
    }

    public void btnQuizPanelOpenAction(ActionEvent actionEvent) {
        selectedQuiz = null;
        textTimeLimit.clear();
        textQuizName.clear();
        textSuccessDefinite.clear();
        expandTitledPane(actionEvent, quizPane);
    }

    public void btnQuestionPanelAction(ActionEvent actionEvent) {
        selectedQuestion = null;
        textQuestion.clear();
        expandTitledPane(actionEvent, questionPane);
    }

    public void btnAnswerPanelOpenAction(ActionEvent actionEvent) {
        selectedAnswer = null;
        textAnswer.clear();
        cBoxAnswerIsCorrect.setSelected(false);
        expandTitledPane(actionEvent, answerPane);
    }

    private void expandTitledPane(ActionEvent actionEvent, TitledPane selectedPane) {
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
        actionEvent.consume();
    }

}
