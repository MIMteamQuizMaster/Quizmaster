package model;

import java.util.List;

public class Group {
    private final String name;
    private User teacher;
    private User coordinator;
    private final List<User> students;

    public Group(String name, User teacher, User coordinator, List<User> students) {
        this.name = name;
        this.teacher = teacher;
        this.coordinator = coordinator;
        this.students = students;
    }

}