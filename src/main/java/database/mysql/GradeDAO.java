package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Grade;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
    public List<Grade> getAllGrades(User student) {
        //String sql = "SELECT quiz_id, grade FROM grade WHERE student_user_id = " + student.getUserId();
        String sql = "SELECT g.quiz_id, q.name, g.grade FROM grade g, quiz q WHERE g.quiz_id = q.id AND g.student_user_id = " + student.getUserId();
        List<Grade> rList = new ArrayList<>();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int quizId = resultSet.getInt("quiz_id");
                double grade = resultSet.getDouble("grade");
                String quizName = resultSet.getString("name");
                Grade rGrade = new Grade(quizId,grade, student.getUserId());
                rGrade.setQuizName(quizName);
                rList.add(rGrade);
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
    public List<Grade> getAllGradesPerQuiz(int student_id, int quiz_id) {
        String sql = "SELECT quiz_id, grade FROM grade WHERE student_user_id = " + student_id + " AND quiz_id = " + quiz_id;
        List<Grade> rList = new ArrayList<>();
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
