package model;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String studieRichting;
    private List<Role> roles;


    public User(int userId, String firstName, String lastName) {
        this(userId, firstName, lastName, "Onbekend", new ArrayList<>());
    }

    public User(int userId, String firstName, String lastName, String studieRichting, List<Role> r) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studieRichting = studieRichting;
        this.roles = r;

    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRoles(List<Role> r){
        this.roles = r;
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

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", studieRichting=" + studieRichting +
                ", role=" + roles +
                '}';
    }
}
