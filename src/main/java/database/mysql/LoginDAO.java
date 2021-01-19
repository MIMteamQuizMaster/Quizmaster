package database.mysql;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public User getUser(int user_id) {
        String sql = "SELECT u.*, ur.endDate , r.name as role FROM quizmaster.user u, quizmaster.user_role ur , quizmaster.role r where u.user_id = ur.user_id and ur.role_id = r.id and \n" +
                "u.user_id = ? and\n" +
                "( ur.endDate > ? or ur.endDate is null)";
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setInt(1, user_id);
            ps.setDate(2,java.sql.Date.valueOf(java.time.LocalDate.now()));

            ResultSet resultSet = executeSelectPreparedStatement(ps);
            List<Role> roles = new ArrayList<>();
            int uid =0;
            String fname = "";
            String lname = "";
            String richting = "";

            while (resultSet.next()) {
                uid = resultSet.getInt("user_id");
                fname = resultSet.getString("firstname");
                lname = resultSet.getString("lastname");
                richting = resultSet.getString("studierichting");
                Role r;
                String role = resultSet.getString("role");
                try {
                    r = Role.getRole(role);
                    System.out.println(r);

                } catch (Exception e) {
                    System.out.println("Something is wrong with user role.");
                    return null;
                }
                roles.add(r);
            }

            return new User(uid, fname, lname, richting, roles);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Something is wrong with GettingUser.");
        }
        return null;
    }

/*
    private User makeUserObject(int uid, String fname, String lname, String richting, List<Role> roles) {
        User returnUser = null;
        for (Role r : roles) {

        }
        switch (r) {
            case STUDENT:
                returnUser = new Student(uid, fname, lname);
                returnUser.setRoles(Role.STUDENT);
                break;
            case TEACHER:
                ///
                returnUser = new Teacher(uid, fname, lname);
                returnUser.setRoles(Role.TEACHER);
                break;
            case COORDINATOR:
                ///
                returnUser = new Coordinator(uid, fname, lname);
                returnUser.setRoles(Role.COORDINATOR);
                break;
            case ADMINISTRATOR:
                ///
                returnUser = new Administrator(uid, fname, lname);
                returnUser.setRoles(Role.ADMINISTRATOR);
                break;
            case TECHNICAL_ADMINISTRATOR:
                ///
                returnUser = new User(uid, fname, lname);
                returnUser.setRoles(Role.TECHNICAL_ADMINISTRATOR);
                break;
        }
        returnUser.setStudieRichting(richting);
        return returnUser;
    }
*/
}


