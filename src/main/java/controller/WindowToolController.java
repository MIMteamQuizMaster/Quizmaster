package controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import launcher.Main;
import view.SceneManager;

import java.io.File;

public class WindowToolController {

    private SceneManager sceneManager = Main.getSceneManager();

    @FXML
    private MenuButton screenMenuButton;

    public void doStart() {
        sceneManager.showLoginScene();
    }

    public void populateScreenMenu() {
        String userDirectory = System.getProperty("user.dir");
        File fxmlDirectory = new File(userDirectory + "/src/main/java/view/fxml");
        if (fxmlDirectory.isDirectory()) {
            String[] files = fxmlDirectory.list();
            if (files != null) {
                for (String filename : files) {
                    if (!filename.equals("windowTool.fxml")) {
                        MenuItem menuItem = new MenuItem();
                        menuItem.setText(filename);
                        menuItem.setOnAction(event -> sceneManager.getScene("/view/fxml/" + filename));
                        screenMenuButton.getItems().add(menuItem);
                    }
                }
            }
        }
    }
}