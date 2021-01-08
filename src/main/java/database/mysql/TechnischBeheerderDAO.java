package database.mysql;

import javafx.scene.control.MenuItem;
import model.Role;

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
        String sql = "SELECT * from role";
        HashMap<Integer,String> roles = null;
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


}
