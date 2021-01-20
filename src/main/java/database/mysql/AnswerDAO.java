package database.mysql;

import model.Answer;
import model.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnswerDAO extends AbstractDAO {
    public AnswerDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    /**
     * @param question a question Object with valid question id
     * @return a list containd all possible answers dedicated to the Question
     * @author M.J. Moshiri
     * Get all answers dedicated to given arugument which must be of question Type with valid Question ID
     * The answer have a Getter for their validity
     */
    public List<Answer> getAllAnswers(Question question) {
        String query = "SELECT * from answer where question_id = ?";
        List<Answer> possibleAnswers = new ArrayList<>();

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

    /**
     * @param answer answer object valid id in case of Update an with id of 0 in case of insert
     * @return answer object with updated id in case of Insert
     * @author M.J. Moshiri
     * Save the given answer into the databes , it van Update or insert the given argument base on its ID
     * so if the id is 0 the method will chose the Insert Query
     */
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
            ps.setInt(4, id);
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
