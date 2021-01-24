package model;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String studieRichting;
    private List<Role> roles;
    private Date delitionDate;


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

    public User(int userId) {
        this.userId = userId;
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

    public boolean isValid() {
        //tODO add body
        return true;
    }

    public Date getDelitionDate() {
        return delitionDate;
    }

    public void setDelitionDate(Date delitionDate) {
        this.delitionDate = delitionDate;
    }
    @Override
    public String toString() {
        return  firstName +" "+ lastName;

    }
}
