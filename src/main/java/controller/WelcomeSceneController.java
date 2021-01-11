package controller;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.Pane;
import launcher.Main;
import model.*;
import view.SceneManager;

public class WelcomeSceneController {
    public BorderPane mainPanel;
    public Label fnameLabel;
    public Label lnameLabel;
    public Label uidLabel;
    public Label richtingLabel;
    public Label welcomeLabel;
    private SceneManager sceneManager ;
    private Pane view;
    private User loggedInUser;

    public void initialize() {
        sceneManager = Main.getSceneManager();
        // Setting the client-object in WelcomeSceneController

        loggedInUser = (User) Main.getPrimaryStage().getUserData();

        this.fnameLabel.setText(loggedInUser.getFirstName());
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

    private void setPane(){
        if(this.loggedInUser.getRole() == Role.TECHNICAL_ADMINISTRATOR){
            view = sceneManager.getPage("TechnicalAdministrator");
            mainPanel.setCenter(view);
        }else if (this.loggedInUser.getRole() == Role.STUDENT){
            view = sceneManager.getPage("studentSignInOut");
            mainPanel.setCenter(view);
        }
        else if (this.loggedInUser.getRole() == Role.COORDINATOR)
        {
            view = sceneManager.getPage("coordinatorPanel");
            mainPanel.setCenter(view);
        }
    }

    public void editUserInfo(ActionEvent actionEvent) {
        // TODO: give the loged in user to
    }
}
