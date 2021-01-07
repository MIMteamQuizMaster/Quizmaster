package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import launcher.Main;
import view.SceneManager;

public class WelcomeScene {
    public BorderPane mainPanel;
    private SceneManager sceneManager ;
    private Pane view;
    public WelcomeScene() {
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
}
