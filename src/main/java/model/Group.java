package model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.List;

public class Group {
    private IntegerProperty dbId;
    private StringProperty name;
    private ObjectProperty<User> teacher;
    private ObjectProperty<User> coordinator;
    private ListProperty<Student> students;

    public Group(String name, User teacher, User coordinator, List<Student> students) {
        this.name = new SimpleStringProperty(name);
        this.teacher = new SimpleObjectProperty<>(teacher);
        this.coordinator =  new SimpleObjectProperty<>(coordinator);
        this.students = new SimpleListProperty<>();
        this.students.addAll(students);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public User getTeacher() {
        return teacher.get();
    }

    public ObjectProperty<User> teacherProperty() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher.set(teacher);
    }

    public User getCoordinator() {
        return coordinator.get();
    }

    public ObjectProperty<User> coordinatorProperty() {
        return coordinator;
    }

    public void setCoordinator(User coordinator) {
        this.coordinator.set(coordinator);
    }

    public ObservableList<Student> getStudents() {
        return students.get();
    }

    public ListProperty<Student> studentsProperty() {
        return students;
    }

    public void setStudents(ObservableList<Student> students) {
        this.students.set(students);
    }

    public void addStudentToGroup(Student s){
        this.students.add(s);
    }
    public int getDbId() {
        return dbId.get();
    }

    public IntegerProperty dbIdProperty() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId.set(dbId);
    }


}
