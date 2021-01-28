package launcher;

import database.mysql.*;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Course;
import model.User;
import view.SceneManager;

public class Main extends Application {

    private static SceneManager sceneManager = null;
    private static Stage primaryStage = null;
    private static DBAccess db = null;
    public GenericDAO genericDAO;
    private static User loggedInUser;



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
        primaryStage.setResizable(false);
        primaryStage.setTitle("Make IT Work - MIM Team");
        getSceneManager().setWindowTool();
        primaryStage.show();
    }

    public static SceneManager getSceneManager() {
        if (sceneManager == null) {
            sceneManager = new SceneManager(primaryStage);
        }
        return sceneManager;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        Main.loggedInUser = loggedInUser;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
