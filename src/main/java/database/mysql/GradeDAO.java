package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Grade;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GradeDAO extends AbstractDAO {

    public GradeDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    /**
     * (R)
     * @param student
     * @return ObservableList with grade objects, linked to user
     * @author M.J. Alden-Montague
     */
    public List<Grade> getAllGrades(User student) {
        String sql = "SELECT g.quiz_id, q.name, g.grade FROM user_quiz_log g, quiz q WHERE g.quiz_id = q.id AND g.student_user_id = " + student.getUserId();
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
     * (R)
     * @param student_id
     * @param quiz_id
     * @return ObservableList with grade objects, linked to quiz and student ids
     * @author M.J. Alden-Montague
     */
    public List<Grade> getAllGradesPerQuiz(int student_id, int quiz_id) {
        String sql = "SELECT quiz_id, grade FROM user_quiz_log WHERE student_user_id = " + student_id + " AND quiz_id = " + quiz_id;
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

    /**
     * (C)
     * Store the Grade object into the database
     * @param grade
     * @return integer grade_id, database ID for the grade.
     * @author M.J. Alden-Montague
     */
    public int storeGrade(Grade grade) {
        String sql = "INSERT INTO user_quiz_log (student_user_id, quiz_id, grade) VALUES(?,?,?);";
        int grade_id = 0;
        try {
            PreparedStatement preparedStatement = getStatementWithKey(sql);
            preparedStatement.setInt(1,grade.getStudentId());
            preparedStatement.setInt(2,grade.getQuizId());
            preparedStatement.setDouble(3,grade.getGrade());
            grade_id = executeInsertPreparedStatement(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return grade_id;
    }

    /**
     * (U)
     * Update the grade object into the database
     * @param grade is the grade object
     * @param dbId is the dbId for the grade to be updated.
     * @author M.J. Alden-Montague
     **/
    public void updateGrade(Grade grade, int dbId) {
        String sql = "UPDATE user_quiz_log SET grade = ? WHERE id = ? ;";
        try {
            PreparedStatement preparedStatement = getStatementWithKey(sql);
            preparedStatement.setDouble(1,grade.getGrade());
            preparedStatement.setInt(2,dbId);
            executeInsertPreparedStatement(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * (D)
     * Delete grade from selected database id.
     * @param dbId
     * @author M.J. Alden-Montague
     */
    public void deleteGrade(int dbId) {
        String sql = "DELETE FROM user_quiz_log WHERE id = ? ;";
        try {
            PreparedStatement preparedStatement = getStatementWithKey(sql);
            preparedStatement.setDouble(1,dbId);
            executeInsertPreparedStatement(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * (R)
     * @param grade object
     * @return a hashmap with database id for the grade and created data stamp
     * @author M.J. Alden-Montague
     */
    public Map<Integer, String> getDbIdsPerQuizAttempt(Grade grade) {
        String sql = "SELECT id, stamp_created FROM user_quiz_log WHERE student_user_id = " + grade.getStudentId() + " AND quiz_id = " + grade.getQuizId();
        Map<Integer, String> result = new HashMap<>();
        try {
            PreparedStatement ps = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(ps);
            while (resultSet.next()) {
                int dbId = resultSet.getInt(1);
                String created = resultSet.getString(2);
                result.put(dbId,created);
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage() + "Unable to retrieve grades for the selected student");
            System.out.println(throwables.getMessage());
        }
        return result;
    }
}