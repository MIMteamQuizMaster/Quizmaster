package database.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO extends AbstractDAO {
    public LoginDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public boolean getUser(String username, String password) {
        String sql = "select count(*) from user where name = ? and password like binary ?";
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                if (resultSet.getInt("count(*)") == 1) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

        return false;
    }
}


