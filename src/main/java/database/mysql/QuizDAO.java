package database.mysql;

import model.Question;
import model.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO extends AbstractDAO{
    public QuizDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public List<Quiz> getAllQuizzes() {

        String sql = "Select * From quiz";
        List<Quiz> quizzes = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            Quiz quiz;
            while (rs.next()) {
                String name = rs.getString(3);
                double succesdefinition = rs.getDouble(4);
                quiz = new Quiz(succesdefinition, name);
                quiz.setIdquiz(rs.getInt(1));
                quiz.setIdcourse(rs.getInt(2));
                quizzes.add(quiz);
            }
        }
        catch (SQLException e){
            System.out.println("SQL error " + e.getMessage());
        }
        return quizzes;
    }

}
