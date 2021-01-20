package launcher;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import java.time.LocalDate;

public class TempPerson {

    private SimpleStringProperty firstName;
    private SimpleStringProperty lastName;
    private SimpleObjectProperty<LocalDate> date;
    private SimpleObjectProperty<Button> button;

    public TempPerson(String firstName, String lastName, LocalDate date) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.date = new SimpleObjectProperty<LocalDate>(date);
        Button thisButton = new Button("I'm fun");
        this.button = new SimpleObjectProperty<Button>(thisButton);
        addPropertystoButton();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public Button getButton() {
        return button.get();
    }

    public SimpleObjectProperty<Button> buttonProperty() {
        return button;
    }

    public void setButton(Button button) {
        this.button.set(button);
    }

    private void addPropertystoButton()
    {
        addTextToButton();
        addAction();
    }

    private void addTextToButton()
    {
        Button newButton = getButton();
        newButton.setText("Hello");
        setButton(newButton);
    }

    public void addAction()
    {
        Button newButton = getButton();
        newButton.setOnAction(this::createAction);
        setButton(newButton);
    }

    public void createAction(ActionEvent actionEvent) {
        Button newButton = getButton();
        if (newButton.getText().equalsIgnoreCase("Hello"))
        {
            newButton.setText("Bye");
        }
        else
        {
            newButton.setText("Hello");
        }
        setButton(newButton);
    }
}
