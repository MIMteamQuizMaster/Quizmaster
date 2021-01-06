package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {
    public TextField loginUsername;
    public TextField loginUnMaskedPassword;
    public PasswordField loginMaskedPassword;
    public Button passShowBtn;
    public Button loginbtn;
    public Button cancelBtn;

    public void showPassword(MouseEvent mouseEvent) {

        loginUnMaskedPassword.setText(loginMaskedPassword.getText());

        loginUnMaskedPassword.setVisible(true);
        loginMaskedPassword.setVisible(false);
    }

    public void unShowPassword(MouseEvent mouseEvent) {
        loginMaskedPassword.setVisible(true);
        loginUnMaskedPassword.setVisible(false);
    }

    public void userLogin(ActionEvent actionEvent) {
    }

    public void loginCancel(ActionEvent actionEvent) {
    }
}
