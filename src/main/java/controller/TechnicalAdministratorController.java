package controller;

import database.mysql.DBAccess;
import database.mysql.TechnischBeheerderDAO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.util.Callback;
import launcher.Main;
import model.Role;
import model.User;
import controller.fx.UserFx;
import org.controlsfx.control.CheckComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private TableColumn<UserFx, String> col_role;
    public TableColumn<UserFx, Void> col_actie;
    public TableColumn<UserFx,Void> col_delete;
    @FXML
    private TextField richtingField;
    @FXML
    private TextField achternaamField;
    @FXML
    private TextField voornaamField;

    @FXML
    private Button updateUserbtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button addUserbtn;

    @FXML
    private CheckComboBox<String> rolesComboBox;
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
     * Get the roles fill the ComboBox for User.
     */
    public void populateRoleMenu() {
        ObservableList<String> roleList = FXCollections.observableArrayList();
        for (Role r : Role.values()) {
            roleList.add(r.toString());
        }
        rolesComboBox.getItems().addAll(roleList);
    }


    /**
     * add or refresh the table of users
     */
    public void refreshTable() {
        table_users.refresh();
        table_users.getItems().clear();
        ObservableList<UserFx> tableListUsers = convertUserToUserFX(dao.getAllusers());

        col_id.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        col_fname.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col_lname.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col_richting.setCellValueFactory(cellData -> cellData.getValue().studieRichtingProperty());
        col_role.setCellValueFactory(cellData -> cellData.getValue().rolesProperty().asString());
        addSetCredentialBtnToUserTable();
        col_delete.setCellFactory(cellData -> new TableCell<UserFx,Void>(){
            private final Button delButton = new Button("end");

            {
                delButton.setOnAction(event ->
                {
                    boolean r = AlertHelper.confirmationDialog("Wilt u de lidmaatschap ven deze gebruiker beëindigen?");
                    if(r){
                        UserFx u = getTableRow().getItem();
                        dao.setEnd(u.getUserObject());
                        refreshTable();
                    }

                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(delButton);
                }

            }
        });
        table_users.setItems(tableListUsers);

    }

    private void addSetCredentialBtnToUserTable() {
        col_actie.setCellFactory(cellData -> new TableCell<UserFx, Void>() {
            private final Button editButton = new Button("Set Credential");
            private final TextField passwordField = new TextField("");
            {
                editButton.setOnAction(event -> {
                    UserFx u = getTableRow().getItem();
                    this.setGraphic(passwordField);
                    String currentPassword = showPasswordString(u.getUserObject());
                    Tooltip tooltip = new Tooltip("From the moment you see this field, you have 10 second to type the password.\n It will be Automatically saved.");
                    passwordField.setText(currentPassword);
                    passwordField.setTooltip(tooltip);
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            Platform.runLater(() -> {

                                    if(!passwordField.getText().equals(currentPassword)){
                                        boolean r = AlertHelper.confirmationDialog("Wilt u de wachtwoord van user id:" +
                                                u.getUserId()
                                                +" te veranderen?");
                                        if (r) {
                                            savePassword(passwordField.getText(), u.getUserObject());
                                            refreshTable();
//                                            editButton.setStyle("-fx-background-color: #ccffcc");
//                                            editButton.setText("Edit Credential");
                                        }
                                    }
                                setGraphic(editButton);

                            });
                        }
                    };
                    timer.schedule(task, 10000L);

                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(editButton);
                    UserFx user = getTableRow().getItem();
                    String pass = showPasswordString(user.getUserObject());
                    if (pass.equals("")) {
                        editButton.setStyle("-fx-background-color: #f6a3a3");
                        editButton.setText("Set Credential");
                    } else {
                        editButton.setStyle("-fx-background-color: #ccffcc");
                        editButton.setText("Edit Credential");
                    }
                }

            }
        });
    }

    /**
     * if a row in the user table is selected , the function add all information of the users to the TexTFields
     * and active the editmode
     */
    public void tableSelected() {
        if (table_users.getSelectionModel().getSelectedItem() != null) {
            this.selectedUser = table_users.getSelectionModel().getSelectedItem();

            voornaamField.setText(selectedUser.getFirstName());
            achternaamField.setText(selectedUser.getLastName());
            richtingField.setText((selectedUser.getStudieRichting()));
            List<Role> roles = selectedUser.getRoles();
            rolesComboBox.getCheckModel().clearChecks();
            for (Role r : roles) {
                rolesComboBox.getCheckModel().check(r.toString());
            }

            editMode(true);
        }
    }


    /**
     * clear the selection if the user change the table sort
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
            List<String> allChecked = rolesComboBox.getCheckModel().getCheckedItems();
            List<Role> roles = new ArrayList<>();

            for (String stringRole : allChecked) {
                roles.add(Role.getRole(stringRole));
            }

            selectedUser.setFirstName(voornaamField.getText());
            selectedUser.setLastName(achternaamField.getText());
            selectedUser.setStudieRichting(richtingField.getText());

            selectedUser.setRoles(roles);

            // Selected user is UserFX but dao Parameter is User
            // the User Objet is stored in UserFX so

            dao.saveUser(selectedUser.getUserObject());
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
     *
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
            List<String> allChecked = rolesComboBox.getCheckModel().getCheckedItems();
            List<Role> roles = new ArrayList<>();

            for (String stringRole : allChecked) {
                roles.add(Role.getRole(stringRole));
            }
            User u = new User(0,
                    voornaamField.getText(),
                    achternaamField.getText(),
                    richtingField.getText(),
                    roles);
            u = dao.saveUser(u);

            refreshTable();
            return u;
        }
        return null;
    }

    /**
     * add credential to user in the Database
     */
    public void savePassword(String text, User u) {
        if (!text.equals("")) {
            dao.setCredential(u.getUserId(), text);
        }
    }

    /**
     * Check existence of credentials for the selected user in the table
     * Also change the color of pane appropriate with the result of search
     *
     * @return the password of searched user
     */
    private String showPasswordString(User u) {
        return dao.getCredential(u.getUserId());
    }


}
