package controller.fx;

import javafx.beans.property.*;
import model.Group;
import model.User;


public class GroupFX {
    private Group group;

    public GroupFX(Group group) {
        this.group = group;
    }

    public Group getGroupObject(){
        return this.group;
    }

    public int getDbId() {
        return new SimpleIntegerProperty(this.group.getGroupId()).get();
    }

    public IntegerProperty dbIdProperty() {
        return new SimpleIntegerProperty(this.group.getGroupId());
    }

    public void setDbId(int dbId) {
        this.group.setGroupId(dbId);
    }

    public String getName() {
        return new SimpleStringProperty(this.group.getName()).get();
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(this.group.getName());
    }

    public void setName(String name) {
        this.group.setName(name);
    }

    public User getTeacher() {
        return (User) new SimpleObjectProperty<>(this.group.getTeacher()).get();
    }

    public void setTeacher(User teacher) {
        this.group.setTeacher(teacher);
    }

    public IntegerProperty getTotalStudents() {
        return new SimpleIntegerProperty(group.getStudents().size());
    }
}

