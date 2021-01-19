package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    public void storePersonalizedAnswers(List<Answer> answers) {
        String sql ="Insert into user_quiz_answers(user_quiz_id, question_id, answer_id, position) values(?,?,?,?) ;";
        int answerPosition = 1;
        for (Answer answer: answers)
        {
            try {
                PreparedStatement preparedStatement = getStatementWithKey(sql);
                preparedStatement.setInt(1, answer.getUserQuizId());
                preparedStatement.setInt(2, answer.getQuestionId());
                preparedStatement.setInt(3, answer.getId());
                preparedStatement.setInt(4, answerPosition);
                int key = executeInsertPreparedStatement(preparedStatement);
                answer.setPersonalizedId(key);
                answerPosition++;
            } catch (SQLException e) {
                System.out.println("SQL error " + e.getMessage());
            }
        }
    }

    public ObservableList<Answer> getAllAnswers(Question question) {
        String query = "SELECT * from answer where question_id = ?";
        ObservableList<Answer> possibleAnswers = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, question.getQuestionId());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()) {
                int answerdbId = rs.getInt("id");
                String answerString = rs.getString("answer");
                boolean isCorrect = rs.getBoolean("isCorrect");
                Answer answer = new Answer(isCorrect, answerString);
                answer.setQuestionId(question.getQuestionId());
                answer.setId(answerdbId);
                possibleAnswers.add(answer);
            }
            return possibleAnswers;
        } catch (SQLException throwables) {
            System.out.println("Somthing went wrong while getting answer lists");
            throwables.printStackTrace();
        }
        return null;
    }

    public Answer saveAnswer(Answer answer) {
        String query;
        int id = answer.getId();
        int questionId = answer.getQuestionId();
        boolean isCorrect = answer.isCorrect();
        String answerString = answer.getAnswer();

        if (id == 0) { // then its a new Question
            query = "INSERT INTO answer (question_id,isCorrect,answer,id) VALUES (?,?,?,?)";
        } else {// it is update case
            query = "UPDATE answer SET question_id = ? , isCorrect = ? ,answer=? WHERE id = ?";
        }
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setInt(1, questionId);
            ps.setBoolean(2, isCorrect);
            ps.setString(3, answerString);
            ps.setInt(4,id);
            int key = executeInsertPreparedStatement(ps);
            if (id == 0) {
                answer.setId(key);
            }
            return answer;
        } catch (SQLException throwables) {
            System.out.println("Somthing went wrong while binding Answer to Question in db");
        }
        return null;
    }



}
