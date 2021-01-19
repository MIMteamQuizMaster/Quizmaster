package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Role;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class TechnischBeheerderDAO extends AbstractDAO {
    public TechnischBeheerderDAO(DBAccess dBaccess) {
        super(dBaccess);
    }


    public ObservableList<User> getAllusers() {
        String sql = "SELECT DISTINCT u.user_id , u.firstname, u.lastname,u.studierichting FROM user u where deletionDate > ? or deletionDate is null";

        ObservableList<User> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setDate(1,java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String fname = resultSet.getString("firstname");
                String lname = resultSet.getString("lastname");
                String richting = resultSet.getString("studierichting");
//                String role = resultSet.getString("name");
//                Role r = Role.getRole(role);
                User u = new User(user_id, fname, lname, richting, new ArrayList<Role>());
                HashMap<Integer, Role> userRoles = getUserRoles(u);
                u.setRoles(userRoles.values().stream().collect(Collectors.toList()));
                rList.add(u);
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while getting all users");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    private HashMap<Integer, Role> getUserRoles(User u) {
//        List<Role> roles = new ArrayList<>();
        HashMap<Integer, Role> roles = new HashMap<>();
        String query = "SELECT ur.* , r.name FROM user_role ur left join role r on ur.role_id = r.id \n" +
                "where ur.user_id = ? and ( ur.endDate > ? or ur.endDate is null )";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, u.getUserId());
            ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()) {
                String role = rs.getString("name");
                Role r = Role.getRole(role);
                int dbid = rs.getInt("id");
                roles.put(dbid, r);
            }

            return roles;
        } catch (SQLException throwables) {
            System.out.println("Somthing wrong while retrieving roles of user ");
        }
        return null;

    }

    public User saveUser(User u) {
        String query;
        if (u.getUserId() == 0) { // then its a new Question
            query = "INSERT INTO user (firstname, lastname, studierichting, user_id) VALUES (?,?,?,?)";
        } else {// it is update case
            query = "UPDATE user SET firstname = ? ,lastname = ?, studierichting =? WHERE user_id = ?";
        }

        int key = 0;
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getStudieRichting());
            ps.setInt(4, u.getUserId());
            key = executeInsertPreparedStatement(ps);
            if(u.getUserId() == 0){
                u.setUserId(key);
            }
            setRoleToUser(u, u.getRoles());
            return u;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while adding/Updating new user");
            throwables.printStackTrace();
        }
        return null;
    }


    public int getRoleId(String roleString) {
        String query = "SELECT id FROM role where name = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setString(1, roleString);
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

    private void setEndToRole(int dbid) {
        String query = "update user_role set enddate = ? where id = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.setInt(2, dbid);
            executeManipulatePreparedStatement(ps);

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while assigning End date to user role");
            throwables.printStackTrace();
        }
    }


    public void setRoleToUser(User u, List<Role> roles) {
        HashMap<Integer, Role> userAlreadyHaveRoles = getUserRoles(u);
        List<Role> userNewRoles = u.getRoles();

        assert userAlreadyHaveRoles != null;
        for (Integer i : userAlreadyHaveRoles.keySet()) {
            if (!userNewRoles.contains(userAlreadyHaveRoles.get(i))) {
                // if user had a role that isnt in the list van new roles
                setEndToRole(i);
            }
        }
        for (Role r : roles) {
            if (!userAlreadyHaveRoles.containsValue(r)) {
                String query = "INSERT INTO user_role (user_id,role_id, startDate) VALUES (?,?,?)";
                int roleId = getRoleId(r.toString());
                try {
                    PreparedStatement ps = getStatement(query);
                    ps.setInt(1, u.getUserId());
                    ps.setInt(2, roleId);
                    ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                    executeManipulatePreparedStatement(ps);

                } catch (SQLException throwables) {
                    System.out.println(throwables.getMessage() + " Somthing wrong while assigning role to user");
                    throwables.printStackTrace();
                }
            }

        }

    }

    public String getCredential(int userID) {
        String query = "SELECT * FROM credentials where user_id = ?";
        String returnString = "";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, userID);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
//                int user_id = resultSet.getInt("user_id");
                returnString = resultSet.getString("password");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while getting credentials");
        }
        return returnString;
    }

    public void setEnd(User u){
        try {
            String query = "UPDATE user set deletionDate = ? WHERE user_id = ?";
            PreparedStatement ps = getStatement(query);
            ps.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.setInt(2, u.getUserId());
            executeManipulatePreparedStatement(ps);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while ending user membership");
        }
    }

    public void setCredential(int userID, String password) {
        try {
            String query = "";
            if (getCredential(userID).equals("")) {
                query = "INSERT INTO credentials (password,user_id) values (?,?)";
            } else {
                query = "UPDATE credentials set password = ? WHERE user_id = ?";
            }
            PreparedStatement ps = getStatement(query);
            ps.setString(1, password);
            ps.setInt(2, userID);
            executeManipulatePreparedStatement(ps);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while getting credentials");
        }
    }


}
