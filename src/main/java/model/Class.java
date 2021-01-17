package model;

import java.util.List;

public class Class {

    private int dbId;
    private String name;
    private Teacher teacher;

    public Class(int dbId, Teacher teacher) {
        this.dbId = dbId;
        this.teacher = teacher;
    }


    public int getDbId() {
        return dbId;
    }


    public void setDbId(int dbId) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Object getTeacher() {
        return teacher;
    }
}
