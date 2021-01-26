package controller;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    public static void expandTitledPane(TitledPane selectedPane) {
        HBox n = (HBox) selectedPane.getGraphic();
        Button b = new Button();
        if (n.getChildren().get(1) instanceof Button) {
            b = (Button) n.getChildren().get(1);
        }
        if (selectedPane.isExpanded()) {
            selectedPane.setCollapsible(true);
            selectedPane.setExpanded(false);
            selectedPane.setCollapsible(false);
            b.setText("Nieuw");
            b.setStyle("-fx-background-color: green");
        } else {
            selectedPane.setCollapsible(true);
            selectedPane.setExpanded(true);
            selectedPane.setCollapsible(false);
            b.setText("Afsluiten");
            b.setStyle("-fx-background-color: red");
        }
    }
}
