package controller.fx;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import model.Answer;

public class AnswerFormFX {

    private SimpleObjectProperty<CheckBox> checkBox;
    private SimpleObjectProperty<Button> button;
    private SimpleObjectProperty<TextArea> textArea;
    private Answer answer;
    private SimpleStringProperty answerString;


    public String getAnswerString() {
        return answerString.get();
    }

    public SimpleStringProperty answerStringProperty() {
        return answerString;
    }

    public void setAnswerString(String answerString) {
        this.answerString.set(answerString);
    }

    /**
     * @author Ismael Ben Cherif
     *Is used to add JavaFX functionality and make it compatible with the answeobject
     * from the Answer class in Model.
     * @param answer
     */

    public AnswerFormFX(Answer answer) {
        this.answer = answer;
        CheckBox alfaCheckBox = new CheckBox();
        Button alfaButton = new Button();
        TextArea alfaTextArea = new TextArea();
        this.checkBox = new SimpleObjectProperty<CheckBox>(alfaCheckBox);
        this.button = new SimpleObjectProperty<Button>(alfaButton);
        this.textArea = new SimpleObjectProperty<TextArea>(alfaTextArea);
        this.answerString = new SimpleStringProperty(answer.getAnswer());
    }

    public CheckBox getCheckBox() {
        return checkBox.get();
    }

    public SimpleObjectProperty<CheckBox> checkBoxProperty() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox.set(checkBox);
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

    public TextArea getTextArea() {
        return textArea.get();
    }

    public SimpleObjectProperty<TextArea> textAreaProperty() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea.set(textArea);
    }

    public Answer getAnswer() {
        return answer;
    }
}
