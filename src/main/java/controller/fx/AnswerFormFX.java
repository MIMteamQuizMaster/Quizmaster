package controller.fx;

import controller.FillOutFormMultipleAnswersController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import model.Answer;

public class AnswerFormFX {

    private SimpleObjectProperty<CheckBox> checkBox;
    private SimpleObjectProperty<Button> button;
    private SimpleObjectProperty<TextArea> textArea;
    private Answer answer;


    public AnswerFormFX(Answer answer) {
        this.answer = answer;
        CheckBox alfaCheckBox = new CheckBox();
        Button alfaButton = new Button();
        TextArea alfaTextArea = new TextArea();
        this.checkBox = new SimpleObjectProperty<CheckBox>(alfaCheckBox);
        this.button = new SimpleObjectProperty<Button>(alfaButton);
        this.textArea = new SimpleObjectProperty<TextArea>(alfaTextArea);
/*        setTableViewAtrributes();*/
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

/*    public void setTableViewAtrributes()
    {
        setAnswerText();
        actionOnButtonClick();
        setCheckBoxAttributes();
    }

    private void setAnswerText()
    {
        TextArea newTextArea = getTextArea();
        newTextArea.setPrefHeight(40);
        newTextArea.setEditable(false);
        newTextArea.setText(this.answer.getAnswer());
        setTextArea(newTextArea);
    }

    private void actionOnButtonClick()
    {
        Button newButton = getButton();
        newButton.setOnAction(this::setActioOnButtonClick);
        setButton(newButton);
    }

    private void setActioOnButtonClick(ActionEvent actionEvent)
    {
        if (!this.answer.getIsGivenAnswer())
        {
            this.answer.setGivenAnswer(true);

            setTableViewAtrributes();
        }
        else
        {
            this.answer.setGivenAnswer(false);
            setTableViewAtrributes();

        }
    }


    private void setCheckBoxAttributes()
    {
        CheckBox newCheckBox = getCheckBox();
        newCheckBox.setDisable(true);
        if (this.answer.getIsGivenAnswer())
        {
            newCheckBox.setSelected(true);
        }
        else
        {
            newCheckBox.setSelected(false);
        }
        setCheckBox(newCheckBox);
    }*/
}
