package database.mysql;

import model.Question;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestionDAO extends AbstractDAO {

    public QuestionDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public void storeCustomer(Question question) {
        String sql = "Insert into question VALUES(?,?,?,?,?) ;";
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            preparedStatement.setString(3, question.getQuestion());
            preparedStatement.setString(4, question.getCorrectAnswer());
            preparedStatement.setString(5, question.getWrongAnswer1());
            preparedStatement.setString(6, question.getWrongAnswer2());
            preparedStatement.setString(7, question.getWrongAnswer3());
            executeManipulatePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            System.out.println("SQL error " + e.getMessage());
        }
    }
}
