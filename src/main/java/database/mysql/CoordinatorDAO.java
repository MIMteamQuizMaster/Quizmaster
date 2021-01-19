package database.mysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class CoordinatorDAO extends AbstractDAO {
    User coordinator;
    QuestionDAO questionDAO;
    QuizDAO quizDAO;


    public CoordinatorDAO(DBAccess dBaccess) {
        super(dBaccess);
        questionDAO = new QuestionDAO(dBaccess);
        quizDAO = new QuizDAO(dBaccess);

    }

    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }

    public ObservableList<Course> getMyCourses() {
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        String query = "SELECT * FROM course WHERE coordinator_user_id=? and endDate > ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, this.coordinator.getUserId());
            ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()) {
                String name = rs.getString("name");
                int courseDbid = rs.getInt("id");
                String startDate = rs.getDate("startDate").toString();
                String endDate = rs.getDate("endDate").toString();

                // maak Course Object
                Course course = new Course(name, this.coordinator);
                course.setDbId(courseDbid);
                course.setStartDate(startDate);
                course.setEndDate(endDate);

                course.setQuizzes(quizDAO.getQuizOfCourse(course)); // add quizes to Course object

                courseList.add(course);
            }
            return courseList;
        } catch (SQLException throwables) {
            System.out.println(throwables.getCause());
            System.out.println("Somthing went wrong while getting course lists");
            throwables.printStackTrace();
        }
        return null;
    }

    public Boolean deleteQuestion(Question question) {
        String query = "DELETE FROM question WHERE id= ?";
        int questionId = question.getQuestionId();
        return deleteQuery(query, questionId);
    }

    public Boolean deleteAnswer(Answer answer)  {
        String query = "DELETE FROM answer WHERE id= ?";
        int answerid = answer.getId();
        return deleteQuery(query, answerid);
    }

    public Boolean deleteQuiz(Quiz quiz)  {
        String query = "DELETE FROM quiz WHERE id= ?";
        int quizId = quiz.getIdquiz();
        return deleteQuery(query, quizId);
    }

    private Boolean deleteQuery(String query, int i)  {
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setInt(1, i);
            executeManipulatePreparedStatement(ps);
            executeInsertPreparedStatement(ps);
            return true;
        } catch (Exception throwables) {
            System.out.println(throwables.getCause());
        }
        return false;
    }


}
