package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Course;
import model.Quiz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO extends AbstractDAO {
    private QuestionDAO questionDAO;

    public QuizDAO(DBAccess dBaccess) {
        super(dBaccess);
        questionDAO = new QuestionDAO(dBaccess);

    }

    /**
     * @param course object that has DbId of the given Course record
     * @return An observableList of quizzes that are dedicated to the given argument
     * It will return Null if it cant not retrieve data from Database
     * @author M.J. Moshiri
     * @should return null if Course argument has no id
     */
    public List<Quiz> getQuizOfCourse(Course course) {
        List<Quiz> quizList = new ArrayList<>();
        String query = "SELECT * FROM quiz WHERE course_id =?";
        int courseId = course.getDbId();
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, courseId);
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()) {
                String name = rs.getString("name");
                int quizID = rs.getInt("id");
                int timelimit = rs.getInt("timelimit");
                double successDefinition = rs.getDouble("successDefinition");
                Quiz quiz = new Quiz(name, successDefinition);
                quiz.setTimeLimit(timelimit);
                quiz.setIdquiz(quizID);
                quiz.setIdcourse(courseId);
                quiz.setQuestions(questionDAO.getQuestions(quiz)); // find questions of the quiz and add to its object
                quizList.add(quiz);
            }
            return quizList;
        } catch (SQLException throwables) {
            System.out.println(throwables.getCause());
            System.out.println("Somthing went wrong while getting quiz lists");
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * @param quiz object
     *             it save the given quiz Object into the databes and
     *             based on the ID inside the given quiz object
     *             if the given object has an ID of 0 then it will chose the INSERT query
     *             otherwise it will chose the UPDATE query for given quiz argument
     * @return the quiz object back with dbID
     * @author M.J. Moshiri
     * @should return null if insert or updating the record was unsuccessful
     */
    public Quiz saveQuiz(Quiz quiz) {
        String query;

        String name = quiz.getName();
        int course_idcourse = quiz.getIdcourse();
        double successDefinition = quiz.getSuccsesDefinition();
        int timelimit = quiz.getTimeLimit();
        int id = quiz.getIdquiz();

        if (id == 0) { // then its a new Question
            query = "INSERT INTO quiz (name, course_id, successDefinition, timelimit ,id) VALUES (?, ?,?,?,?)";
        } else {// it is update case
            query = "UPDATE quiz SET name=?, course_id=?, successDefinition=?, timelimit = ?  WHERE id = ?";
        }
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setString(1, name);
            ps.setInt(2, course_idcourse);
            ps.setDouble(3, successDefinition);
            ps.setInt(4, timelimit);
            ps.setInt(5, id);
            int key = executeInsertPreparedStatement(ps);
            if (id == 0) {
                quiz.setIdquiz(key);
            }
            return quiz;
        } catch (SQLException throwables) {
            System.out.println("Somthing went wrong while adding quiz");
        }
        return null;
    }

}
