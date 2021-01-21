package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Group;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO extends AbstractDAO {

    public GroupDAO(DBAccess dBaccess) {
        super(dBaccess);
    }


    /**
     *
     * @param teacher
     * @return ObservableList with Group objects, linked to a teacher
     * @author M.J. Alden-Montague
     */
    public ObservableList<Group> getAllGroups(User teacher) {
        String sql = "SELECT uhg.group_id, g.name, uhg.teacher_user_id FROM user_has_group uhg INNER JOIN quizmaster.group g ON uhg.group_id = g.id WHERE uhg.teacher_user_id = " + teacher.getUserId();
        ObservableList<Group> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int groupId = resultSet.getInt("group_id");
                String groupName = resultSet.getString("name");
                rList.add(new Group(groupId,groupName,teacher));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve classes for the selected teacher");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    /**
     *
     * @param group
     * @return ObservableList with User objects, linked to a specific group
     * @author M.J. Alden-Montague
     */
    public ObservableList<User> getStudentsPerGroup(Group group) {
        String sql = "SELECT ug.student_user_id, u.firstname, u.lastname FROM user_has_group ug INNER JOIN user u ON ug.student_user_id = u.user_id WHERE ug.group_id = " + group.getDbId();
        ObservableList<User> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_user_id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                rList.add(new User(studentId,firstName,lastName));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve students for selected group");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }
}
