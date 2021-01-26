package database.mysql;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDAO extends AbstractDAO {
    public UserDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    /**
     * @param username an int dedicating userID
     * @param password a string containing the password of user
     * @return true id the user is valid otherwise false
     * @author M.J. Moshiri
     * <p>
     * Checks if the given username or password is from a valid user
     * @should return false if the password of the user is wrong
     * @should return false if it failed in retrieving data from db
     */
    public boolean isValidUser(int username, String password) {
        String sql = "select count(*) from credentials c\n" +
                "join user u on u.user_id = c.user_id \n" +
                "where c.user_id = ? and c.password like binary ? and (u.deletionDate > ? or u.deletionDate is null)";
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setInt(1, username);
            ps.setString(2, password);
            ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
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

    /**
     * @param user_id an int which is the ID of the user
     * @return a userObject with all important data for further use
     * @author M.J. Moshiri
     * <p>
     * Creates a user object from the given user id with alle information that are stored in db from the databes
     * this user object will be used furthur in thet application to for showing correct panels of retrieving appropriate date
     * @should return null if it failed retrieving data from DB
     */
    public User getUser(int user_id) {
        String sql = "SELECT u.*, ur.endDate , r.name as role FROM quizmaster.user u, quizmaster.user_role ur , quizmaster.role r where u.user_id = ur.user_id and ur.role_id = r.id and \n" +
                "u.user_id = ? and\n" +
                "( ur.endDate > ? or ur.endDate is null) group by r.name";
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setInt(1, user_id);
            ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));

            ResultSet resultSet = executeSelectPreparedStatement(ps);
            List<Role> roles = new ArrayList<>();
            int uid = 0;
            String fname = "";
            String lname = "";
            String richting = "";
            Date deletionDate = null;

            while (resultSet.next()) {
                uid = resultSet.getInt("user_id");
                fname = resultSet.getString("firstname");
                lname = resultSet.getString("lastname");
                richting = resultSet.getString("studierichting");
                deletionDate = resultSet.getDate("deletiondate");
                Role r;
                String role = resultSet.getString("role");
                try {
                    r = Role.getRole(role);
                } catch (Exception e) {
                    System.out.println("Something is wrong with user role.");
                    return null;
                }
                roles.add(r);
            }
            if(uid!=0){return new User(uid, fname, lname, richting, roles);}

        } catch (
                SQLException throwables) {
            System.out.println(throwables.getMessage() + "Something is wrong with GettingUser.");
        }
        return null;
    }

    public List<Integer> getListOfTeachers()
    {
        List<Integer> listOfTeachers = new ArrayList<>();
        String sql = "SELECT user_id, role_id FROM user_role " +
                "WHERE role_id = 2;";
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
            while (resultSet.next())
            {
                int teacherId = resultSet.getInt(1);
                listOfTeachers.add(teacherId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return listOfTeachers;
    }


}


