package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import launcher.TempPerson;

import java.time.LocalDate;

public class FillOutFormNewController{

    public TableColumn<TempPerson,String> firstColumn;
    public TableColumn<TempPerson, LocalDate> thirdColumn;
    public TableColumn<TempPerson, String> secondColumn;
    public TableView<TempPerson> answerTable;
    public Button nextButton;
    public TableColumn<TempPerson, Button> fourthColumn;

    public void initialize()
    {
        thirdColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        firstColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        secondColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        fourthColumn.setCellValueFactory(cellData -> cellData.getValue().buttonProperty());

        answerTable.setItems(getAnswerClassFX());
    }

    private ObservableList<launcher.TempPerson> getAnswerClassFX()
    {
        ObservableList<launcher.TempPerson> answerClassFXES = FXCollections.observableArrayList();
        answerClassFXES.add(new TempPerson("Erikaa", "Green", LocalDate.of(2010,5,7)));
        answerClassFXES.add(new launcher.TempPerson("Manny", "Delgado", LocalDate.of(2000,5,8)));
        answerClassFXES.add(new launcher.TempPerson("Jon", "Do", LocalDate.of(1999,5,8)));
        return answerClassFXES;
    }


    public void nextButtonAction(ActionEvent actionEvent) {

    }
}

