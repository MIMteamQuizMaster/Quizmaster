package model;

import database.mysql.DomainClass;
import database.mysql.GenericDAO;

import java.util.List;

public class Group {
    private int dbID;
    private String name;
    private User teacher;
    private List<User> students;


    public Group(String name, User teacher, List<User> students) {
        this.name = name;
        this.teacher = teacher;
        this.students = students;
    }

    public Group(int dbID, String name, User teacher) {
        this.dbID = dbID;
        this.name = name;
        this.teacher = teacher;
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getStudents() {
        return students;
    }

    public void setStudents(List<User> students) {
        this.students = students;
    }

    public int getDbID() {
        return dbID;
    }

    public void setDbID(int dbID) {
        this.dbID = dbID;
    }
}