package model;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import java.sql.Date;

public class Course {
    private IntegerProperty dbId;
    private StringProperty name;
    private ObjectProperty<User> coordinator;
    private StringProperty startDate;
    private StringProperty endDate;
    private ListProperty<Quiz> quizzes;
    private ListProperty<Group> groups;

    public Course(String name, User coordinator) {
        this.name = new SimpleStringProperty(name);
        this.coordinator = new SimpleObjectProperty<>(coordinator);
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
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

    public String getStartDate() {
        return startDate.get();
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = new SimpleStringProperty(startDate);
//        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate= new SimpleStringProperty(endDate);
    }

    public ObservableList<Quiz> getQuizzes() {
        return quizzes.get();
    }

    public ListProperty<Quiz> quizzesProperty() {
        return quizzes;
    }

    public void setQuizzes(ObservableList<Quiz> quizzes) {
        this.quizzes.set(quizzes);
    }

    public ObservableList<Group> getGroups() {
        return groups.get();
    }

    public ListProperty<Group> groupsProperty() {
        return groups;
    }

    public void setGroups(ObservableList<Group> groups) {
        this.groups.set(groups);
    }

    public boolean addQuiz(Coordinator coordinator, Quiz quiz){
        if (coordinator == this.getCoordinator()){
            quizzes.add(quiz);
            return true;
        }
        return false;
    }

    public boolean addGroup(Coordinator coordinator,Group group){
        if (coordinator == this.getCoordinator()){
            groups.add(group);
            return true;
        }
        return false;
    }

}
