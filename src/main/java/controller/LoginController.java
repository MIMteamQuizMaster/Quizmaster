package controller;

import database.mysql.DomainClass;
import database.mysql.GenericDAO;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;


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
     * @author M.J. Moshiri
     * Every logging attempt of the user to store its data in the NoSql
     * @param id that been used to login
     */
    private void logLoginAttempt(int id) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String ip = "";
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();

        } catch (Exception e) {
            ip = "could not retrieve ip.";
        }
        try {
            LoginAttempt la = new LoginAttempt(id, ip, formatter.format(date));
            dbClient.save(la);
        } catch (Exception e) {
            System.out.println("couldnt syncWithdb logging attemp in NoSQL");
        }
//        List<LoginAttempt> docs =  dbClient.view("_all_docs").includeDocs(true);
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

            if (result) {
                System.out.println("login permission: " + result);

                // set logedin user data to use in different pane with appropriate permision!
                Main.getPrimaryStage().setUserData(passUser(userid));
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
     * Passes a User object appropriate with the given User ID
     * @param userId that its User objcet has been asked
     * @return
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
     * @author M.J. Moshiri
     * it forces the User Id field to only accept Integers
     * @param keyEvent
     */
    public void onlyIntegerAcceptable(KeyEvent keyEvent) {
//        System.out.println(keyEvent.getCode());
        loginUsername.setEditable(keyEvent.getCode() == KeyCode.BACK_SPACE ||
                keyEvent.getCode().isDigitKey() ||
                keyEvent.getCode().isKeypadKey() ||
                keyEvent.getCode().isArrowKey());
        warningLabel.setVisible(false);
    }


}
