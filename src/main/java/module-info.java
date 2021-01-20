module QuizMaster {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.sql;
    requires mysql.connector.java;
    requires org.controlsfx.controls;
    requires org.junit.jupiter.api;
    requires lightcouch;
    requires com.google.gson;

    opens view to javafx.graphics, javafx.fxml;
    opens launcher to javafx.graphics, javafx.fxml;
    opens controller to javafx.fxml;
    opens model to gson, javafx.base;
}