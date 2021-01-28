package controller;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import com.google.gson.JsonParser;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import launcher.*;
import model.LoginAttempt;
import org.lightcouch.CouchDbClient;

import view.SceneManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WindowToolController {

    private final SceneManager sceneManager = Main.getSceneManager();
    private CouchDbClient dbClient;
    @FXML
    private MenuButton screenMenuButton;


    public void initialize() {
        populateScreenMenu();

        try {
            dbClient = new CouchDbClient("couchdb.properties");
        } catch (Exception e) {
            System.out.println("CouchDB user not found or CouchDB not running");
        }

        // Initialization code can go here.
        // The parameters url and resources can be omitted if they are not needed
    }

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