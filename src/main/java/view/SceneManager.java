package view;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

public class SceneManager {

    private Stage primaryStage;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Laadt een scene
    public FXMLLoader getScene(String fxml) {
        Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            return loader;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }

    public void setWindowTool() {
        FXMLLoader loader = getScene("/view/fxml/windowTool.fxml");
        if (loader != null) {
            WindowToolController controller = loader.getController();
            controller.populateScreenMenu();
        } else {
            System.out.println("set windowTool: Loader is not initialized");
            System.out.flush();
        }
    }

    public void showLoginScene() {
        getScene("/view/fxml/login.fxml");

    }

    public void showEnterQuestionScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/createUpdateQuestion.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
