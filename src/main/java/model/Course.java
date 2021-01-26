package model;
import database.mysql.DomainClass;
import database.mysql.GenericDAO;
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
    private GenericDAO genericDAO;

    public Course() {
        this(0,"",null,"","");
    }
    public Course(String name, User coordinator) {
        this(0,name,coordinator,"","");
    }
    public Course(int dbId, String name, User coordinator, String startDate, String endDate) {
        this.dbId = dbId;
        this.name = name;
        this.coordinator = coordinator;
        this.startDate = startDate;
        this.endDate = endDate;
        quizzes = new ArrayList<>();
        groups = new ArrayList<>();
        genericDAO = new DomainClass();
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

    public void getGroupsFromDb(){
        this.groups = genericDAO.getGroupsOfCourse(this);
    }

    /**
     * @should add a group to a course.
     * @param group
     */
    public void addGroup(Group group){
        groups.add(group);
    }

    public boolean saveToDB(){
        return genericDAO.saveCourse(this);
    }
    public boolean deleteMe(){
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s", this.name);
    }
}
