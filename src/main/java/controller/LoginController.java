package controller;

import database.mysql.DBAccess;
import database.mysql.LoginDAO;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import launcher.Main;
import model.User;


public class LoginController {
    private final DBAccess dBaccess;
    public Label warningLabel;
    private LoginDAO dao;
    public TextField loginUsername;
    public TextField loginUnMaskedPassword;
    public PasswordField loginMaskedPassword;
    public Button passShowBtn;
    public Button loginbtn;
    public Button cancelBtn;


    public LoginController() {
        this.dBaccess = Main.getDBaccess();
        this.dao = new LoginDAO(dBaccess);
    }

    public void showPassword(MouseEvent mouseEvent) {
        /// get the password from passwordFIeld
        /// and pass it to a text field and change the visibility
        loginUnMaskedPassword.setText(loginMaskedPassword.getText());
        loginUnMaskedPassword.setDisable(false);
        loginUnMaskedPassword.setVisible(true);

        loginMaskedPassword.setVisible(false);

    }

    public void unShowPassword(MouseEvent mouseEvent) {

        loginMaskedPassword.setVisible(true);

        loginUnMaskedPassword.setVisible(false);
        loginUnMaskedPassword.setDisable(true);

    }

    public void userLogin(ActionEvent actionEvent) {
        int userid =0;
        try{
            userid = Integer.parseInt(loginUsername.getText());
            String password = loginMaskedPassword.getText();
            boolean result = dao.isValidUser(userid, password);
            if (result) {
                Main.getSceneManager().showWelcome(passUser(userid));
            } else {

                loginMaskedPassword.setStyle("-fx-border-color: red;");
                loginUnMaskedPassword.setStyle("-fx-border-color: red;");
                warningLabel.setText("De gebruikersnaam of het wachtwoord is niet correct.");
                warningLabel.setVisible(true);

            }
            System.out.println(result);
        }
        catch (Exception e){
            warningLabel.setVisible(true);
            warningLabel.setText("Gebruikers-ID-formaat is onjuist.");
        }

    }

    public void loginCancel(ActionEvent actionEvent) {
        dBaccess.closeConnection();
        Main.getSceneManager().setWindowTool();

    }

    public User passUser(int userId) {
        // get appropriate user object
        return dao.getUser(userId);
    }

    public void passfieldInputChanged(KeyEvent inputMethodEvent) {
        loginMaskedPassword.setStyle(null);
        loginUnMaskedPassword.setStyle(null);
        warningLabel.setVisible(false);
    }

    public void onlyIntegerAcceptable(KeyEvent keyEvent) {
//        System.out.println(keyEvent.getCode());
        if (keyEvent.getCode() == KeyCode.BACK_SPACE ||
                keyEvent.getCode().isDigitKey() ||
                keyEvent.getCode().isKeypadKey() ||
                keyEvent.getCode().isArrowKey()) {
            loginUsername.setEditable(true);
        } else {
            loginUsername.setEditable(false);
        }
        warningLabel.setVisible(false);
    }
}
