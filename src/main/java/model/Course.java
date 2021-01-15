package model;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Course {
    private int dbId;
    private String name;
    private User coordinator;
    private String startDate;
    private String endDate;
    private List<Quiz> quizzes;
    private List<Group> groups;

    public Course(String name, User coordinator) {
        this.name = name;
        this.coordinator = coordinator;
        quizzes = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public int getDbId() {
        return dbId;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void addQuiz(Quiz quiz){
        quizzes.add(quiz);
    }

    public void removeQuiz(Quiz quiz){
        quizzes.remove(quiz);
    }

    public void addGroup(Group group){
            groups.add(group);
    }

}
