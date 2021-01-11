package controller;

import database.mysql.DBAccess;
import database.mysql.TechnischBeheerderDAO;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import launcher.Main;
import model.AlertHelper;
import model.Role;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class TechnicalAdministratorController {

    public TableView<User> table_users;
    public TableColumn<User, Integer> col_id;
    public TableColumn<User, String> col_fname;
    public TableColumn<User, String> col_lname;
    public TableColumn<User, String> col_richting;
    public TableColumn<User, Role> col_role;
    public TextField richtingField;
    public TextField achternaamField;
    public TextField voornaamField;
    public Label gebruikersidLabel;
    public Button updateUserbtn;
    public Button cancelBtn;
    public Button addUserbtn;
    public TextField passwordField;
    public Button setPassword;
    public GridPane passwordPane;
    private DBAccess dBaccess;
    public ComboBox<String> rolesComboBox;
    private TechnischBeheerderDAO dao;
    private HashMap<Integer, String> roles;
    ObservableList<User> listUsers;
    User selectedUser;


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

    public void refreshTable() {
        listUsers = dao.getAllusers();
        col_id.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        col_fname.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col_lname.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col_richting.setCellValueFactory(cellData -> cellData.getValue().studieRichtingProperty());
        col_role.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        table_users.setItems(listUsers);

    }


    public void tableSelected(MouseEvent mouseEvent) {
        if (table_users.getSelectionModel().getSelectedItem() != null) {
            this.selectedUser = table_users.getSelectionModel().getSelectedItem();
            String pass = getPasswordString();
            passwordField.setText(pass);
            voornaamField.setText(selectedUser.getFirstName());
            achternaamField.setText(selectedUser.getLastName());
            richtingField.setText((selectedUser.getStudieRichting()));
            rolesComboBox.setValue((selectedUser.getRole() == null ? "" : selectedUser.getRole().toString()));
            gebruikersidLabel.setText(String.valueOf(selectedUser.getUserId()));
            passwordField.setText(dao.getCredential(selectedUser.getUserId()));
            editMode(true);
        }
    }

    private String getPasswordString() {
        String pass = dao.getCredential(this.selectedUser.getUserId());
        if (pass == null) {
            passwordPane.setStyle("-fx-background-color: red;");
        } else {
            passwordPane.setStyle("-fx-background-color: green;");
        }
        return pass;
    }

    public void clearSelectionOnSort(SortEvent<TableView<User>> tableViewSortEvent) {
        table_users.getSelectionModel().clearSelection();
    }

    public void updateUser(ActionEvent actionEvent) {
        boolean r = AlertHelper.confirmationDialog("Wilt u deze gebruiker bijwerken in de databank?");
        if (r) {
            Role role = Role.getRole(rolesComboBox.getValue());
            selectedUser.setFirstName(voornaamField.getText());
            selectedUser.setLastName(achternaamField.getText());
            selectedUser.setStudieRichting(richtingField.getText());
            selectedUser.setRole(role);
            dao.updateUser(selectedUser);
            refreshTable();
            editMode(false);
        }
    }

    public void cancelUpdate(ActionEvent actionEvent) {
        // hide edit btn
        editMode(false);
    }

    private void editMode(boolean editMode) {
        if (editMode) {
            // SHOW update btn
            updateUserbtn.setVisible(true);
            updateUserbtn.setDisable(false);

            // SHOW cancel btn
            cancelBtn.setVisible(true);
            cancelBtn.setDisable(false);

            /// HIDE add btn
            addUserbtn.setVisible(false);
            addUserbtn.setDisable(true);
        } else {
            // HIDE update btn
            updateUserbtn.setVisible(false);
            updateUserbtn.setDisable(true);

            // hide cancel btn
            cancelBtn.setVisible(false);
            cancelBtn.setDisable(true);

            /// Show add btn
            addUserbtn.setVisible(true);
            addUserbtn.setDisable(false);

            // Empty fields
            voornaamField.setText("");
            achternaamField.setText("");
            richtingField.setText("");
            rolesComboBox.setValue("");
            gebruikersidLabel.setText("XXXXX");
            table_users.getSelectionModel().clearSelection();
        }

    }

    public User addUser(ActionEvent actionEvent) {
        boolean r = AlertHelper.confirmationDialog("Wilt u deze gebruiker toevoegen aan de databank?");

        if (r) {
            Role role = Role.getRole(rolesComboBox.getValue());
            User u = new User(0,
                    voornaamField.getText(),
                    achternaamField.getText(),
                    richtingField.getText(),
                    role);
            int id = dao.addNewUser(u);
            u.setUserId(id);
            refreshTable();
            return u;
        }
        return null;
    }

    public void setPasswordToUser(ActionEvent actionEvent) {
        if(passwordField.getText() != null){
            boolean r = AlertHelper.confirmationDialog("Wilt u de wachtwoord te veranderen?");
            if (r) {
            dao.setCredential(selectedUser.getUserId(),passwordField.getText());
                String pass = getPasswordString();
                passwordField.setText(pass);
            }
        }
    }
}
