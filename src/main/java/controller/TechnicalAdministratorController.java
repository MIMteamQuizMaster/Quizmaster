package controller;

import database.mysql.DBAccess;
import database.mysql.TechnischBeheerderDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TechnicalAdministratorController {
    public Button Click;
    //    private final DBAccess dBaccess;
    public ComboBox rolesComboBox;
    public ChoiceBox choiceBox;
    public MenuButton mymenu;

    private TechnischBeheerderDAO dao;
    private HashMap<Integer, String> roles;

//    public TechnicalAdministratorController(DBAccess dBaccess) {
//        this.dBaccess = dBaccess;
////        populateRoleMenu();
//    }


    public void initialize() {
        // Initialization code can go here.
        // The parameters url and resources can be omitted if they are not needed
        populateRoleMenu();

    }

    public void populateRoleMenu() {
//        roles = dao.getMenuItems();
        System.out.println("KIIIIR");
//        for (Map.Entry<Integer, String> e : roles.entrySet()) {
//            rolesComboBox.getItems().add(e.getValue());
//        }

    }

    public void onhetClick(ActionEvent actionEvent) {
//        populateRoleMenu();

        Click.setText("kut");
    }
}
