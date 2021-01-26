package controller.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Role;
import model.User;

import java.util.List;

public class UserFx{
    private final User userObject;

    public UserFx(User user) {
        this.userObject = user;
    }

    public ObservableList<Role> getRoles() {
        return new SimpleListProperty<>(FXCollections.observableList(this.userObject.getRoles())).get();
    }

    public ListProperty<Role> rolesProperty() {

        return new SimpleListProperty<>(FXCollections.observableList(this.userObject.getRoles()));
    }

    public int getUserId() {
        return new SimpleIntegerProperty(userObject.getUserId()).get();
    }

    public IntegerProperty userIdProperty() {
        return new SimpleIntegerProperty(userObject.getUserId());
    }

    public String getFirstName() {
        return new SimpleStringProperty(userObject.getFirstName()).get();
    }

    public StringProperty firstNameProperty() {
        return new SimpleStringProperty(userObject.getFirstName());
    }

    public String getLastName() {
        return new SimpleStringProperty(userObject.getLastName()).get();
    }

    public StringProperty lastNameProperty() {
        return new SimpleStringProperty(userObject.getLastName());
    }

    public String getStudieRichting() {
        return new SimpleStringProperty(userObject.getStudieRichting()).get();
    }

    public StringProperty studieRichtingProperty() {
        return new SimpleStringProperty(userObject.getStudieRichting());
    }


    public void setStudieRichting(String studieRichting) {
        this.userObject.setStudieRichting(studieRichting);
    }

    public void setUserId(int userId) {
        this.userObject.setUserId(userId);
    }

    public void setFirstName(String firstName) {
        this.userObject.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.userObject.setLastName(lastName);
    }

    public void setRoles(List<Role> roles) {
        this.userObject.setRoles(roles);
    }

    public User getUserObject(){
        return this.userObject;
    }

    @Override
    public String toString() {
        return "UserFx{" +
                "userObject=" + userObject.toString() +
                '}';
    }
}
