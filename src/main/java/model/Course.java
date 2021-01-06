package model;

import java.util.List;

public class Course {
    private String name;
    private Coordinator coordinator;
    private List<Quiz> quizzes;
    private List<Group> groups;

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public Course(String name, Coordinator coordinator) {
        this.name = name;
        this.coordinator = coordinator;
    }
}
