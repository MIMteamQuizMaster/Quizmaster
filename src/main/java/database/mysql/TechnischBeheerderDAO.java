package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import model.Role;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TechnischBeheerderDAO extends AbstractDAO {
    public TechnischBeheerderDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public HashMap<Integer,String> getMenuItems() {
        String sql = "SELECT * FROM role";
        HashMap<Integer,String> roles = new HashMap<>();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int key = resultSet.getInt("id");
                String value = resultSet.getString("name");
                roles.put(key,value);
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return roles;
    }


    public ObservableList<User> getAllusers() {
        String sql = "SELECT u.user_id , u.firstname, u.lastname,u.studierichting , r.name FROM user u \n" +
                "left join  user_role ur on u.user_id = ur.user_id \n" +
                "left join role r on ur.role_id = r.id";
        ObservableList<User> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String fname = resultSet.getString("firstname");
                String lname = resultSet.getString("lastname");
                String richting = resultSet.getString("studierichting");
                String role = resultSet.getString("name");
                Role r = Role.getRole(role);
                rList.add(new User(user_id,fname,lname,richting,r));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

}
