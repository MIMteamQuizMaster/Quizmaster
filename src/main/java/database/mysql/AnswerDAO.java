package database.mysql;

import model.Answer;
import model.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO extends AbstractDAO{
    public AnswerDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public boolean convertTinyIntToBoolean(int i) {
        // The expected boolean value
        boolean boolValue;

        // Check if it's greater than equal to 1
        if (i == 0) {
            boolValue = false;
        } else {
            boolValue = true;
        }
        return boolValue;
    }

    public List<Answer> getAnswersForQuestion(Question question)
    {
        List<Answer> answers = new ArrayList<>();
        int questionId = question.getQuestionId();
        String sql = String.format("SELECT * FROM answer WHERE question_id = %d", questionId);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            Answer answer;
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            while (rs.next())
            {
                String answerToQuestion = rs.getString(4);
                boolean correct = convertTinyIntToBoolean(rs.getInt(3));
                answer = new Answer(correct, answerToQuestion);
                answer.setId(rs.getInt(1));
                answer.setQuestionId(rs.getInt(2));
                answers.add(answer);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return answers;
    }

    
}
