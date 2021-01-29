package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import launcher.Main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private final Stage primaryStage;
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
        getScene("/view/fxml/windowTool.fxml");
    }

    public void showLoginScene() {
        primaryStage.setFullScreen(false);
        primaryStage.setMinWidth(380);
        primaryStage.setMinHeight(200);
        primaryStage.setWidth(400);
        primaryStage.setHeight(200);
        primaryStage.setMaxWidth(380);
        primaryStage.setMaxHeight(200);
        primaryStage.setResizable(false);
        getScene("/view/fxml/login.fxml");

    }


    public void showWelcome() {
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(800);
        primaryStage.setMaxHeight(Double.MAX_VALUE);
        primaryStage.setMaxWidth(Double.MAX_VALUE);
        getScene("/view/fxml/welcomeScene.fxml");
    }

    public void showQuiz() {
        getScene("/view/fxml/fillOutFormMultipleAnswers.fxml");
    }


}
