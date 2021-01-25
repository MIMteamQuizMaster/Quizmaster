package controller;

import controller.fx.CourseFx;
import controller.fx.ObjectConvertor;
import controller.fx.UserFx;
import database.mysql.CourseDAO;
import database.mysql.GroupDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;

import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import launcher.Main;
import model.Course;
import model.Group;
import model.Role;
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

    public TableView<CourseFx> courseTableView;
    public TableColumn<CourseFx, String> col_CourseName;
    public TableColumn<CourseFx, String> col_CourseStart;
    public TableColumn<CourseFx, String> col_CourseEnd;
    public TableColumn<CourseFx, Void> col_CourseGroup;
    public TableColumn<CourseFx, User> col_CourseCoordinator;
    public TableColumn<CourseFx, Void> col_CourseActions;

    public SplitPane splitPane;
    public Button newCourseBtn;
    private CourseDAO courseDAO;
    private GroupDAO groupDAO;
    private GlyphFont glyphFont;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        glyphFont = GlyphFontRegistry.font("FontAwesome");
        courseDAO = new CourseDAO(Main.getDBaccess());
        groupDAO = new GroupDAO(Main.getDBaccess());
        fillCourseTable();
    }

    /**
     * @author M.J. Moshiri
     * fill the course table wit all data
     * and show the coordinator assigned to the course
     * the coordinator must be a valid user
     */
    private void fillCourseTable() {
        List<Course> s = courseDAO.getAllCourses();
        ObservableList<CourseFx> courseFxes = ObjectConvertor.convertCoursetoCourseFX(s);
        col_CourseName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        col_CourseStart.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        col_CourseEnd.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
//        col_CourseGroup.setCellValueFactory(cellData -> cellData.getValue().getTotalGroups().asObject());
        col_CourseGroup.setCellFactory(cellData -> createCourseGroupCell());
        col_CourseCoordinator.setCellValueFactory(cellData -> cellData.getValue().coordinatorProperty());
        col_CourseActions.setCellFactory(cellData -> createCourseActionCell());
        courseTableView.setItems(courseFxes);
        courseTableView.refresh();
        courseTableView.setOnMouseClicked(event -> {
//            if (event.getClickCount() == 1) showGroupPanel(false);
//            else if (event.getClickCount() == 2) {
//                onTableClick();
//            }

        });
        courseTableView.getColumns().addListener((ListChangeListener) change -> {
            change.next();
            if (change.wasReplaced()) {
                courseTableView.refresh();
            }
        }); // on coloums re ordering or resizing fix the rendering bug by refreshing the table
        col_CourseName.prefWidthProperty().bind(courseTableView.widthProperty().divide(6));
        col_CourseStart.prefWidthProperty().bind(courseTableView.widthProperty().divide(10));
        col_CourseEnd.prefWidthProperty().bind(courseTableView.widthProperty().divide(10));
        col_CourseGroup.prefWidthProperty().bind(courseTableView.widthProperty().divide(3));
        col_CourseCoordinator.prefWidthProperty().bind(courseTableView.widthProperty().divide(5));
        createCourseTableContextMenu();
    }

    private void createCourseTableContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem item1 = new MenuItem("Refresh");
        item1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                courseTableView.getItems().clear();
                fillCourseTable();
                courseTableView.refresh();
            }
        });
        MenuItem item2 = new MenuItem("Edit");
        item2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println(courseTableView.getSelectionModel().getSelectedItem());
            }
        });

        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(item1, item2);


        courseTableView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                contextMenu.show(courseTableView, contextMenuEvent.getScreenX(), contextMenuEvent.getScreenY());
            }
        });
    }

    private TableCell<CourseFx, Void> createCourseGroupCell() {
        return new TableCell<>() {
            private final Button add = new Button();
            private final HBox pane = new HBox();

            {
                add.setGraphic(glyphFont.create(FontAwesome.Glyph.PLUS).color(Color.RED));
                pane.setSpacing(2);
                pane.getChildren().add(add);

            }

            @Override
            protected void updateItem(Void unused, boolean b) {
                super.updateItem(unused, b);
                if (!b) {
                    if (getTableRow() != null) {
                        CourseFx c = getTableRow().getItem();
                        if (c != null) {
                            ObservableList<Group> groups = c.getGroups();
                            for (Group g : groups) {
                                pane.getChildren().add(groupHolderBtn(c.getCourseObject(), g));
                            }
                        }
                        setGraphic(pane);
                    }

                }
            }
        };
    }

    private Button groupHolderBtn(Course c, Group g) {
        Button holder = new Button();
        holder.setText(g.getName() + " (" + g.getStudents().size() + ")");
        holder.setStyle("-fx-background-color: #6ff34b");

        holder.setGraphic(glyphFont.create(FontAwesome.Glyph.USERS).color(Color.BLUE));
        holder.setOnAction(actionEvent -> {
                    PopOver popOver = groupsPopOver(c, g);
                    popOver.show(holder);
                }
        );
        return holder;
    }

    private PopOver groupsPopOver(Course course, Group group) {
        PopOver popOver = new PopOver();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(5, 5, 5, 5));

        Label groupName = new Label("Group name:");
        TextField groupNameText = TextFields.createClearableTextField();
        groupNameText.setText(group.getName());
        gridPane.addRow(0, groupName, groupNameText); // group name row

        Label teacherLable = new Label("Docent:");
        Button teacherBtn = new Button();
        teacherBtn = setBtn(group.getTeacher(), teacherBtn);
        Button finalTeacherBtn = teacherBtn;
        teacherBtn.setOnAction(event -> {
            PopOver teacherPopOver = createTeacherPopOver(group);
            teacherPopOver.show(finalTeacherBtn);
        });
        gridPane.addRow(1, teacherLable, teacherBtn);  // teacher row

        Label labelAssign = new Label("Students:");
        Label labelTotalAssign = new Label("Aantal: " + group.getStudents().size());
        VBox vBoxAssign = new VBox(labelAssign, labelTotalAssign);
        vBoxAssign.setSpacing(10);
        ListView<User> listAssigned = new ListView<User>();
        listAssigned.setPrefHeight(300);
//        ObservableList<User> students = groupDAO.getStudentsPerGroup(group);
        ObservableList<User> students = FXCollections.observableArrayList(group.getStudents());
        if (students != null && students.size() != 0) listAssigned.setItems(students);
        gridPane.addRow(2, vBoxAssign, listAssigned);
        GridPane.setValignment(vBoxAssign, VPos.TOP); // students row

        Button addStudent = new Button("Student Toevoegen");
        addStudent.setGraphic(glyphFont.create(FontAwesome.Glyph.USER_PLUS).color(Color.RED));
        addStudent.setMaxWidth(Double.MAX_VALUE);
        addStudent.setOnAction(actionEvent -> {
            ListView<User> allAvailable;
            allAvailable = createAllAvailableStudentList(course, group, listAssigned);
            allAvailable.setPrefHeight(300);
            gridPane.add(allAvailable, 2, 2);
        });
        gridPane.add(addStudent, 1, 3);

        popOver.setContentNode(gridPane);
        return popOver;
    }

    private ListView<User> createAllAvailableStudentList(Course course, Group group, ListView<User> students) {
        ListView<User> allAvailable = new ListView<User>();
        ObservableList<User> observableList = FXCollections.observableArrayList(courseDAO.getStudentsWithNoGroupAssigned(course));
        allAvailable.setCellFactory(param -> new ListCell<User>() {
            Button move = new Button();

            {
                move.setGraphic(glyphFont.create(FontAwesome.Glyph.CHEVRON_LEFT).color(Color.BLUE));
                move.setOnAction(event -> {
                    if (getItem() != null) {
                        User u = getItem();
//                        students.getItems().add(u);
                        observableList.remove(u);
                        group.addStudent(u);
                        getListView().refresh();

                    }

                });
            }

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (!empty) {
                    if (user != null) {
                        move.setText(getItem().toString());
                        setGraphic(move);
                    }
                } else {
                    setGraphic(null);
                }

            }
        });
        allAvailable.setItems(observableList);
        return allAvailable;
    }


    /**
     * @return counfigured cell
     * @author M.J. Moshiri
     * create a cell with 2 btn responsible for edditing of removing CourseFX object type
     */
    private TableCell<CourseFx, Void> createCourseActionCell() {
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
                        // TODO delete action on Course table
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


    /**
     * Create a Vbox pane with a ListView in index 1 which is filled with the given Role parameter
     *
     * @param r
     * @return
     */
    private VBox getPanelOfRoles(Role r) {
        ObservableList<User> users = FXCollections.observableArrayList(courseDAO.getAllValidUsersByRole(r));
        Label lb = new Label("Double click om te kiezen.");
        ListView<User> listView = new ListView<>();
        VBox vBox = new VBox(lb, listView);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(10);
        listView.setItems(users);
        listView.setMinHeight(300);
        return vBox;
    }


    /**
     * @param course object from tableView
     * @return a popOver
     * @author M.J. Moshiri
     * create a popOver to assign new coordinator to to course, it also show the momentory coordinator of the
     * course
     * the listView in the popOver has a listener to update the new value of the coordinator for course
     */
    private PopOver createCoordinatorPopOver(Course course) {
        PopOver popOver = new PopOver();
        VBox vBox = getPanelOfRoles(Role.COORDINATOR);
        ListView<User> listView = (ListView<User>) vBox.getChildren().get(1);
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
                    setBtn(u, owner);
                    popOver.hide();
                }
            }
        });
        listView.getSelectionModel().getSelectedItems();
        popOver.setContentNode(vBox);
        return popOver;
    }

    private PopOver createTeacherPopOver(Group group) {
        PopOver popOver = new PopOver();
        VBox vBox = getPanelOfRoles(Role.TEACHER);
        ListView<User> listView = (ListView<User>) vBox.getChildren().get(1);
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
                    if (group.getTeacher() != null) {
                        if (group.getTeacher().getUserId() == name.getUserId()) {

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
                    group.setTeacher(u);
                    Button owner = (Button) popOver.getOwnerNode();
                    owner = setBtn(u, owner);
//                    owner.setText(u.toString());
//                    owner.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.GREEN));

                    popOver.hide();
                }
            }
        });
        listView.getSelectionModel().getSelectedItems();
        popOver.setContentNode(vBox);
        return popOver;
    }


//    private ListView fillAvailableList(Course course) {
//        ListView listView = new ListView();
//        List<User> userList = courseDAO.getStudentsWithNoGroupAssigned(course);
//        if (userList != null) {
//            ObservableList<UserFx> userFxes = ObjectConvertor.convertUserToUserFX(userList);
////            ObservableList<User> userFxes1 = FXCollections.observableArrayList(userList);
//
//            listView.setCellFactory(param -> new ListCell<UserFx>() {
//                @Override
//                protected void updateItem(UserFx item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setText(null);
//                    } else {
//                        User u = item.getUserObject();
//                        if (u != null) {
//                            setText(u.toString());
//                        } else {
//                            setText(null);
//                        }
//                    }
//                }
//            });
//            listView.setItems(userFxes);
//        }
//        return listView;
//    }


    private Button setBtn(User user, Button btn) {
        if (user == null) {
            btn.setGraphic(glyphFont.create(FontAwesome.Glyph.PLUS_CIRCLE).color(Color.GREEN));
            btn.setText("Assign");

        } else {
            btn.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.BLUE));
            btn.setText(user.toString());
        }
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    /**
     * @param courseInHand which can be a new Course object or a reference to a Course object
     *                     if the id of object be 0 then the method will describe it as new entry
     * @return PopOver with values
     * @author M.J. Moshiri
     * This is a PopOver pane contain needed information for creating or editing a Course object
     */
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
        DatePicker startDP = makeDatePicker();
        gridPane.addRow(1, startDateLable, startDP);

        Label endDateLable = new Label("End Date:");
        DatePicker endDP = makeDatePicker();
        gridPane.addRow(2, endDateLable, endDP);

        Label coordinatorLable = new Label("Coordinator:");
        Button setCoordinator = new Button("Assign");
        setBtn(courseInHand.getCoordinator(), setCoordinator);
        setCoordinator.setOnAction(event -> {
            PopOver pop = createCoordinatorPopOver(courseInHand);
            pop.show(setCoordinator);
        });
        gridPane.addRow(3, coordinatorLable, setCoordinator);

        Button savebtn = new Button();
        savebtn.setText("Save");

        savebtn.setOnAction(actionEvent -> {
            courseInHand.setName(courseNameTxt.getText());
            LocalDate startdate = startDP.getValue();
            LocalDate enddate = endDP.getValue();
            courseInHand.setStartDate(startdate == null ? null : startdate.toString());
            courseInHand.setEndDate(enddate == null ? null : enddate.toString());
            boolean newEntry;
            if ((courseInHand.getDbId() == 0)) {
                newEntry = true;
            } else {
                newEntry = false;
            }
            courseTableView.refresh();
            boolean result = courseInHand.saveToDB();
            if (result) {
                if (newEntry) courseTableView.getItems().add(new CourseFx(courseInHand));
                courseTableView.refresh();
                popOver.hide();
            }
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
        return popOver;
    }

    /**
     * Create a dataPicker with the format we use
     * which is yyyy-MM-dd
     *
     * @return configured datepicker
     */
    private DatePicker makeDatePicker() {
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
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty())
                    return null;
                try {
                    return LocalDate.parse(dateString, dateFormatter);
                } catch (Exception e) {
                    /// bad format
                    datePicker.setValue(null);
                    datePicker.getEditor().clear();
                    return null;
                }
            }
        });
        datePicker.setPromptText("yyyy-MM-dd");
        datePicker.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    datePicker.setValue(datePicker.getConverter().fromString(datePicker.getEditor().getText()));
                }
            }
        });
        return datePicker;
    }

    public void newCourse(ActionEvent actionEvent) {
        PopOver popOver = coursePanelPopOver(new Course());
        popOver.show(newCourseBtn);
    }
}
