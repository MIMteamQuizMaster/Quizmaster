package controller;

import database.mysql.DBAccess;
import database.mysql.TechnischBeheerderDAO;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import launcher.Main;
import model.Role;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class TechnicalAdministratorController {
    public Button Click;
    public TableView<User> table_users;
    public TableColumn<User, Integer> col_id;
    public TableColumn<User, String> col_fname;
    public TableColumn<User, String> col_lname;
    public TableColumn<User, String> col_richting;
    public TableColumn<User, Role> col_role;
    private DBAccess dBaccess;
    public ComboBox<String> rolesComboBox;
    private TechnischBeheerderDAO dao;
    private HashMap<Integer, String> roles;
    ObservableList<User> listUsers;


    public void initialize() {
        // Initialization code can go here.
        // The parameters url and resources can be omitted if they are not needed
        this.dBaccess = Main.getDBaccess();
        this.dao = new TechnischBeheerderDAO(this.dBaccess);
        populateRoleMenu();
        refreshTable();

    }

    public void populateRoleMenu() {
        this.roles = new HashMap<>();
        this.roles = dao.getMenuItems();
        for (Map.Entry<Integer, String> e : roles.entrySet()) {
            rolesComboBox.getItems().add(e.getValue());
        }
    }
    public void refreshTable(){
        listUsers = dao.getAllusers();
        col_id.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        col_fname.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col_lname.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col_richting.setCellValueFactory(cellData -> cellData.getValue().studieRichtingProperty());
        col_role.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        table_users.setItems(listUsers);

    }

    public void onhetClick(ActionEvent actionEvent) {
//        populateRoleMenu();

        Click.setText("kut");
    }
}
