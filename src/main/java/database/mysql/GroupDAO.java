package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.Class;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO extends AbstractDAO {

    public GroupDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public ObservableList<Class> getAllClasses(User teacher) {
        System.out.println(teacher.getUserId());

        String sql = "SELECT group_id FROM user_has_group WHERE teacher_user_id = " + teacher.getUserId();
        ObservableList<Class> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int groupId = resultSet.getInt("group_id");


                rList.add(new Class(groupId,teacher));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve classes for the selected teacher");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    public ObservableList<User> getStudentsPerClass(Class c) {

        String sql = "SELECT ug.student_user_id, u.firstname, u.lastname FROM user_has_group ug INNER JOIN user u ON ug.student_user_id = u.user_id WHERE ug.group_id = " + c.getDbId();
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
            System.out.println(throwables.getMessage() + "Unable to retrieve classes for the selected teacher");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    public ObservableList<User> getAllStudents(User teacher) {

        String sql = "SELECT ug.student_user_id, u.firstname, u.lastname FROM user_has_group ug INNER JOIN user u ON ug.student_user_id = u.user_id WHERE ug.teacher_user_id = " + teacher.getUserId();
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
            System.out.println(throwables.getMessage() + "Unable to retrieve classes for the selected teacher");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }







}
