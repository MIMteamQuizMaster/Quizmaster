package controller;

import controller.fx.*;
import database.mysql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import launcher.Main;
import model.*;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.util.List;

import static controller.fx.ObjectConvertor.*;

public class CoordinatorPanelController {

    public Button btnNewQuestion;
    public Button btnNewAnswer;
    public Button btnNewQuiz;

    public Label labelCourse;
    public Label labelTotalQuizen;

    public TableView<QuizFx> quizzesTable;
    public TableColumn<QuizFx, String> col_NameQuiz;
    public TableColumn<QuizFx, Double> colSuccessQuizTable;
    public TableColumn<QuizFx, Integer> colTimeLimitQuizTable;
    public TableColumn<QuizFx, Void> col_Action_Quiz;

    public TableView<QuestionFx> questionTable;
    public TableColumn<QuestionFx, String> colQuestion;
    public TableColumn<QuestionFx, Integer> colTotalAnswer;
    public TableColumn<QuestionFx, Integer> colTotatlGood;
    public TableColumn<QuestionFx, Void> colActionQuestion;

    public TableView<AnswerFx> answerTable;
    public TableColumn<AnswerFx, String> col_Answer;
    public TableColumn<AnswerFx, Boolean> col_validity;
    public TableColumn<AnswerFx, Void> col_Delete_Answer;

    public VBox leftVBox;
    public VBox rightVBox;

    public HBox rootHPane;
    public HBox quizPaneHbox;
    public HBox questionPaneHbox;
    public HBox answerPaneHbox;
    public AnchorPane rootPane;
    private GlyphFont glyphFont;


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
        User loggedInUser = Main.getLoggedInUser();
        this.coordinatorDAO = new CoordinatorDAO(dBaccess);
        this.quizDAO = new QuizDAO(dBaccess);
        this.questionDAO = new QuestionDAO(dBaccess);
        this.answerDAO = new AnswerDAO(dBaccess);
        this.coordinatorDAO.setCoordinator(loggedInUser);
        System.out.println("initialize");
        fillCoursesTable();
        clearAll();
        glyphFont = GlyphFontRegistry.font("FontAwesome");
        rootPane.widthProperty().addListener(data -> bindSizeProperty());
    }

    /**
     * @author M.J. Moshiri
     * Bind de width property of tables for good responsive view and change in column sizes
     * by resizing the stage
     */
    private void bindSizeProperty() {
        leftVBox.prefWidthProperty().bind(Main.getPrimaryStage().widthProperty().divide(4));
        courseTable.prefWidthProperty().bind(leftVBox.widthProperty().subtract(10));
        col_Answer.minWidthProperty().bind(answerTable.widthProperty().multiply(0.87));
        colQuestion.minWidthProperty().bind(questionTable.widthProperty().multiply(0.75));
        col_NameQuiz.minWidthProperty().bind(quizzesTable.widthProperty().multiply(0.35));
        colSuccessQuizTable.minWidthProperty().bind(quizzesTable.widthProperty().multiply(0.35));
        col_course_name.minWidthProperty().bind(courseTable.widthProperty().multiply(0.35));

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

        answerTable.getItems().clear();
        quizzesTable.getItems().clear();
        questionTable.getItems().clear();
        btnNewQuiz.setDisable(true);
        btnNewQuestion.setDisable(true);
        btnNewAnswer.setDisable(true);
    }


    /**
     * @author M.J. Moshiri
     * <p>
     * Fill the Course Table view Using CourseFX objects
     */
    private void fillCoursesTable() {
        ObservableList<CourseFx> courses;
        courses = convertCoursetoCourseFX(coordinatorDAO.getMyCourses(false));
        col_course_name.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_sdate.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_edate.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

        col_course_name.prefWidthProperty().bind(courseTable.widthProperty().divide(2));
        col_sdate.prefWidthProperty().bind(courseTable.widthProperty().divide(4));
        col_edate.prefWidthProperty().bind(courseTable.widthProperty().divide(4));

        courseTable.setItems(courses);
        courseTable.refresh();
    }

    /**
     * @author M.J. Moshiri
     * <p>
     * Fill the Quiz Table view using QuizFX
     */
    private void fillQuizTable() {
        ObservableList<QuizFx> quizFxes;
        // fil table accodring to selectedCourse
        quizFxes = convertQuizToQuizFX(quizDAO.getQuizOfCourse(selectedCourse.getCourseObject(), false));
        col_NameQuiz.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colSuccessQuizTable.setCellValueFactory(cellData -> cellData.getValue().succsesDefinitionProperty().asObject());
//        colTimeLimitQuizTable.setCellValueFactory(cellData -> cellData.getValue().timeLimitProperty().asObject());
        addActionBtnToQuizTable();

        col_course_name.prefWidthProperty().bind(quizzesTable.widthProperty().divide(3));
        colSuccessQuizTable.prefWidthProperty().bind(quizzesTable.widthProperty().divide(3));
        col_Action_Quiz.prefWidthProperty().bind(quizzesTable.widthProperty().divide(3));

        quizzesTable.setItems(quizFxes);

        this.labelCourse.setText(selectedCourse.getName());
        this.labelTotalQuizen.setText(String.valueOf(quizFxes.size()));


    }

    /**
     * @author M.J. Moshiri
     * <p>
     * Add 2 btn to Quiz table for delet or edit Row
     */
    private void addActionBtnToQuizTable() {
        col_Action_Quiz.setCellFactory(cellData -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                pane.setSpacing(2);
                editButton.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.BLUE));
                deleteButton.setGraphic(glyphFont.create(FontAwesome.Glyph.REMOVE).color(Color.RED));
                deleteButton.setOnAction(event -> {
                    quizzesTable.getSelectionModel().select(getIndex());
                    selectedQuiz = getTableView().getItems().get(getIndex());
                    deleteQuiz();
                });

                editButton.setOnAction(event -> {
                    quizzesTable.getSelectionModel().select(getIndex());
                    selectedQuiz = getTableView().getItems().get(getIndex());
                    PopOver p = createQuizPopOver(selectedCourse.getCourseObject(), selectedQuiz.getQuizObject());
                    p.show(editButton);
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
     * <p>
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

        btnNewQuestion.setDisable(false);
    }

    /**
     * @author M.J. Moshiri
     * <p>
     * Add edit and delete btn to questionTable
     */
    private void addActionBtnToQuestionTable() {
        colActionQuestion.setCellFactory(cellData -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final VBox pane = new VBox(editButton, deleteButton);

            {
                pane.setSpacing(2);
                editButton.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.BLUE));
                deleteButton.setGraphic(glyphFont.create(FontAwesome.Glyph.REMOVE).color(Color.RED));
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
                    PopOver p = createQuestionPopOver(selectedQuiz.getQuizObject(), selectedQuestion.getQuestionObject());
                    p.show(editButton);

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
     * <p>
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
        btnNewAnswer.setDisable(false);

    }

    /**
     * @author M.J. Moshiri
     * <p>
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
     * <p>
     * Add edit and delete btn to answer table
     */
    private void addBtnToAnswerTable() {
        col_Delete_Answer.setCellFactory(cellData -> new TableCell<>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                pane.setSpacing(2);
                editButton.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.BLUE));
                deleteButton.setGraphic(glyphFont.create(FontAwesome.Glyph.REMOVE).color(Color.RED));
                deleteButton.setOnAction(event -> {
                    answerTable.getSelectionModel().select(getIndex());
                    selectedAnswer = getTableView().getItems().get(getIndex());
                    deleteAnswer();
                });

                editButton.setOnAction(event -> {
                    answerTable.getSelectionModel().select(getIndex());
                    selectedAnswer = getTableView().getItems().get(getIndex());
                    PopOver p = createAnswerPopOver(selectedQuestion.getQuestionObject(), selectedAnswer.getAnswerObject());
                    p.show(editButton);

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
     * <p>
     * it will refresh the quiz table according to the selected course and if no course has been selected
     * it will clean and empty all fields
     * also close the panel if it is open
     */
    private void refreshQuizTable() {
        if (selectedCourse != null) {
            fillQuizTable();

        } else {
            clearAll();
        }

    }

    /**
     * @author M.J. Moshiri
     * <p>
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


    }

    /**
     * @author M.J. Moshiri
     * <p>
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

    }


    /**
     * @author M.J. Moshiri
     * <p>
     * Action method of Click on Course table
     * Which will fill add quizzes of selected course to de ListView
     */
    public void courseTableOnClick() {
        if (courseTable.getSelectionModel().getSelectedItem() != null) {
            clearAll();
            btnNewQuiz.setDisable(false);
            selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            refreshQuizTable();
        }
    }

    /**
     * @author M.J. Moshiri
     * <p>
     * Quiz Table select function which is on table click action
     * it checks if the user has chosen a valid row then take the selected row object as selected quiz
     * and then fill the question table according to the selected row
     */
    public void quizTableOnClick() {
        if (quizzesTable.getSelectionModel().getSelectedItem() != null) {
            selectedQuiz = quizzesTable.getSelectionModel().getSelectedItem();
            refreshQuestionTable();
            selectedQuestion = null;
            refreshAnswerTable();
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
    }

    /**
     * @param lastAnswer is the answer which the user is trying to add
     * @return a boolean define possibility of adding given answer
     * @author M.J. Moshiri
     * <p>
     * Limit adding new answer if the given answer is extra or its the second correct answer
     * it take the given answer and count the answers that has already been dedicated to the question
     * and also count the number of corect answers and it the new giving answer is exceeding the limit it will
     * return false
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
     * <p>
     * Delete the selected quiz if the user Approve the confirmation
     */
    public void deleteQuiz() {
        QuizFx quizFx = quizzesTable.getSelectionModel().getSelectedItem();
        boolean r = AlertHelper.confirmationDialog("Wilt u zeker Quiz " +
                quizzesTable.getSelectionModel().getSelectedItem().getName()
                + " verwijderen ?");
        if (r) {
            coordinatorDAO.archiveQuiz(quizFx.getQuizObject());
            refreshQuizTable();
            selectedQuiz = null;
        }
    }

    /**
     * @author M.J. Moshiri
     * <p>
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
            answerTable.getItems().clear();
        }
    }

    /**
     * @author M.J. Moshiri
     * <p>
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
     * Opens the pane for adding new Quiz also clears the field
     */
    public void btnNewQuizAction() {
        try {
            PopOver popOver = createQuizPopOver(selectedCourse.getCourseObject(), new Quiz(0));
            popOver.show(btnNewQuiz);
        } catch (NullPointerException e) {
            /// select a course fisrt
        }

    }

    /**
     * @author M.J. Moshiri
     * Opens the pane for adding new Question
     */
    public void btnNewQuestionAction() {
        try {
            PopOver p = createQuestionPopOver(selectedQuiz.getQuizObject(), new Question(0));
            p.show(btnNewQuestion);
        } catch (NullPointerException e) {
            /// select a quiz
        }
    }


    /**
     * @author M.J. Moshiri
     * <p>
     * Opens the titlepane for adding new answer to the selected Question
     */
    public void btnNewAnswerAction() {
        try {
            PopOver popOver = createAnswerPopOver(selectedQuestion.getQuestionObject(), new Answer(0));
            popOver.show(btnNewAnswer);
        } catch (NullPointerException e) {
            /// select a quistion fisrt
        }
    }

    /**
     * @param header of the popover
     * @return popover object
     * @author M.J. Moshiri
     * Creates a general popover with preferred attribute
     */
    private PopOver getConfiguredPopover(String header) {
        PopOver popOver = new PopOver();
        popOver.setArrowSize(0);
        popOver.setTitle(header);
        popOver.setDetachable(false);
        popOver.setHeaderAlwaysVisible(true);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        return popOver;
    }

    /**
     * @param course object that quizzes will be added to
     * @param quiz   object for adding to course
     * @return a popover for edit or saving Quiz object
     * @author M.J. Moshiri
     */
    private PopOver createQuizPopOver(Course course, Quiz quiz) {
        PopOver popOver = getConfiguredPopover("Quiz panel");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        Label quiznameLabel = new Label("Quiz naam:");
        Label successDefinitieLable = new Label("Success Definitie:");

        TextField quiznameText = TextFields.createClearableTextField();
        quiznameText.setPromptText("de naam van quiz");

        TextField successDefinitieText = TextFields.createClearableTextField();
        successDefinitieText.setPromptText("Success definitie");
        if (quiz.getIdquiz() != 0) {
            successDefinitieText.setText(String.valueOf(quiz.getSuccsesDefinition()));
            quiznameText.setText(quiz.getName());
        }
        Button savebtn = new Button("Opslaan");
        savebtn.setMaxHeight(Double.MAX_VALUE);

        savebtn.setDefaultButton(true);

        gridPane.addRow(0, quiznameLabel, quiznameText);
        gridPane.addRow(1, successDefinitieLable, successDefinitieText);
        gridPane.add(savebtn, 2, 0, 1, 2);

        savebtn.setOnAction(actionEvent -> {
            String finalquizName = quiznameText.getText();
            String sd = successDefinitieText.getText();

            if (!sd.equals("") && !finalquizName.equals("")) {
                quiz.setIdcourse(course.getDbId());
                double finalsuccesDefinite = Double.parseDouble(sd);
                quiz.setName(finalquizName);
                quiz.setSuccsesDefinition(finalsuccesDefinite);

                if (quizDAO.saveQuiz(quiz) != null) {
                    courseTable.getSelectionModel().getSelectedItem().addQuiz(quiz);
                    refreshQuizTable();
                    popOver.hide();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "AUB vull alle benodigde informatie").show();
            }
        });

        popOver.setContentNode(gridPane);

        return popOver;
    }

    /**
     * @param quiz     object that the question will be added to
     * @param question qustion object
     * @return a popover for editing or saving new Question in database
     * @author M.J. Moshiri
     */
    private PopOver createQuestionPopOver(Quiz quiz, Question question) {
        PopOver popOver = getConfiguredPopover("Question panel");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        TextArea textArea = new TextArea();
        textArea.setPromptText("Type de vraag hier.");

        Button saveBtn = new Button("Opslaan");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setMaxHeight(Double.MAX_VALUE);

        gridPane.add(textArea, 0, 0);
        gridPane.add(saveBtn, 1, 0);

        if (question.getQuestionId() != 0) textArea.setText(question.getQuestion());

        saveBtn.setOnAction(actionEvent -> {
            question.setQuestion(textArea.getText());
            question.setQuizId(quiz.getIdquiz());
            if (questionDAO.saveQuestion(question) != null) popOver.hide();
            refreshQuestionTable();
        });

        popOver.setContentNode(gridPane);
        return popOver;
    }

    /**
     * @param question     the onwer question
     * @param answerInhand the answer that will be processed
     * @return the PopOver
     * @author M.J. Moshiri
     * Create a popOver for edit or creating Answers
     */
    private PopOver createAnswerPopOver(Question question, Answer answerInhand) {
        PopOver popOver = getConfiguredPopover("Answer panel");
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        TextArea answerText = new TextArea();
        CheckBox isCorrect = new CheckBox("Juist Antwoord");
        Button savebtn = new Button("Opslaan");
        savebtn.setDefaultButton(true);
        savebtn.setMaxWidth(Double.MAX_VALUE);
        savebtn.setMaxHeight(Double.MAX_VALUE);
        gridPane.add(answerText, 0, 0, 1, 2);
        gridPane.add(isCorrect, 1, 0);
        gridPane.add(savebtn, 1, 1);

        if (answerInhand.getId() != 0) answerText.setText(answerInhand.getAnswer());

        savebtn.setOnAction(actionEvent -> {
            boolean newCorrectValue = isCorrect.isSelected();
            String answer = answerText.getText();
            answerInhand.setQuestionId(question.getQuestionId());
            answerInhand.setCorrect(newCorrectValue);
            answerInhand.setAnswer(answer);
            if (!limitAnswers(answerInhand)) {
                new Alert(Alert.AlertType.ERROR, "max 4 answer and 1 good answer").show();
                return;
            }
            question.addAnswer(answerInhand);
            /// Send to DAO

            if (answerDAO.saveAnswer(answerInhand) != null) { // after succesfull add we disable the editMode
                refreshQuestionTable();
                refreshAnswerTable();
                popOver.hide();
            }

        });
        popOver.setContentNode(gridPane);

        return popOver;
    }

}
