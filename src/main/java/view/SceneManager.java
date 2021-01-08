package view;

import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import launcher.Main;
import model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private Stage primaryStage;
    private Pane view;

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

    public Pane getPage(String fileName){
        try{
            URL fileUrl = Main.class.getResource("/view/fxml/" + fileName + ".fxml");
            if(fileUrl ==null){
                throw new FileNotFoundException("FXML file not found.");
            }
            view = FXMLLoader.load(fileUrl);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return view;
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

    public void showWelcome(User logedIn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/welcomeScene.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            WelcomeSceneController wsc = loader.getController();
            wsc.setClient(logedIn); // Passing the client-object to the ClientViewController

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
