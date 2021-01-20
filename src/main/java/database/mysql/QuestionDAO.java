package database.mysql;

import javafx.collections.FXCollections;
import model.Answer;
import model.Question;
import model.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO extends AbstractDAO {
    private AnswerDAO answerDAO;

    public QuestionDAO(DBAccess dBaccess) {
        super(dBaccess);
        this.answerDAO = new AnswerDAO(dBaccess);

    }

    public List<Question> getQuestionsForCourse(int courseId) {
        List<Question> questions = new ArrayList<>();
        String sql = String.format("SELECT * FROM question\n" +
                "WHERE quiz_id=%d;", courseId);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            Question question;
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            while (rs.next()) {
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

    /**
     * @param quiz object with a valid Quiz id
     * @return a list of Question Objects
     * @author M.J. Moshiri
     * <p>
     * Get all questions that are dedicated to the given argument of Quiz typo
     * which must have a valid quizId in it
     */
    public List<Question> getQuestions(Quiz quiz) {
        List<Question> questionsList = new ArrayList<>();
        int quizId = quiz.getIdquiz();
        String query = "SELECT * FROM question WHERE quiz_id=?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, quizId);
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()) {
                String questionString = rs.getString("question");
                int id = rs.getInt("id");
                Question question = new Question(id, questionString);
                question.setQuizId(quizId);
                question.setAnswers(answerDAO.getAllAnswers(question)); // find answers

                questionsList.add(question);
            }
            return questionsList;
        } catch (SQLException throwables) {

            System.out.println("Somthing went wrong while getting Question lists");
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * @param question Object with valid QuizID
     * @return the Question ID with valid QuestionID in case of INSERT
     * @author M.J. Moshiri
     * <p>
     * Save a Question in the databese, A question that has valid QuizId and QuestionID
     * although the Quuestion id can be 0 so it mean that it is a new Question and the
     * method will pick the INSERT query
     * @Should return null if it fails while inserting or updating.
     */
    public Question saveQuestion(Question question) {
        int id = question.getQuestionId();
        int quizid = question.getQuizId();
        String query;
        String questionString = question.getQuestion();
        if (id == 0) { // then its a new Question
            query = "INSERT INTO question (quiz_id,question,id) values (?,?,?)";
        } else {// it is update case
            query = "UPDATE question SET quiz_id = ? , question = ? WHERE id = ?";
        }
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setInt(1, quizid);
            ps.setString(2, questionString);
            ps.setInt(3, id);
            int questionID = executeInsertPreparedStatement(ps);
            if (id == 0) {
                question.setQuestionId(questionID);
            }
            return question;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
