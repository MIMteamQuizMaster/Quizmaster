package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {

    private List<Group> groups = new ArrayList<Group>();

    public Student(String name, String password, List<Role> role) {
        super(name, password, role);
    }

    @Override
    public boolean loginUser(String username, String password) {
        return false;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
