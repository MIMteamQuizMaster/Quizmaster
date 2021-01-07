package launcher;

import database.mysql.DBAccess;
import javafx.application.Application;
import javafx.stage.Stage;
import view.SceneManager;

public class Main extends Application {

    private static SceneManager sceneManager = null;
    private static Stage primaryStage = null;
    private static DBAccess db = null;

    public static DBAccess getDBaccess() {
        if (db == null) {
            db = new DBAccess("QuizMasterProto", "userQuizMasterProto",
                    "pwQuizMasterProto");
        }
        return db;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // test comment first push
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("Make IT Work - Project 1");
        getSceneManager().setWindowTool();
        primaryStage.show();
    }

    public static SceneManager getSceneManager() {
        if (sceneManager == null) {
            sceneManager = new SceneManager(primaryStage);
        }
        return sceneManager;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}