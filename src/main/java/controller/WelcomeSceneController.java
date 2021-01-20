package controller;

import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import javafx.scene.layout.Pane;
import launcher.Main;
import model.*;
import view.SceneManager;

import java.util.ArrayList;
import java.util.List;

public class WelcomeSceneController {

    public Label lnameLabel;
    public Label uidLabel;
    public Label richtingLabel;
    public Label welcomeLabel;
    public TabPane tabPanel;
    private SceneManager sceneManager;
    private Pane view;
    private User loggedInUser;

    public void initialize() {
        sceneManager = Main.getSceneManager();
        // Setting the client-object in WelcomeSceneController

        loggedInUser = (User) Main.getPrimaryStage().getUserData();

        this.welcomeLabel.setText(String.format("Welcome %s!", loggedInUser.getFirstName().toUpperCase()));
        this.lnameLabel.setText(loggedInUser.getLastName());
        this.uidLabel.setText(String.valueOf(loggedInUser.getUserId()));
        this.richtingLabel.setText(loggedInUser.getStudieRichting());

        /// set the pane according to the loged in user .
        setPane();
    }

    public void logOutClick(ActionEvent actionEvent) {
        Main.getSceneManager().showLoginScene();

    }

    /**
     * @author M.J. Moshiri
     *
     * After a successful login and retrieveing user data this method will check the dedicated roles in the user object
     * which is the LoggedInUser
     * and for each role it will add the appripriate tab based on their role to the TabPane
     */
    private void setPane() {
        List<Role> roles = this.loggedInUser.getRoles();
        if(roles.size()== 0){
            new Alert(Alert.AlertType.INFORMATION, "No role has been dedicated to you.").show();
        }else {
            for (Role r : roles) {
                if (r == Role.TECHNICAL_ADMINISTRATOR) {
                    view = sceneManager.getPage("TechnicalAdministrator");
                    Tab tab = new Tab();
                    tab.setText("Technical Administrator");
                    tab.setContent(view);
                    tabPanel.getTabs().add(tab);

                }
                else if (r == Role.STUDENT) {
                    view = sceneManager.getPage("studentSignInOut");
                    Tab tab = new Tab();
                    tab.setText("Student");
                    tab.setContent(view);
                    tabPanel.getTabs().add(tab);

                }
                else if (r == Role.COORDINATOR) {
                    view = sceneManager.getPage("coordinatorPanel");
                    Tab tab = new Tab();
                    tab.setText("Coordinator");
                    tab.setContent(view);
                    tabPanel.getTabs().add(tab);

                }
            }
        }

    }

}
