package model;

import java.util.List;

public class Teacher extends User implements Login{
    public Teacher(int userId, String firstname, String lastname) {
        super(userId, firstname, lastname);
    }

    public void getStudentResult(){

    }

    @Override
    public boolean loginUser(String username, String password) {
        return false;
    }
}
