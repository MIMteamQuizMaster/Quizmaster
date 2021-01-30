package controller;

import database.mysql.DBAccess;
import database.mysql.TechnischBeheerderDAO;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import launcher.Main;
import model.Role;
import model.User;
import controller.fx.UserFx;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import java.util.*;

import static controller.fx.ObjectConvertor.*;

/**
 * @author M.J. Moshiri
 * This controller class is completly dedicated to Technical Administrator Story line
 */

public class TechnicalAdministratorController {


    public TitledPane newUserPane;
    public HBox hboxInsidePane;
    public AnchorPane rootPane;
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
    public TableColumn<UserFx, Void> col_action;
    @FXML
    private TextField richtingField;
    @FXML
    private TextField achternaamField;
    @FXML
    private TextField voornaamField;


    @FXML
    private CheckComboBox<String> rolesComboBox;
    private TechnischBeheerderDAO dao;
    private UserFx selectedUser;

    private GlyphFont glyphFont;


    public void initialize() {
        DBAccess dBaccess = Main.getDBaccess();
        this.dao = new TechnischBeheerderDAO(dBaccess);
        populateRoleMenu(); // add items to ComboBox
        refreshTable(); // add data to table
        glyphFont = GlyphFontRegistry.font("FontAwesome");
        rootPane.widthProperty().addListener(data -> bindSizeProperty());
    }


    private void bindSizeProperty() {
        table_users.maxWidthProperty().bind(rootPane.widthProperty().subtract(10));
        hboxInsidePane.minWidthProperty().bind((table_users.widthProperty().multiply(0.92)));
        col_id.prefWidthProperty().bind(table_users.widthProperty().divide(10)); // w * 1/10
        col_fname.prefWidthProperty().bind(table_users.widthProperty().divide(8)); // w * 1/8
        col_lname.prefWidthProperty().bind(table_users.widthProperty().divide(8)); // w * 1/8
        col_richting.prefWidthProperty().bind(table_users.widthProperty().divide(7)); // w * 1/7
        col_role.prefWidthProperty().bind(table_users.widthProperty().divide(5));

        col_actie.prefWidthProperty().bind(table_users.widthProperty().divide(7));
    }


    /**
     * @author M.J. Moshiri
     * Create an ObservableList of String type and add the ToString of all Enum attributes to it
     * then will fill the CheckComboBox with it
     */
    public void populateRoleMenu() {
        ObservableList<String> roleList = FXCollections.observableArrayList();
        for (Role r : Role.values()) {
            roleList.add(r.toString());
        }
        rolesComboBox.getItems().addAll(roleList);
    }


    /**
     * Empty the User table and fill it again with fresh data from dataBase
     *
     * @author M.J. Moshiri
     */
    public void refreshTable() {
        table_users.refresh();
        table_users.getItems().clear();
        ObservableList<UserFx> tableListUsers = convertUserToUserFX(dao.getAllUsers());
        col_id.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        col_fname.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        col_lname.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        col_richting.setCellValueFactory(cellData -> cellData.getValue().studieRichtingProperty());
        col_role.setCellValueFactory(cellData -> cellData.getValue().rolesProperty().asString());
        addSetCredentialBtnToUserTable();
        addEndBtnToUserTable();
        table_users.setItems(tableListUsers);

    }

    /**
     * This method add a button to each row in User table that let the technical asdministrator to end the validity
     * of a user
     *
     * @author M.J. Moshiri
     */
    private void addEndBtnToUserTable() {
        col_action.setCellFactory(cellData -> new TableCell<>() {
            private final Button delButton = new Button("");
            private final Button editButton = new Button("");
            private final HBox pane = new HBox(editButton, delButton);

            {
                col_action.prefWidthProperty().bind(pane.widthProperty().add(5));
                delButton.setGraphic(glyphFont.create(FontAwesome.Glyph.REMOVE).color(Color.RED));
                editButton.setGraphic(glyphFont.create(FontAwesome.Glyph.PENCIL).color(Color.BLUE));

                delButton.setOnAction(event ->
                {
                    boolean r = AlertHelper.confirmationDialog("Wilt u de lidmaatschap ven deze gebruiker beëindigen?");
                    if (r) {
                        UserFx u = getTableRow().getItem();
                        dao.setEnd(u.getUserObject());
                        getTableView().getItems().remove(u);
                    }

                });

                editButton.setOnAction(event -> {
                    table_users.getSelectionModel().select(getIndex());
                    selectedUser = getTableView().getItems().get(getIndex());
                    expandTitledPane(newUserPane);
                    editUserPreSetup();

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(pane);
                }

            }
        });
    }

    public void expandTitledPane(TitledPane selectedPane) {
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

    private void editUserPreSetup() {
        voornaamField.setText(selectedUser.getFirstName());
        achternaamField.setText(selectedUser.getLastName());
        richtingField.setText((selectedUser.getStudieRichting()));
        List<Role> roles = selectedUser.getRoles();
        rolesComboBox.getCheckModel().clearChecks();
        for (Role r : roles) {
            rolesComboBox.getCheckModel().check(r.toString());
        }
    }

    /**
     * the method add a btn and a hidden text field to eevry
     */
    private void addSetCredentialBtnToUserTable() {
        col_actie.setCellFactory(cellData -> new TableCell<>() {
            private final TextField passwordField = new TextField("");
            private final Button setPassBtn = new Button("Set Credential");


            {
                setPassBtn.prefWidthProperty().bind(col_actie.widthProperty().subtract(5));
                setPassBtn.setOnAction(event -> {
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
                                if (!passwordField.getText().equals(currentPassword)) {
                                    boolean r = AlertHelper.confirmationDialog("Wilt u de wachtwoord van user id:" +
                                            u.getUserId()
                                            + " te veranderen?");
                                    if (r) {
                                        savePassword(passwordField.getText(), u.getUserObject());
                                        refreshTable();
                                    }
                                }
                                setGraphic(setPassBtn);
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
                    setGraphic(setPassBtn);
                    UserFx user = getTableRow().getItem();
                    if (user != null) {
                        String pass = showPasswordString(user.getUserObject());
                        if (pass.equals("")) {
                            setPassBtn.setStyle("-fx-background-color: #f6a3a3");
                            setPassBtn.setGraphic(glyphFont.create(FontAwesome.Glyph.PLUS_CIRCLE).color(Color.GREEN));
                            setPassBtn.setText("Set Credential");
                        } else {
                            setPassBtn.setStyle("-fx-background-color: #ccffcc");
                            setPassBtn.setGraphic(glyphFont.create(FontAwesome.Glyph.EDIT).color(Color.RED));
                            setPassBtn.setText("Edit Credential");
                        }
                    }

                }

            }
        });
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
    public void btnSaveUser() {
        User user;
        if (selectedUser == null) //New
        {
            user = new User(0);
        } else {//Update
            user = selectedUser.getUserObject();
        }
        List<String> allChecked = rolesComboBox.getCheckModel().getCheckedItems();
        List<Role> roles = new ArrayList<>();
        for (String stringRole : allChecked) {
            roles.add(Role.getRole(stringRole));
        }
        user.setFirstName(voornaamField.getText());
        user.setLastName(achternaamField.getText());
        user.setStudieRichting(richtingField.getText());
        user.setRoles(roles);

        if (!(roles.size() == 0)) {
            boolean r = AlertHelper.confirmationDialog("Wilt u deze gebruiker bijwerken in de databank?");
            if (r) {
                selectedUser = null;
                dao.saveUser(user);
                refreshTable();
                expandTitledPane(newUserPane);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "een user moet altijd tenminste een role hebben.").show();
        }
    }

    /**
     * this fucntion change the visiblity of UPDATE _ CANCEL _ ADD button also empty the fields
     *
     * @param editMode boolean to determine the situation of panel
     */


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


    public void openPaneVoorNewUser(ActionEvent actionEvent) {
        cancelUpdate(); // will open the Pane and empty all fields
    }

    public void cancelUpdate() {
        expandTitledPane(newUserPane);
        voornaamField.clear();
        achternaamField.clear();
        richtingField.clear();
        rolesComboBox.getCheckModel().clearChecks();
        selectedUser = null;
    }

    public void onTableClick() {
        if (newUserPane.isExpanded()) {
            expandTitledPane(newUserPane);
        }
    }
}
