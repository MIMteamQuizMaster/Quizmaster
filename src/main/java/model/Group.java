package model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private int dbId;
    private String name;
    private User teacher;
    private List<User> students;

    public Group(int dbId, String name, User teacher, List<User> students) {
        this.dbId = dbId;
        this.name = name;
        this.teacher = teacher;
        this.students = students;
    }
    public Group(int dbId, String name, User teacher) {
        this(dbId,name,teacher,new ArrayList<>());
    }

    public Group(int dbId) {
        this(dbId,"",null,new ArrayList<>());
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