package database.mysql;

import model.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizDAO extends AbstractDAO{
    public QuizDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public int getPersonalQuiz(int userId) {

        String sql = String.format("SELECT * FROM user_quiz_log\n" +
                "WHERE user_id=%d;", userId);
        Quiz result = null;
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            if (rs.next()) {
                String initials = rs.getString("voorletters");
                String prefix = rs.getString("voorvoegsels");
                String surName = rs.getString("achternaam");
                String mobile = rs.getString("telefoon");
                result = new Customer(initials, prefix, surName, mobile);
                result.setCustomerId(custId);
            } else {
                System.out.println("Klant met dit klantnummer bestaat niet");
            }

        }
        catch (SQLException e){
            System.out.println("SQL error " + e.getMessage());
        }
        return result;
    }

}
