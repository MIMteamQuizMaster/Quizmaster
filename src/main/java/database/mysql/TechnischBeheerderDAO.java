package database.mysql;

import model.Role;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class TechnischBeheerderDAO extends AbstractDAO {
    public TechnischBeheerderDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    /**
     * @return a list of User objects with all needed information
     * @author M.J. Moshiri
     * <p>
     * Get all valid users
     * valid means that the row deletionDate hasnt meet today or their deletiondate is null
     * The method also use another methode whitin to retrieve a hashmap of user role
     * which the key is the database id and the value is van type Role
     * the key will be used further for Ending their role
     * @should return null if it wasnt successful in retrieving data from DB
     */
    public List<User> getAllusers() {
        String sql = "SELECT DISTINCT u.user_id , u.firstname, u.lastname,u.studierichting FROM user u where deletionDate > ? or deletionDate is null";

        List<User> rList = new ArrayList<>();
        try {
            PreparedStatement ps = getStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String fname = resultSet.getString("firstname");
                String lname = resultSet.getString("lastname");
                String richting = resultSet.getString("studierichting");

                User u = new User(user_id, fname, lname, richting, new ArrayList<Role>());
                HashMap<Integer, Role> userRoles = getUserRoles(u);
                u.setRoles(userRoles.values().stream().collect(Collectors.toList()));
                rList.add(u);
            }
            return rList;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while getting all users");
        }
        return null;
    }

    /**
     * @param u a User object with a valid User-ID
     * @return a hashmap where the key is the Database row Id and the value is Role
     * the key will be used further to end a role
     * @author M.J. Moshiri
     * <p>
     * Give a hashMap of roles dedicated to the given argument of type USER which must have a valid User-ID in it
     * @should return null if it wasnt successful in retrieving data from DB
     */
    private HashMap<Integer, Role> getUserRoles(User u) {
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

    /**
     * @param u is a User Object with a valid UserID in case of UPDATE or 0 in case of INSERT
     * @return the user Object if the execution of query was successful otherwise it will return NULL
     * @author M.J. Moshiri
     * <p>
     * Save a user into the database which can be an INSERT or an UPDATE
     * which find it's path according to the given ID to the User Object
     * if it be a 0 then the method will chose INSERT otherwise it will chose UPDATE
     * @should return null if it wasnt successful in saving data into the DB
     */
    public User saveUser(User u) {
        String query;
        if (u.getUserId() == 0) { // then its a new Question
            query = "INSERT INTO user (firstname, lastname, studierichting, user_id) VALUES (?,?,?,?)";
        } else {// it is update case
            query = "UPDATE user SET firstname = ? ,lastname = ?, studierichting =? WHERE user_id = ?";
        }
        int key;
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getStudieRichting());
            ps.setInt(4, u.getUserId());
            key = executeInsertPreparedStatement(ps);
            if (u.getUserId() == 0) {
                u.setUserId(key);
            }
            setRoleToUser(u, u.getRoles());
            return u;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while adding/Updating user");
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * @param roleString an identical role String
     * @return the id of role in the database
     * @author M.J. Moshiri
     * <p>
     * Get ID of a role
     * @should return 0 if it wasnt successful in retrieving data from DB
     */
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

    /**
     * @param dbid the record id
     * @author M.J. Moshiri
     * <p>
     * Set an end to the role dedicated to a user
     */
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

    /**
     * @param u     User object with valid user id and the roles that it already have
     * @param roles a list of new roles which doesnt mean only new roles but all the roles the user must have included
     *              new or old
     * @author M.J. Moshiri
     * <p>
     * Update ,Add , Or delete user roles
     * at first it take the roles that user already have and compare it with the new roles
     * so if a role needs to be ended or be assignt the method will do that
     */
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

    /**
     * @param userID an int which must the user ID
     * @return the password of user in String
     * @author M.J. Moshiri
     * @should return empty String if it wasnt successful in retrieving data from DB
     * @should return empty String if user has no credential
     */
    public String getCredential(int userID) {
        String query = "SELECT * FROM credentials where user_id = ?";
        String returnString = "";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, userID);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                returnString = resultSet.getString("password");
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while getting credentials");
        }
        return returnString;
    }

    /**
     * @param u user Object with valid user id
     * @author M.J. Moshiri
     * <p>
     * will set an end to the user membership although the user data will still remains in databse
     * but the deletionDate field will be filled with todays date and the user can no more use the system
     */
    public void setEnd(User u) {
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

    /**
     * @param userID   int of user ID whom the password wil be set for
     * @param password a string that will be assigned as password for the user
     * @author M.J. Moshiri
     * <p>
     * Set a new password for the given userID
     */
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
