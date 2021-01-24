package controller.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Group;
import model.User;

public class GroupFx {
    private Group group;

    public GroupFx(Group group) {
        this.group = group;
    }


    public int getDbID() {
        return new SimpleIntegerProperty(group.getDbID()).get();
    }

    public IntegerProperty dbIDProperty() {
        return new SimpleIntegerProperty(group.getDbID());
    }

    public void setDbID(int dbID) {
        this.group.setDbID(dbID);
    }

    public String getName() {
        return new SimpleStringProperty(group.getName()).get();
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(group.getName());
    }

    public void setName(String name) {
        this.group.setName(name);
    }

    public User getTeacher() {
        return new SimpleObjectProperty<User>(group.getTeacher()).get();
    }

    public ObjectProperty teacherProperty() {
        return new SimpleObjectProperty<>(group.getTeacher());
    }

    public void setTeacher(User teacher) {
        this.group.setTeacher( teacher);
    }

    public ObservableList<User> getStudents() {
        return new SimpleListProperty<>(FXCollections.observableList(this.group.getStudents())).get();
    }

    public ListProperty<User> studentsProperty() {
        return new SimpleListProperty<>(FXCollections.observableList(this.group.getStudents()));
    }

    public void setStudents(ObservableList<User> students) {
        this.group.setStudents(students);
    }

    public IntegerProperty getTotalStudents(){
        return new SimpleIntegerProperty(group.getStudents().size());
    }
}
