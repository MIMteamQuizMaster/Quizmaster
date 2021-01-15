package controller.fx;

import javafx.beans.property.*;
import model.Role;
import model.User;

public class UserFx{
    private final User userObject;

    public UserFx(User user) {
        this.userObject = user;
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

    public Role getRole() {
        return new SimpleObjectProperty<>(userObject.getRole()).get();
    }

    public ObjectProperty<Role> roleProperty() {
        return new SimpleObjectProperty<>(userObject.getRole());
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
        this.userObject.setFirstName(lastName);
    }

    public void setRole(Role role) {
        this.userObject.setRole(role);
    }

    public User getUserObject(){
        return this.userObject;
    }
}
