package model;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String studieRichting;
    private Role role;


    public User(int userId, String firstName, String lastName) {
        this(userId, firstName, lastName, "Onbekend", null);
    }

    public User(int userId, String firstName, String lastName, String studieRichting, Role r) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studieRichting = studieRichting;
        this.role = r;
//        this.role.add(r);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRole(Role r){
        this.role = r;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStudieRichting(String richting){
        this.studieRichting = richting;
    }

    public int getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStudieRichting() {
        return studieRichting;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", studieRichting=" + studieRichting +
                ", role=" + role +
                '}';
    }
}
