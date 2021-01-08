package controller;

import javafx.event.ActionEvent;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javafx.scene.layout.Pane;
import launcher.Main;
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

    public void s1click(ActionEvent actionEvent) {
        System.out.println(" s1 gekeuzen");

        view = sceneManager.getPage("s1");
        mainPanel.setCenter(view);
    }

    public void s2click(ActionEvent actionEvent) {
        System.out.println(" s2 gekeuzen");

        view = sceneManager.getPage("s2");
        mainPanel.setCenter(view);
    }
    public void logOutClick(ActionEvent actionEvent) {
        System.out.println(logedIn);
    }

    public void setClient(User client) {
        // Setting the client-object in WelcomeSceneController
        this.logedIn = client;
        this.fnameLabel.setText(logedIn.getFirstname());
        this.welcomeLabel.setText(String.format("Welcome %s!",logedIn.getFirstname().toUpperCase()));
        this.lnameLabel.setText(logedIn.getLastname());
        this.uidLabel.setText(String.valueOf(logedIn.getUserId()));
        this.richtingLabel.setText(logedIn.getStudierichting());
    }
}
