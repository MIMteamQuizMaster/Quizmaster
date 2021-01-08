package database.mysql;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDAO extends AbstractDAO {
    public LoginDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public boolean isValidUser(int username, String password) {
        String sql = "select count(*) from credentials where user_id = ? and password like binary ?";
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setInt(1, username);
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

    public User getUser(int user_id){
        String sql = "SELECT u.* , r.name as role FROM quizmaster.user u, quizmaster.user_role ur , quizmaster.role r\n" +
                "where u.user_id = ur.user_id and ur.role_id = r.id and u.user_id = ?";
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setInt(1, user_id);

            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int uid = resultSet.getInt("user_id");
                String fname = resultSet.getString("firstname");
                String lname = resultSet.getString("lastname");
                String role = resultSet.getString("role");
                try {
                    Role r = Role.getEnum(role);
                    return makeUserObject(uid,fname,lname,r);
                }catch (Exception e){
                    System.out.println("Something is wrong with user role.");
                    return null;
                }
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
        return null;
    }

    private User makeUserObject(int uid,String fname,String lname,Role r){
        User returnUser = null;
        switch (r){
            case STUDENT:
                returnUser = new Student(uid,fname,lname);
                break;
            case TEACHER:
                ///
                returnUser = new Teacher(uid,fname,lname);
                break;
            case COORDINATOR:
                ///
                returnUser = new Coordinator(uid,fname,lname);
                break;
            case ADMINISTRATOR:
                ///
                returnUser = new Administrator(uid,fname,lname);
                break;
            case TECHNICAL_ADMINISTRATOR:
                ///
                returnUser = new TechnicalAdministrator(uid,fname,lname);
                break;
        }
        return returnUser;
    }
}


