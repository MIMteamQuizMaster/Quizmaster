package database.mysql;

import model.Answer;
import model.Course;
import model.Question;
import model.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO extends AbstractDAO {

    public QuestionDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public List<Question> getQuestionsForQuize(Quiz quiz)
    {
        List<Question> questions = new ArrayList<>();
        int quizID = quiz.getIdquiz();
        String sql = String.format("SELECT * FROM question\n" +
                "WHERE quiz_id=%d;", quizID);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            Question question;
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            while (rs.next())
            {
                String questionToCourse = rs.getString(3);
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
