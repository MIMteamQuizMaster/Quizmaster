package model;

import java.util.List;

public class Teacher extends User implements Login{
    public Teacher(String name, String password, List<Role> role) {
        super(name, password, role);
    }

    public void getStudentResult(){

    }

    @Override
    public boolean loginUser(String username, String password) {
        return false;
    }
}
