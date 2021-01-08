package database.mysql;

import model.Answer;
import model.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO extends AbstractDAO {

    public QuestionDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public List<Question> getQuestionsForCourse(int courseId)
    {
        List<Question> questions = new ArrayList<>();
        String sql = String.format("SELECT * FROM question\n" +
                "WHERE quiz_id=%d;", courseId);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            Question question;
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            while (rs.next())
            {
                String questionToCourse = rs.getString(3);
                int correct = rs.getInt(3);
                question = new Question(questionToCourse);
                question.setQuestionId(rs.getInt(1));
                question.setQuizId(2);
                questions.add(question);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return questions;
    }
}
