package controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

import java.util.Optional;

public class AlertHelper {
    public static void dialog(Alert.AlertType alertType, String statement) {
        Alert alert = new Alert(alertType, statement);
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static boolean confirmationDialog(String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Weet je het zeker?");
        alert.setContentText(text);
        Optional<ButtonType> option = alert.showAndWait();
        return ButtonType.OK.equals(option.get());
    }
}
