package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    private List<Group> groups = new ArrayList<Group>();

    public Student(int userId, String firstname, String lastname) {
        super(userId, firstname, lastname);
    }

    @Override
    public boolean loginUser(String username, String password) {
        return false;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
