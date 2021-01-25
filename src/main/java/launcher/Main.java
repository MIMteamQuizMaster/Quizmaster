package launcher;

import database.mysql.CourseDAO;
import database.mysql.DBAccess;
import database.mysql.DbInfo;
import database.mysql.QuestionDAO;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Course;
import view.SceneManager;

public class Main extends Application {

    private static SceneManager sceneManager = null;
    private static Stage primaryStage = null;
    private static DBAccess db = null;

    public static DBAccess getDBaccess() {
        if (db == null) {
            db = new DBAccess(DbInfo.DATABASE_NAAM, DbInfo.DB_USERNAME,
                    DbInfo.DB_USER_PASSWORD);
        }
        return db;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // test comment first push
        DBAccess dbAccess = getDBaccess();
        dbAccess.openConnection();

        Main.primaryStage = primaryStage;
        primaryStage.setResizable(true);
        primaryStage.setTitle("Make IT Work - Project 1");

        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setMaxWidth(1920);
        primaryStage.setMaxHeight(1080);


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