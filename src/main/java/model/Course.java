package model;

import java.util.List;

public class Course {
    private int dbId;
    private String name;
    private Coordinator coordinator;


    private String startDate;
    private String endDate;
    private List<Quiz> quizzes;
    private List<Group> groups;

    public Course(String name, Coordinator coordinator) {
        this.name = name;
        this.coordinator = coordinator;
    }

    public boolean addQuiz(Coordinator coordinator, Quiz quiz) {
        if (coordinator == this.coordinator) {
            quizzes.add(quiz);
            return true;
        }
        return false;
    }

    public boolean addGroup(Coordinator coordinator, Group group) {
        if (coordinator == this.coordinator) {
            groups.add(group);
            return true;
        }
        return false;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public String getName() {
        return name;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
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
}