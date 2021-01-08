package model;

import java.util.List;

public abstract class User implements Login {
    private int userId;
    private String firstname;
    private String lastname;
    private String studierichting;
    private List<Role> role;


    public User(int userId, String firstname, String lastname) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;

    }

    public void setStudierichting(String studierichting) {
        this.studierichting = studierichting;
    }

}
