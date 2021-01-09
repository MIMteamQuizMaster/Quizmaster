package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    private final List<Group> groups = new ArrayList<>();

    public Student(int userId, String firstname, String lastname) {
        super(userId, firstname, lastname);
    }


    public List<Group> getGroups() {
        return groups;
    }

}
