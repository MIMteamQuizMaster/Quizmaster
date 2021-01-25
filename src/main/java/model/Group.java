package model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private int dbId;
    private String name;
    private User teacher;
    private User coordinator;
    private List<User> students;
    public Group(int dbId, String name, User teacher, User coordinator, List<User> students) {
        this.dbId = dbId;
        this.name = name;
        this.teacher = teacher;
        this.coordinator = coordinator;
        this.students = students;
    }


    public Group(int dbId, String name, User teacher) {
        this.dbId = dbId;
        this.name = name;
        this.teacher = teacher;
        this.coordinator = new User(0,null,null);
        List<User> emptyUser = new ArrayList<>();
        this.students = emptyUser;
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

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public User getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public boolean addStudent(User student){
        this.students.add(student);
        return students.contains(student);
    }
    public boolean removeStudent(User student){
        if(students.contains(student))this.students.remove(student);
        return !(students.contains(student));
    }
}