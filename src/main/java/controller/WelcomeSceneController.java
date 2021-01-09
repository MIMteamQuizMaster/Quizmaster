package controller;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.Pane;
import launcher.Main;
import model.Student;
import model.TechnicalAdministrator;
import model.User;
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
    private User logedIn;

    public WelcomeSceneController() {
        sceneManager = Main.getSceneManager();
    }


    public void logOutClick(ActionEvent actionEvent) {
        System.out.println(logedIn);
    }

    public void setClient(User client) {
        // Setting the client-object in WelcomeSceneController
        this.logedIn = client;
        setPane();
        this.fnameLabel.setText(logedIn.getFirstName());
        this.welcomeLabel.setText(String.format("Welcome %s!",logedIn.getFirstName().toUpperCase()));
        this.lnameLabel.setText(logedIn.getLastName());
        this.uidLabel.setText(String.valueOf(logedIn.getUserId()));
        this.richtingLabel.setText(logedIn.getStudieRichting());
    }
    private void setPane(){
        if(this.logedIn instanceof TechnicalAdministrator){
            view = sceneManager.getPage("TechnicalAdministrator");

            mainPanel.setCenter(view);
        }else if (this.logedIn instanceof Student){
            view = sceneManager.getPage("studentSignInOut");

            mainPanel.setCenter(view);
        }
    }

    public void editUserInfo(ActionEvent actionEvent) {
    }
}
