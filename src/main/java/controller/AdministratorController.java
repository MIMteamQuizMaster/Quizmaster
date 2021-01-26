package controller;

import controller.fx.CourseFx;
import controller.fx.GroupFX;
import controller.fx.ObjectConvertor;
import controller.fx.UserFx;
import database.mysql.CourseDAO;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.StringConverter;
import launcher.Main;
import model.Course;
import model.Group;
import model.User;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.*;


import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


public class AdministratorController implements Initializable {

    public TableView<CourseFx> courseDetailsTable;
    public TableColumn<CourseFx, String> col_CourseName;
    public TableColumn<CourseFx, String> col_CourseStart;
    public TableColumn<CourseFx, String> col_CourseEnd;
    public TableColumn<CourseFx, Integer> col_CourseGroup;
    public TableColumn<CourseFx, User> col_CourseCoordinator;
    public TableColumn<CourseFx, Void> col_CourseActions;

    public SplitPane splitPane;

    public TableView<GroupFX> groupTable;
    public TableColumn<GroupFX, String> col_groupName;
    public TableColumn<GroupFX, Integer> col_totalStudents;
    public TableColumn<GroupFX, Void> col_groupTeacher;
    public Button backtoCourse;
    public ListView<UserFx> totalAvailable;
    public ListView studentInGroup;
    public Button newCourseBtn;
    private CourseDAO courseDAO;
    private GlyphFont glyphFont;

    private CourseFx selectedCourseFx;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        glyphFont = GlyphFontRegistry.font("FontAwesome");
        courseDAO = new CourseDAO(Main.getDBaccess());
        backtoCourse.setCancelButton(true);
        backtoCourse.setOnAction(actionEvent -> showGroupPanel(false));
        fillTable();
    }

    /**
     * @author M.J. Moshiri
     * fill the course table wit all data
     * and show the coordinator assigned to the course
     * the coordinator must be a valid user
     */
    private void fillTable() {
        List<Course> s = courseDAO.getAllCourses();
        ObservableList<CourseFx> courseFxes = ObjectConvertor.convertCoursetoCourseFX(s);
        col_CourseName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_CourseStart.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_CourseEnd.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
        col_CourseGroup.setCellValueFactory(cellData -> cellData.getValue().getTotalGroups().asObject());
//        col_CourseCoordinator.setCellFactory(cellData -> getCoordinatorTableCell());
        col_CourseCoordinator.setCellValueFactory(cellData -> cellData.getValue().coordinatorProperty());
        col_CourseActions.setCellFactory(cellData -> createCourseActionCel());

        courseDetailsTable.setItems(courseFxes);
        courseDetailsTable.refresh();
        courseDetailsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) showGroupPanel(false);
            else if (event.getClickCount() == 2) {
                onTableClick();
            }

        });
        courseDetailsTable.getColumns().addListener((ListChangeListener) change -> {
            change.next();
            if (change.wasReplaced()) {
                courseDetailsTable.refresh();
            }
        }); // on coloums re ordering or resizing fix the rendering bug by refreshing the table
    }

    private TableCell<CourseFx, Void> createCourseActionCel() {
        return new TableCell<>() {
            private final Button delButton = new Button("");
            private final Button editButton = new Button("");
            private final HBox pane = new HBox(editButton, delButton);

            {
                delButton.setGraphic(glyphFont.create(FontAwesome.Glyph.REMOVE).color(Color.RED));
                editButton.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.BLUE));
                delButton.setOnAction(event ->
                {
                    boolean r = AlertHelper.confirmationDialog("Wilt u de lidmaatschap ven deze gebruiker beëindigen?");
                    if (r) {
//                        UserFx u = getTableRow().getItem();
//                        dao.setEnd(u.getUserObject());
//                        refreshTable();
                        System.out.println(this.getTableColumn().getWidth());
                        System.out.println(this.getWidth());
                    }
                });

                editButton.setOnAction(event -> {
                    CourseFx courseFx = getTableRow().getItem();
                    PopOver popOver = coursePanelPopOver(courseFx.getCourseObject());
                    popOver.show(editButton);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(pane);

                }

            }
        };
    }

//    private TableCell<CourseFx, Void> getCoordinatorTableCell() {
//        return new TableCell<>() {
//            private final Button btn = new Button();
//
//            {
//                btn.prefWidthProperty().bind(col_CourseCoordinator.widthProperty().subtract(5));
//                btn.setOnAction(event -> {
//                    CourseFx courseFx = getTableRow().getItem();
//                    PopOver popOver = creatCoordinatorPopOver(courseFx);
//                    popOver.show(btn);
//                });
//            }
//
//            @Override
//            protected void updateItem(Void s, boolean empty) {
//                super.updateItem(s, empty);
//
//                if (!empty) {
//                    setGraphic(btn);
//                    CourseFx courseFx = getTableRow().getItem();
//                    if (courseFx != null) {
//                        User coordinator = courseFx.getCoordinator();
//                        getbtn(coordinator, btn);
//                    }
//                }
//            }
//        };
//    }

    public void onTableClick() {
        if (courseDetailsTable.getSelectionModel().getSelectedItem() != null) {
//            splitPane.setDividerPosition(0, 0.4);
            showGroupPanel(true);
            selectedCourseFx = courseDetailsTable.getSelectionModel().getSelectedItem();
            fillGroupTable(courseDetailsTable.getSelectionModel().getSelectedItem());

        }
    }

    /**
     * @param course object from tableView
     * @return a popOver
     * @author M.J. Moshiri
     * create a popOver to assign new coordinator to to course, it also show the momentory coordinator of the
     * course
     * the listView in the popOver has a listener to update the new value of the coordinator for course
     */
    private PopOver creatCoordinatorPopOver(Course course) {
        PopOver popOver = new PopOver();
        ObservableList<User> coordinators = FXCollections.observableArrayList(courseDAO.getALlValidCoordinators());
        Label lb = new Label("Double click om te kiezen.");
        ListView<User> listView = new ListView<>();
        VBox vBox = new VBox(lb, listView);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);
        listView.setItems(coordinators);
        listView.setMinHeight(300);
        listView.setCellFactory(param -> new ListCell<User>() {
            @Override
            public void updateItem(User name, boolean empty) {
                super.updateItem(name, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setContentDisplay(ContentDisplay.RIGHT);
                    setText(name.toString());
                    if (course.getCoordinator() != null) {
                        if (course.getCoordinator().getUserId() == name.getUserId()) {
                            setGraphic(glyphFont.create(FontAwesome.Glyph.CHECK).color(Color.GREEN));
                        }
                    }

                }
            }
        });

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    User u = listView.getSelectionModel().getSelectedItem();
                    course.setCoordinator(u);
                    Button owner = (Button) popOver.getOwnerNode();
                    owner.setText(u.toString());
                    owner.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.GREEN));
                    popOver.hide();
                }
            }
        });
        listView.getSelectionModel().getSelectedItems();
        popOver.setContentNode(vBox);
        return popOver;
    }

    private void fillGroupTable(CourseFx courseFx) {
        List<Group> s = courseFx.getGroups();
        ObservableList<GroupFX> courseFxes = ObjectConvertor.convertGroupToGroupFX(s);
        col_groupName.setCellValueFactory(data -> data.getValue().nameProperty());
        col_totalStudents.setCellValueFactory(data -> data.getValue().getTotalStudents().asObject());
        col_groupTeacher.setCellFactory(data -> getTeacherTableCell());
        groupTable.setItems(courseFxes);
        fillAvailableList(courseFx);
    }

    private TableCell<GroupFX, Void> getTeacherTableCell() {
        return new TableCell<>() {
            private final Button btn = new Button();

            {
                btn.prefWidthProperty().bind(col_CourseCoordinator.widthProperty().subtract(5));
                btn.setOnAction(event -> {
                    GroupFX GroupFX = getTableRow().getItem();
                    //TODO assign teacher to group
                    PopOver popOver = groupsPopOver(GroupFX);
                    popOver.show(btn);
//                    PopOver popOver = creatCoordinatorPopOver(courseFx);
//                    popOver.show(btn);
                });
            }

            @Override
            protected void updateItem(Void s, boolean empty) {
                super.updateItem(s, empty);

                if (!empty) {
                    setGraphic(btn);
                    GroupFX GroupFX = getTableRow().getItem();
                    if (GroupFX != null) {
                        User teacher = GroupFX.getTeacher();
                        getbtn(teacher, btn);
                    }
                }
            }
        };
    }

    private ListView fillAvailableList(CourseFx courseFx) {
        ListView listView = new ListView();
        List<User> userList = courseDAO.getStudentsWithNoGroupAssigned(courseFx.getCourseObject());
        if (userList != null) {
            ObservableList<UserFx> userFxes = ObjectConvertor.convertUserToUserFX(userList);
//            ObservableList<User> userFxes1 = FXCollections.observableArrayList(userList);

            listView.setCellFactory(param -> new ListCell<UserFx>() {
                @Override
                protected void updateItem(UserFx item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        User u = item.getUserObject();
                        if (u != null) {
                            setText(u.toString());
                        } else {
                            setText(null);
                        }
                    }
                }
            });
            listView.setItems(userFxes);
        }
        return listView;
    }

    private PopOver groupsPopOver(GroupFX group) {
        PopOver popOver = new PopOver();
        Tooltip tp = new Tooltip("Double click to add to group");
        Label labelAssign = new Label("Students with no group:");
        Label labelAssigned = new Label("Students in " + group.getName() + ":");
        ListView listAssign = fillAvailableList(selectedCourseFx);
        ListView listAssigned = new ListView();
        VBox leftside = new VBox(labelAssign, listAssign);
        Tooltip.install(leftside, tp);
        Button add = new Button(), addall = new Button(), remove = new Button(), removeall = new Button();
        VBox midBox = new VBox(add, addall, remove, removeall);
        midBox.setAlignment(Pos.CENTER);
        midBox.setSpacing(10);
        VBox rightside = new VBox(labelAssigned, listAssigned);
        leftside.setSpacing(5);
        rightside.setSpacing(5);
        HBox hBox = new HBox(leftside, midBox, rightside);
        hBox.setPadding(new Insets(5, 5, 5, 5));
        popOver.setContentNode(hBox);
        return popOver;
    }

    private Button getbtn(User teacher, Button btn) {
        if (teacher == null) {
            btn.setGraphic(glyphFont.create(FontAwesome.Glyph.PLUS_CIRCLE).color(Color.GREEN));
            btn.setStyle("-fx-background-color: #f6a3a3");
            btn.setText("Assign");

        } else {
            btn.setGraphic(glyphFont.create(FontAwesome.Glyph.EDIT).color(Color.RED));
            btn.setStyle("-fx-background-color: #ccffcc");
            btn.setText(teacher.toString());
        }
        return btn;
    }

    private void showGroupPanel(Boolean expand) {
        KeyValue keyValue = new KeyValue(splitPane.getDividers().get(0).positionProperty(), (expand ? 0.01 : 0.99));
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), keyValue));
        timeline.play();
    }

    private PopOver coursePanelPopOver(Course courseInHand) {
        PopOver popOver = new PopOver();
        popOver.setArrowSize(0);
        popOver.setDetachable(false);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        Label courseLableName = new Label("Naam van Cursus:");
        TextField courseNameTxt = TextFields.createClearableTextField();
        gridPane.addRow(0, courseLableName, courseNameTxt);

        Label startDateLable = new Label("Start Date:");
        DatePicker startDP = getDp();
        gridPane.addRow(1, startDateLable, startDP);

        Label endDateLable = new Label("End Date:");
        DatePicker endDP = getDp();
        gridPane.addRow(2, endDateLable, endDP);

        Label coordinatorLable = new Label("Coordinator:");
        Button setCoordinator = new Button("Assign");
        setCoordinator.setGraphic(glyphFont.create(FontAwesome.Glyph.PLUS_CIRCLE).color(Color.GREEN));
        setCoordinator.setOnAction(event -> {
            PopOver pop = creatCoordinatorPopOver(courseInHand);
            pop.show(setCoordinator);
        });
        gridPane.addRow(3, coordinatorLable, setCoordinator);

        Button savebtn = new Button("add");
        /// TODO make update courseInHand
        savebtn.setOnAction(actionEvent -> {
            courseInHand.setName(courseNameTxt.getText());
            courseInHand.setStartDate(startDP.getValue().toString());
            courseInHand.setEndDate(endDP.getValue().toString());
            courseDetailsTable.refresh();
            boolean result = courseInHand.saveToDB();
//            if (result) {
//            }
        });
        Button cancelbtn = new Button("cancel");
        cancelbtn.setOnAction(actionEvent -> popOver.hide());

        savebtn.setMaxWidth(Double.MAX_VALUE);
        cancelbtn.setMaxWidth(Double.MAX_VALUE);

        HBox btnBox = new HBox(savebtn, cancelbtn);
        btnBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(savebtn, Priority.ALWAYS);
        HBox.setHgrow(cancelbtn, Priority.ALWAYS);
        btnBox.setSpacing(5);
        gridPane.add(btnBox, 1, 4);

        popOver.setContentNode(gridPane);
        // fill data
        courseNameTxt.setText(courseInHand.getName());
        if (courseInHand.getStartDate() != null) startDP.setValue(LocalDate.parse(courseInHand.getStartDate()));
        if (courseInHand.getEndDate() != null) endDP.setValue(LocalDate.parse(courseInHand.getEndDate()));
        if (courseInHand.getCoordinator() != null) {
            setCoordinator.setText(courseInHand.getCoordinator().toString());
            setCoordinator.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.GREEN));
        }
        return popOver;
    }

    /**
     * Create a dataPicker with the format we use
     * which is yyyy-MM-dd
     *
     * @return configured datepicker
     */
    private DatePicker getDp() {
        DatePicker datePicker = new DatePicker();
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate localDate) {
                if (localDate != null) {
                    return dateFormatter.format(localDate);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String s) {
                if (s != null && !s.isEmpty()) {
                    return LocalDate.parse(s, dateFormatter);
                }
                return null;
            }
        });
        return datePicker;
    }

    public void newCourse(ActionEvent actionEvent) {
        PopOver popOver = coursePanelPopOver(new Course());
        popOver.show(newCourseBtn);
    }
}
