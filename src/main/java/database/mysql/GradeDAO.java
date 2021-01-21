package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Grade;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GradeDAO extends AbstractDAO {

    public GradeDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    /**
     *
     * @param student
     * @return ObservableList with grade objects, linked to user
     * @author M.J. Alden-Montague
     */
    public ObservableList<Grade> getAllGrades(User student) {
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

    /**
     *
     * @param student_id
     * @param quiz_id
     * @return ObservableList with grade objects, linked to quiz and student ids
     * @author M.J. Alden-Montague
     */
    public ObservableList<Grade> getAllGradesPerQuiz(int student_id, int quiz_id) {
        String sql = "SELECT quiz_id, grade FROM grade WHERE student_user_id = " + student_id + " AND quiz_id = " + quiz_id;
        ObservableList<Grade> rList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int quizId = resultSet.getInt("quiz_id");
                double grade = resultSet.getDouble("grade");
                rList.add(new Grade(quizId,grade,quiz_id));
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve grades for the selected student");
            System.out.println(throwables.getMessage());
        }
        return rList;
    }
}
