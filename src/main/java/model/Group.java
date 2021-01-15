package model;

import java.util.List;

public class Group {
    private String name;
    private Teacher teacher;
    private Coordinator coordinator;
    private List<Student> students;

    public Group(String name, Teacher teacher, Coordinator coordinator, List<Student> students) {
        this.name = name;
        this.teacher = teacher;
        this.coordinator = coordinator;
        this.students = students;
    }
    public void addStudentToGroup(Student s){
        this.students.add(s);
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}