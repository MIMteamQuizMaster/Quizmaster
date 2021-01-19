package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Grade;
import model.Student;
import model.Teacher;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GradeDAO extends AbstractDAO {

    public GradeDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public ObservableList<Grade> getAllGrades(User student) {
        System.out.println(student.getUserId());

        String sql = "SELECT quiz_id, grade FROM grade WHERE student_user_id = " + student.getUserId();
        ObservableList<Grade> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int quizId = resultSet.getInt("quiz_id");
                double grade = resultSet.getDouble("grade");

                rList.add(new Grade(quizId,grade,student.getUserId()));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve grades for the selected student");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

    public ObservableList<Grade> getAllGradesPerTeacher(Teacher teacher) {
        System.out.println(teacher.getUserId());
        String sql = "SELECT q.id, g.grade FROM quiz q INNER JOIN course c ON q.course_id = c.id INNER JOIN grade g ON g.quiz_id = q.id  WHERE coordinator_user_id = " + teacher.getUserId();
        ObservableList<Grade> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int quizId = resultSet.getInt("id");
                double grade = resultSet.getDouble("grade");

                rList.add(new Grade(quizId,grade,teacher.getUserId()));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve grades for the selected student");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }

//    public ObservableList<User> getStudents(Class c) {
//
//        String sql = "SELECT sc.student_user_id, u.firstname, u.lastname FROM student_has_class sc INNER JOIN user u ON sc.student_user_id = u.user_id WHERE sc.class_id = " + c.getDbId();
//        ObservableList<User> rList = FXCollections.observableArrayList();
//        try {
//            PreparedStatement ps = getStatement(sql);
//            ResultSet resultSet = executeSelectPreparedStatement(ps);
//            while (resultSet.next()) {
//                int studentId = resultSet.getInt("student_user_id");
//                String firstName = resultSet.getString("firstname");
//                String lastName = resultSet.getString("lastname");
//
//                rList.add(new User(studentId,firstName,lastName));
//
//
//            }
//        } catch (SQLException throwables) {
//            System.out.println(throwables.getMessage() + "Unable to retrieve classes for the selected teacher");
//            System.out.println(throwables.getMessage());
//        }
//        return rList;
//    }








}
