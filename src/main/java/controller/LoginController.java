package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import database.mysql.DomainClass;
import database.mysql.GenericDAO;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import launcher.Main;
import model.LoginAttempt;
import model.User;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.lightcouch.CouchDbClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class LoginController implements Initializable {

    public Label warningLabel;
    public TextField loginUsername;
    public TextField loginUnMaskedPassword;
    public PasswordField loginMaskedPassword;
    public Button passShowBtn;
    public Button loginbtn;
    public Button cancelBtn;
    private CouchDbClient dbClient;
    private GenericDAO genericDao;
    private GlyphFont glyphFont;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            dbClient = new CouchDbClient("couchdb.properties");
        } catch (Exception e) {
            System.out.println("CouchDB user not found or CouchDB not running");
        }
        this.genericDao = new DomainClass();
        GlyphFont glyphFont = GlyphFontRegistry.font("FontAwesome");
        passShowBtn.setGraphic(glyphFont.create(FontAwesome.Glyph.EYE).color(Color.BROWN));
        passShowBtn.setText("");
    }

    /**
     * @author M.J. Moshiri
     * Shows the typed Password by swaping data with a text field
     */
    public void showPassword() {
        /// get the password from passwordFIeld
        /// and pass it to a text field and change the visibility
        loginUnMaskedPassword.setText(loginMaskedPassword.getText());
        loginUnMaskedPassword.setDisable(false);
        loginUnMaskedPassword.setVisible(true);
        loginMaskedPassword.setVisible(false);
    }

    /**
     * @author M.J. Moshiri
     * Hides the password by swaping visiblity back with the passwordField
     */
    public void unShowPassword() {
        loginMaskedPassword.setVisible(true);
        loginUnMaskedPassword.setVisible(false);
        loginUnMaskedPassword.setDisable(true);
    }

    /**
     * @param id that been used to login
     * @author M.J. Moshiri
     * Every logging attempt of the user to store its data in the NoSql
     */
    private void logLoginAttempt(int id) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String ip = getUserIP();
        try {
            LoginAttempt la = new LoginAttempt(id, ip, formatter.format(date));
            dbClient.save(la);
        } catch (Exception e) {
            System.out.println("couldnt sync with db loggingattempt in NoSQL");
        }

//        List<LoginAttempt> docs =  dbClient.view("_all_docs").includeDocs(true);
    }

    /**
     * @author M.J. Moshiri
     * take the ip adress of user
     * @return return the Ip aadress  and if it was unsuccesfull it will return NUll
     */
    private String getUserIP() {
        String ip;
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();
            return ip;
        } catch (Exception e) {
            System.out.println("could not retrieve ip.");
        }
        return null;
    }


    /**
     * @author M.J. Moshiri
     * the attempts of logging in which is the handler of the Login button
     * that will controll the username and password and giving permission for logging in
     */
    public void userLogin() {
        int userid;
        try {
            userid = Integer.parseInt(loginUsername.getText());
            String password = loginMaskedPassword.getText();
            logLoginAttempt(userid);
            boolean result = genericDao.isValidUser(userid, password);
            if (read() > 5) {
                new Alert(Alert.AlertType.ERROR," yo yo yo yo !!! \n slow down buddy!!! \n wacht tot het toetsenbord is afgekoeld").show();
                return;
            }
            if (result) {
                System.out.println("login permission: " + true);
                // set logedin user data to use in different pane with appropriate permision!
                Main.setLoggedInUser(passUser(userid));
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

    /**
     * Will close the Primary Stage as an act of exit
     */
    public void loginCancel() {
        Main.getPrimaryStage().close();
    }

    /**
     * @author M.J. Moshiri
     * Passes a User object appropriate with the given User ID
     *
     * @param userId that its User objcet has been asked
     * @return User Object
     */
    public User passUser(int userId) {
        // get appropriate user object
        return genericDao.getUser(userId);
    }

    /**
     * @author M.J. Moshiri
     * Reset the style of password field and remove the warning lable
     * this happens after a wrong attempt and changing hte passwordfield content
     */
    public void passfieldInputChanged() {
        loginMaskedPassword.setStyle(null);
        loginUnMaskedPassword.setStyle(null);
        warningLabel.setVisible(false);
    }

    /**
     * @param keyEvent the key that hass been pressed
     * @author M.J. Moshiri
     * it forces the User Id field to only accept Integers
     * if checks if the Key that has been press is a key from Integer group
     * also it can be tricked by holding shift and using number keys but
     * on more step of validation will be done on exit focus of the textfield
     */
    public void onlyIntegerAcceptable(KeyEvent keyEvent) {
        loginUsername.setEditable(keyEvent.getCode() == KeyCode.BACK_SPACE ||
                keyEvent.getCode().isDigitKey() ||
                keyEvent.getCode().isKeypadKey() ||
                keyEvent.getCode().isArrowKey());
        warningLabel.setVisible(false);
    }


    /**
     * @author M.J. Moshiri
     * Count the login attempts of the user base on the Ip since 10 seconds ago
     * @return the number of attemps
     */
    private int read() {
        Gson gson = new Gson();
        LoginAttempt attempt;
        Date targetTime = new Date(); // now
        int count = 0;
        String ip = getUserIP();
        List<JsonObject> all = dbClient.view("_all_docs").includeDocs(true).query(JsonObject.class);
        for (JsonObject json : all) {
            attempt = gson.fromJson(json, LoginAttempt.class);
            try {
                Date pogingDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(attempt.getDate());
                long diff = targetTime.getTime() - pogingDate.getTime();
                if (TimeUnit.MILLISECONDS.toSeconds(diff) < 10 && attempt.getIp().equals(ip)) {
                    count++;
                }
            } catch (Exception e) {
                System.out.println("Could not convert poging date");
            }
        }
        return count;
    }
}
