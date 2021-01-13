package controller;

import database.mysql.DBAccess;
import database.mysql.TechnischBeheerderDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import launcher.Main;
import model.Role;
import model.User;
import controller.fx.UserFx;
import static controller.fx.ObjectConvertor.*;


public class TechnicalAdministratorController {
    @FXML
    private TableView<UserFx> table_users;
    @FXML
    private TableColumn<UserFx, Integer> col_id;
    @FXML
    private TableColumn<UserFx, String> col_fname;
    @FXML
    private TableColumn<UserFx, String> col_lname;
    @FXML
    private TableColumn<UserFx, String> col_richting;
    @FXML
    private TableColumn<UserFx, Role> col_role;
    @FXML
    private TextField richtingField;
    @FXML
    private TextField achternaamField;
    @FXML
    private TextField voornaamField;
    @FXML
    private Label gebruikersidLabel;
    @FXML
    private Button updateUserbtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button addUserbtn;
    @FXML
    private TextField passwordField;
    @FXML
    private GridPane passwordPane;
    @FXML
    private ComboBox<String> rolesComboBox;
    private TechnischBeheerderDAO dao;
    private UserFx selectedUser;


    public void initialize() {
        // Initialization code can go here.
        // The parameters url and resources can be omitted if they are not needed
        DBAccess dBaccess = Main.getDBaccess();
        this.dao = new TechnischBeheerderDAO(dBaccess);
        populateRoleMenu(); // add items to ComboBox
        refreshTable(); // add data to table
    }


    /**
     * Get the roles frome Database and fill the ComboBox for User.
     */
    public void populateRoleMenu() {
        ObservableList<String> roleList = FXCollections.observableArrayList();
        for (Role r : Role.values()) {
            roleList.add(r.toString());
        }
        rolesComboBox.setItems(roleList);
    }




    /**
     *  add or refresh the table of users
     */
    public void refreshTable() {
        table_users.getItems().clear();
        ObservableList<UserFx> tableListUsers = convertUserToUserFX(dao.getAllusers());
        col_id.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        col_fname.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col_lname.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col_richting.setCellValueFactory(cellData -> cellData.getValue().studieRichtingProperty());
        col_role.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        table_users.setItems(tableListUsers);

    }

    /**
     * if a row in the user table is selected , the function add all information of the users to the TexTFields
     * and active the editmode
     *
     */
    public void tableSelected() {
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

    /**
     * Check existence of credentials for the selected user in the table
     * Also change the color of pane appropriate with the result of search
     * @return the password of searched user
     */
    private String getPasswordString() {
        String pass = dao.getCredential(this.selectedUser.getUserId());
        if (pass == null) {
            passwordPane.setStyle("-fx-background-color: red;");
        } else {
            passwordPane.setStyle("-fx-background-color: green;");
        }
        return pass;
    }

    /**
     * clear the selection if the user change the table sort
     *
     */
    public void clearSelectionOnSort() {
        table_users.getSelectionModel().clearSelection();
    }

    /**
     * Update the user information given in the field to the Database
     */
    public void updateUser() {
        boolean r = AlertHelper.confirmationDialog("Wilt u deze gebruiker bijwerken in de databank?");
        if (r) {
            Role role = Role.getRole(rolesComboBox.getValue());
            selectedUser.setFirstName(voornaamField.getText());
            selectedUser.setLastName(achternaamField.getText());
            selectedUser.setStudieRichting(richtingField.getText());
            selectedUser.setRole(role);

            // Selected user is UserFX but dao Parameter is User
            // the User Objet is stored in UserFX so

            dao.updateUser(selectedUser.getUserObject());
            refreshTable();
            editMode(false);
        }
    }

    /**
     * change the edit mode to skip accidentally update command
     */
    public void cancelUpdate() {
        // hide edit btn
        editMode(false);
    }

    /**
     * this fucntion change the visiblity of UPDATE _ CANCEL _ ADD button also empty the fields
     * @param editMode boolean to determine the situation of panel
     */
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

    /**
     * takes the values in textfield and add a new user to de database
     *
     * @return the object of made user
     */
    public User addUser() {
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

    /**
     * add credential to user in the Database
     *
     */
    public void setPasswordToUser() {
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
