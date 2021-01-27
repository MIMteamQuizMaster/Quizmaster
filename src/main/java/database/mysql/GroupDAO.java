package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Course;
import model.Group;
import model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
        String sql = "SELECT uhg.group_id, g.name, uhg.teacher_user_id FROM student_has_group uhg INNER JOIN quizmaster.group g ON uhg.group_id = g.id WHERE uhg.teacher_user_id = " + teacher.getUserId();
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
            System.out.println(throwables.getMessage() + " Unable to retrieve classes for the selected teacher");
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
        String sql = "SELECT ug.student_user_id, u.firstname, u.lastname FROM student_has_group ug INNER JOIN user u ON ug.student_user_id = u.user_id WHERE ug.group_id = " + group.getDbId();
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
            System.out.println(throwables.getMessage() + " Unable to retrieve students for selected group");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    public void createNewGroup(Course course, String name, User student, int teacher)
    {
        String sql = "Insert into group(course_id, name, docent) values(?,?, ?) ;";
        try {
            PreparedStatement preparedStatement = getStatementWithKey(sql);
            preparedStatement.setInt(1,course.getDbId());
            preparedStatement.setString(2,name);
            preparedStatement.setInt(3, teacher);
            int key = executeInsertPreparedStatement(preparedStatement);
            createUserHasGroup(key,student);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createUserHasGroup(int groupId, User student)
    {
        String sql = "Insert into student_has_group (student_user_id, group_id) values(?,?) ;";
        try {
            PreparedStatement preparedStatement = getStatementWithKey(sql);
            preparedStatement.setInt(2,groupId);
            preparedStatement.setInt(1,student.getUserId());
            int key = executeInsertPreparedStatement(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * @return
     * @author M.J. Moshiri
     */
    public Group saveGroupDedicatedToCourse(Course course, Group group) {

        if (group.getDbId() == 0) {
            //new group and add all students
            saveGroup(course, group);
            System.out.println(group.getDbId());
            for (User student : group.getStudents()) {
                createUserHasGroup(group.getDbId(),student);
            }
        } else {
            //update = group.getStudents(); = getStudentsOfGroup(group);
            List<Integer> newList = group.getStudents().stream().map(User::getUserId).collect(Collectors.toList());
            List<Integer> oldList = getStudentsPerGroup(group).stream().map(User::getUserId).collect(Collectors.toList());
            saveGroup(course, group);
            for (Integer i : oldList) {
                if (!newList.contains(i)) {
                    // user is not in new list then delete user from group
                    removeUserFromGroup(new User(i), group);
                }
            }

            for (Integer i : newList) {
                if (!oldList.contains(i)) {
                    // there is a new user that isnt in the old list then add him
                    createUserHasGroup(group.getDbId(), new User(i));
                }
            }
        }
        return group;
    }


    /**
     * @return
     * @author M.J. Moshiri
     */
    public boolean removeUserFromGroup(User user,Group group){
        String query = "DELETE FROM student_has_group WHERE student_user_id = ? and group_id = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,user.getUserId());
            ps.setInt(2,group.getDbId());
            executeManipulatePreparedStatement(ps);
            return true;
        } catch (SQLException throwables) {
            System.out.println("Somthing went wrong while deleting user from group");
        }
        return false;
    }
    /**
     * @return
     * @author M.J. Moshiri
     */
    public Group saveGroup(Course course, Group group){
        String query;
        if(group.getDbId()==0){
            // new
            query = "Insert into `group` (course_id, name, docent,id) values(?,?,?,?)";
        } else {
            // update
            query = "UPDATE `group` set course_id = ?, name = ? , docent = ? where id = ?";
        }
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setInt(1,course.getDbId());
            ps.setString(2,group.getName());
            if (group.getTeacher() == null) {
                ps.setNull(3, java.sql.Types.NULL);
            } else {
                ps.setInt(3,group.getTeacher().getUserId());
            }
            ps.setInt(4,group.getDbId());
            int key = executeInsertPreparedStatement(ps);
            if (group.getDbId()==0)group.setDbId(key);

            return group;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(" somthing went wrong while saving Group");
        }
        return null;
    }

}
