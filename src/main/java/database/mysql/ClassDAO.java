package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.Class;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClassDAO extends AbstractDAO {

    public ClassDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public ObservableList<Class> getAllClasses(Teacher teacher) {
        System.out.println(teacher.getUserId());

        String sql = "SELECT id FROM class WHERE teacher_user_id = " + teacher.getUserId();
        ObservableList<Class> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int classId = resultSet.getInt("id");


                rList.add(new Class(classId,teacher));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve classes for the selected teacher");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    public ObservableList<Student> getStudents(Class c) {

        String sql = "SELECT sc.student_user_id, u.firstname, u.lastname FROM student_has_class sc INNER JOIN user u ON sc.student_user_id = u.user_id WHERE sc.class_id = " + c.getDbId();
        ObservableList<Student> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int studentId = resultSet.getInt("student_user_id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");

                rList.add(new Student(studentId,firstName,lastName));


            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve classes for the selected teacher");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }








}
