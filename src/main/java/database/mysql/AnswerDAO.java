package database.mysql;

import model.Answer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO extends AbstractDAO{
    public AnswerDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public List<Answer> getAnswersForQuestion(int questionid)
    {
        List<Answer> answers = new ArrayList<>();
        String sql = String.format("SELECT * FROM answer\n" +
                "WHERE question_id=%d;", questionid);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            Answer answer;
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            while (rs.next())
            {
                String answerToQuestion = rs.getString(4);
                int correct = rs.getInt(3);
                boolean trueFalse = false;
                if (correct == 1)
                {
                    trueFalse = true;
                }
                answer = new Answer(trueFalse, answerToQuestion);
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
