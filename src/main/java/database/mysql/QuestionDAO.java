package database.mysql;

import model.Question;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestionDAO extends AbstractDAO {

    public QuestionDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public void storeCustomer(Question question) {
        String sql = "Insert into questioninprogress VALUES(?,?,?,?,?,?) ;";
        try {
            PreparedStatement preparedStatement = getStatementWithKey(sql);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, question.getQuestion());
            preparedStatement.setString(3, question.getCorrectAnswer());
            preparedStatement.setString(4, question.getWrongAnswer1());
            preparedStatement.setString(5, question.getWrongAnswer2());
            preparedStatement.setString(6, question.getWrongAnswer3());
            int key = executeInsertPreparedStatement(preparedStatement);
            question.setQuestionId(key);
        } catch (SQLException e) {
            System.out.println("SQL error " + e.getMessage());
        }
    }
}
