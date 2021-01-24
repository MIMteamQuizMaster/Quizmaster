package controller;

import controller.fx.CourseFx;
import controller.fx.GroupFx;
import controller.fx.ObjectConvertor;
import controller.fx.UserFx;
import database.mysql.CourseDAO;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import launcher.Main;
import model.Course;
import model.Group;
import model.User;

import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.*;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class AdministratorController implements Initializable {

    public TableView<CourseFx> courseDetailsTable;
    public TableColumn<CourseFx, String> col_CourseName;
    public TableColumn<CourseFx, String> col_CourseStart;
    public TableColumn<CourseFx, String> col_CourseEnd;
    public TableColumn<CourseFx, Integer> col_CourseGroup;
    public TableColumn<CourseFx, Void> col_CourseCoordinator;
    public SplitPane splitPane;

    public TableView<GroupFx> groupTable;
    public TableColumn<GroupFx, String> col_groupName;
    public TableColumn<GroupFx, Integer> col_totalStudents;
    public TableColumn<GroupFx, Void> col_groupTeacher;
    public Button backtoCourse;
    public ListView<UserFx> totalAvailable;
    public ListView studentInGroup;
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
        col_CourseCoordinator.setCellFactory(cellData -> getCoordinatorTableCell());
        courseDetailsTable.setItems(courseFxes);
        courseDetailsTable.refresh();
        courseDetailsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) showGroupPanel(false);
            else if (event.getClickCount() == 2) {
                onTableClick();
            }

        });
    }


    private TableCell<CourseFx, Void> getCoordinatorTableCell() {
        return new TableCell<>() {
            private final Button btn = new Button();

            {
                btn.prefWidthProperty().bind(col_CourseCoordinator.widthProperty().subtract(5));
                btn.setOnAction(event -> {
                    CourseFx courseFx = getTableRow().getItem();
                    PopOver popOver = creatCoordinatorPopOver(courseFx);
                    popOver.show(btn);
                });
            }

            @Override
            protected void updateItem(Void s, boolean empty) {
                super.updateItem(s, empty);

                if (!empty) {
                    setGraphic(btn);
                    CourseFx courseFx = getTableRow().getItem();
                    if (courseFx != null) {
                        User coordinator = courseFx.getCoordinator();
                        getbtn(coordinator, btn);
                    }
                }
            }
        };
    }

    public void onTableClick() {
        if (courseDetailsTable.getSelectionModel().getSelectedItem() != null) {
//            splitPane.setDividerPosition(0, 0.4);
            showGroupPanel(true);
            selectedCourseFx = courseDetailsTable.getSelectionModel().getSelectedItem();
            fillGroupTable(courseDetailsTable.getSelectionModel().getSelectedItem());

        }
    }

    /**
     * @param courseFx object from tableView
     * @return a popOver
     * @author M.J. Moshiri
     * create a popOver to assign new coordinator to to course, it also show the momentory coordinator of the
     * course
     * the listView in the popOver has a listener to update the new value of the coordinator for course
     */
    private PopOver creatCoordinatorPopOver(CourseFx courseFx) {
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
                    if (courseFx.getCoordinator().getUserId() == name.getUserId()) {
                        setGraphic(glyphFont.create(FontAwesome.Glyph.CHECK).color(Color.GREEN));
                    }
                }
            }
        });

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    User u = listView.getSelectionModel().getSelectedItem();
                    Course c = courseFx.getCourseObject();
                    courseFx.setCoordinator(u);
                    courseDAO.assignCoordinatorToCourse(u, c);
                    courseDetailsTable.refresh();
                }
            }
        });
        listView.getSelectionModel().getSelectedItems();
        popOver.setContentNode(vBox);
        return popOver;
    }

    private void fillGroupTable(CourseFx courseFx) {
        List<Group> s = courseFx.getGroups();
        ObservableList<GroupFx> courseFxes = ObjectConvertor.convertGrouptoGroupFX(s);
        col_groupName.setCellValueFactory(data -> data.getValue().nameProperty());
        col_totalStudents.setCellValueFactory(data -> data.getValue().getTotalStudents().asObject());
        col_groupTeacher.setCellFactory(data -> getTeacherTableCell());
        groupTable.setItems(courseFxes);
        fillAvailableList(courseFx);
    }

    private TableCell<GroupFx, Void> getTeacherTableCell() {
        return new TableCell<>() {
            private final Button btn = new Button();

            {
                btn.prefWidthProperty().bind(col_CourseCoordinator.widthProperty().subtract(5));
                btn.setOnAction(event -> {
                    GroupFx groupFx = getTableRow().getItem();
                    //TODO assign teacher to group
                    PopOver popOver = groupsPopOver(groupFx);
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
                    GroupFx groupFx = getTableRow().getItem();
                    if (groupFx != null) {
                        User teacher = groupFx.getTeacher();
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

    private PopOver groupsPopOver(GroupFx group) {
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
}
