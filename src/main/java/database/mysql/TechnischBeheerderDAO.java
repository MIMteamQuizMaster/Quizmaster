package database.mysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
            System.out.println(throwables.getMessage() + " Somthing wrong while getting menu items");
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
            System.out.println(throwables.getMessage() + " Somthing wrong while getting all users");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    public int addNewUser(User u){
        String query = "INSERT INTO user\n" +
                "(`firstname`,\n" +
                "`lastname`,\n" +
                "`studierichting`,\n" +
                "`creationDate`)\n" +
                "VALUES\n" +
                "(?,?,?,?)";
        int id=0;
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setString(1,u.getFirstName());
            ps.setString(2,u.getLastName());
            ps.setString(3,u.getStudieRichting());
            ps.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
            id = executeInsertPreparedStatement(ps);
            setRoleToUser(id,u.getRole().toString());
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while adding new user");
            throwables.printStackTrace();
        }
        return id;
    }

    public int getRoleId(String roleString){
        String query = "SELECT id FROM role where name = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setString(1,roleString);
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()){
                return rs.getInt("id");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


    public List<Role> getUserRole(User u){
        List<Role> roleList = new ArrayList<>();
        String query = "SELECT ur.user_id , r.name as role_name FROM user_role ur, role r where r.id = ur.role_id and user_id = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,u.getUserId());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next());
                {
                    String roleString = rs.getString("role_name");
                    roleList.add(Role.getRole(roleString));
                }
                return roleList;
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while assigning role to user");
            throwables.printStackTrace();
        }
        return null;
    }

    public void setRoleToUser(int userID, String role){
        int roleID = getRoleId(role);
        String query = "INSERT INTO user_role (user_id,role_id, startDate) VALUES (?,?,?)";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,userID);
            ps.setInt(2,roleID);
            ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            executeManipulatePreparedStatement(ps);

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while assigning role to user");
            throwables.printStackTrace();
        }
    }

    public String getCredential(int userID){
        String query = "SELECT * FROM credentials where user_id = ?";
        String returnString = null;
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,userID);
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

    public void setCredential(int userID,String password){
        try {
            String query ="";
            if(getCredential(userID) == null){
                query= "INSERT INTO credentials (password,user_id) values (?,?)";
            }   else
            {
                query = "UPDATE credentials set password = ? WHERE user_id = ?";
            }
            PreparedStatement ps = getStatement(query);
            ps.setString(1,password);
            ps.setInt(2,userID);
            executeManipulatePreparedStatement(ps);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while getting credentials");
        }
    }

    public void updateUser(User u){
        String query = "UPDATE user SET firstname = ? ,lastname = ?, studierichting =? WHERE user_id = ?";
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setString(1,u.getFirstName());
            ps.setString(2,u.getLastName());
            ps.setString(3,u.getStudieRichting());
            ps.setInt(4,u.getUserId());
            executeManipulatePreparedStatement(ps);
            // TODO: update role or add new role function

//            setRoleToUser(u.getUserId(),u.getRole().toString());
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + " Somthing wrong while updating");
            throwables.printStackTrace();
        }
    }

}
