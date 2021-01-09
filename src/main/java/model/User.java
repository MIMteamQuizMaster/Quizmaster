package model;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty userId;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty studieRichting;
    private final ObjectProperty<Role> role;


    public User(int userId, String firstName, String lastName) {
        this(userId, firstName, lastName,"Onbekend",null);
    }

    public User(int user_id, String fname, String lname, String richting, Role r) {
        this.userId = new SimpleIntegerProperty(user_id);
        this.firstName = new SimpleStringProperty(fname);
        this.lastName = new SimpleStringProperty(lname);
        this.studieRichting = new SimpleStringProperty(richting);
        this.role = new SimpleObjectProperty<>(r);
    }

    public int getUserId() {
        return userId.get();
    }

    public IntegerProperty userIdProperty() {
        return userId;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getStudieRichting() {
        return studieRichting.get();
    }

    public StringProperty studieRichtingProperty() {
        return studieRichting;
    }

    public Role getRole() {
        return role.get();
    }

    public ObjectProperty<Role> roleProperty() {
        return role;
    }
}
