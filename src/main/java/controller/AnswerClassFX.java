package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import model.Answer;

public class AnswerClassFX extends Answer {

    private Button button = new Button();
    private CheckBox checkBox = new CheckBox();
    private TextArea textArea = new TextArea();

    public AnswerClassFX(boolean isCorrect, String answer) {
        super(isCorrect, answer);
        this.checkBox.setDisable(true);
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public void setButtonProperies()
    {
        button.setOnAction(this::menuButtonAction);
    }

    public void menuButtonAction(ActionEvent actionEvent) {
        if (!this.getIsGivenAnswer())
        {
            this.setGivenAnswer(true);
            this.checkBox.setSelected(true);
        }
        else
        {
            this.setGivenAnswer(false);
            this.checkBox.setSelected(false);
        }
    }



}
