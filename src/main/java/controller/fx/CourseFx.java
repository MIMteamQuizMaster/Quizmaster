package controller.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

public class CourseFx {
    private final Course course;

    public CourseFx(Course c) {
        this.course = c;
    }

    public Course getCourseObject(){
        return this.course;
    }

    public int getDbId() {
        return new SimpleIntegerProperty(this.course.getCourseId()).get();
    }

    public IntegerProperty dbIdProperty() {
        return new SimpleIntegerProperty(this.course.getCourseId());
    }

    public void setDbId(int dbId) {
        this.course.setCourseId(dbId);
    }

    public String getName() {
        return new SimpleStringProperty(this.course.getName()).get();
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(this.course.getName());
    }

    public void setName(String name) {
        this.course.setName(name);
    }

    public User getCoordinator() {
        return  new SimpleObjectProperty<>(this.course.getCoordinator()).get();
    }

    public ObjectProperty<User> coordinatorProperty() {
        return  new SimpleObjectProperty<>(this.course.getCoordinator());
    }

    public void setCoordinator(User coordinator) {
        this.course.setCoordinator(coordinator);
    }

    public String getStartDate() {
        return new SimpleStringProperty(this.course.getStartDate()).get();
    }

    public StringProperty startDateProperty() {
        return new SimpleStringProperty(this.course.getStartDate());
    }

    public void setStartDate(String startDate) {
        this.course.setStartDate(startDate);
    }

    public String getEndDate() {
        return new SimpleStringProperty(this.course.getEndDate()).get();
    }

    public StringProperty endDateProperty() {
        return new SimpleStringProperty(this.course.getEndDate());
    }

    public void setEndDate(String endDate) {
        this.course.setEndDate(endDate);
    }

    public ObservableList<Quiz> getQuizzes() {
        return new SimpleListProperty<>(FXCollections.observableList(this.course.getQuizzes())).get();
    }

    public ListProperty<Quiz> quizzesProperty() {
        return new SimpleListProperty<>(FXCollections.observableList(this.course.getQuizzes()));
    }

    public void setQuizzes(ObservableList<Quiz> quizzes) {
        this.course.setQuizzes(quizzes);
    }

    public ObservableList<Group> getGroups() {
        return new SimpleListProperty<>(FXCollections.observableList(this.course.getGroups())).get();
    }

    public ListProperty<Group> groupsProperty() {
        return new SimpleListProperty<>(FXCollections.observableList(this.course.getGroups()));
    }
    public IntegerProperty getTotalGroups(){
        return new SimpleIntegerProperty(getGroups().size());
    }

    public void setGroups(ObservableList<Group> groups) {
        this.course.setGroups(groups);
    }

    public void addQuiz(Quiz quiz){
            this.course.addQuiz(quiz);
    }

    public void removeQuiz(Quiz quiz){
        this.course.removeQuiz(quiz);
    }



}
