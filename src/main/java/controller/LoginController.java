package controller;

import com.google.gson.JsonObject;
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
import org.lightcouch.CouchDbClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoginController {
    private final DBAccess dBaccess;
    public Label warningLabel;
    private final LoginDAO dao;
    public TextField loginUsername;
    public TextField loginUnMaskedPassword;
    public PasswordField loginMaskedPassword;
    public Button passShowBtn;
    public Button loginbtn;
    public Button cancelBtn;
    private CouchDbClient dbClient;


    public LoginController() {
        this.dBaccess = Main.getDBaccess();
        this.dao = new LoginDAO(dBaccess);

        try {
            dbClient = new CouchDbClient("couchdb.properties");
        } catch (Exception e) {
            System.out.println("CouchDB user not found or CouchDB not running");
        }
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

    private void logLoginAttempt(int id, String pass) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String ip = "";
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();

        } catch (Exception e) {
            ip = "could not retrieve.";
        }
        try{
            String jsonstr = String.format("{\"ip\":\"%s\" , \"user-id\":\"%s\" , \"password\": \"%s\",\"time\": \"%s\"}", ip, id, pass, formatter.format(date));
            JsonObject jsonobj = dbClient.getGson().fromJson(jsonstr, JsonObject.class);
            dbClient.save(jsonobj);
        }catch (Exception e){
            System.out.println("couldnt save logging attemp in NoSQL");
        }



    }

    public void userLogin() {
        int userid;
        try {
            userid = Integer.parseInt(loginUsername.getText());
            String password = loginMaskedPassword.getText();
            logLoginAttempt(userid, password);
            boolean result = dao.isValidUser(userid, password);
            if (result) {
                System.out.println("login permission: " + result);

                // set logedin user data to use in different pane with appropriate permision!
                Main.getPrimaryStage().setUserData(passUser(userid));
                //
                Main.getSceneManager().showWelcome();
            } else {

                loginMaskedPassword.setStyle("-fx-border-color: #ff0000;");
                loginUnMaskedPassword.setStyle("-fx-border-color: red;");
                warningLabel.setText("De gebruikersnaam of het wachtwoord is niet correct.");
                warningLabel.setVisible(true);

            }

        } catch (Exception e) {
            warningLabel.setVisible(true);
            warningLabel.setText("Gebruikers-ID-formaat is onjuist.");
            System.out.println(e.getMessage());
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
        loginUsername.setEditable(keyEvent.getCode() == KeyCode.BACK_SPACE ||
                keyEvent.getCode().isDigitKey() ||
                keyEvent.getCode().isKeypadKey() ||
                keyEvent.getCode().isArrowKey());
        warningLabel.setVisible(false);
    }
}
