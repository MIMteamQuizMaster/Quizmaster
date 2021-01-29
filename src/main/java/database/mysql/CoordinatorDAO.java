package database.mysql;


import model.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CoordinatorDAO extends AbstractDAO {
    User coordinator;
    QuestionDAO questionDAO;
    QuizDAO quizDAO;


    public CoordinatorDAO(DBAccess dBaccess) {
        super(dBaccess);
        questionDAO = new QuestionDAO(dBaccess);
        quizDAO = new QuizDAO(dBaccess);

    }

    /**
     * @param coordinator a user object which expected to has a coordinator role
     * @author M.J. Moshiri
     * <p>
     * Set de given argument as the coordinator to use it for the select course query
     */
    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }

    /**
     * @return an ObservableList of courses assigned to the coordinator
     * @author M.J. Moshiri
     * <p>
     * retrive alle courses that been assign the a coordinator
     * the coordinator should be assignd to the DAO with setCoordinator method
     * ans has not meet the endDate of the courser
     * @should return null if it cant retrieve data from database
     */
    public List<Course> getMyCourses(Boolean archive) {

        List<Course> courseList = new ArrayList<>();
        String query = "SELECT * FROM course WHERE coordinator_user_id=? and (endDate > CURRENT_DATE() or endDate is null ) and archive = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, this.coordinator.getUserId());
//            ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.setBoolean(2, archive);
            ResultSet rs = executeSelectPreparedStatement(ps);
            while (rs.next()) {
                String name = rs.getString("name");
                int courseDbid = rs.getInt("id");
                Date startDate = rs.getDate("startDate");
                Date endDate = rs.getDate("endDate");

                // maak Course Object
                Course course = new Course(name, this.coordinator);
                course.setCourseId(courseDbid);
                course.setStartDate(startDate==null?"":startDate.toString());
                course.setEndDate(endDate==null?"":endDate.toString());

                course.setQuizzes(quizDAO.getQuizOfCourse(course, archive)); // add quizes to Course object

                courseList.add(course);
            }
            return courseList;
        } catch (SQLException throwables) {
            System.out.println("Somthing went wrong while getting course lists");
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Set the quiz in archive so the end user will no more see it
     *
     * @param quiz
     * @return true if it was successful
     * @author M.J. Moshiri
     */
    public boolean archiveQuiz(Quiz quiz) {
        String query = "UPDATE QUIZ SET archive=1 where id = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1, quiz.getQuizId());
            executeManipulatePreparedStatement(ps);
            return true;
        } catch (SQLException throwables) {
            System.out.println("Could not archive the quiz");
            throwables.printStackTrace();
        }
        return false;
    }

    /**
     * @param question object that has a valid questionID
     * @return calls a private method that will execute made query with the ID of the record
     * @author M.J. Moshiri
     * <p>
     * Creates a delete query for deleting a Question record from data base
     */
    public Boolean deleteQuestion(Question question) {
        String query = "DELETE FROM question WHERE id= ?";
        int questionId = question.getQuestionId();
        for (Answer a : question.getAnswers()) {
            deleteAnswer(a);
        }
        return deleteQuery(query, questionId);
    }

    /**
     * @param answer object with a valid Answer ID
     * @return calls a private method that will execute the given query for given answer object
     * @author M.J. Moshiri
     * <p>
     * Creates a delete query for deleting an anwert record from db
     */
    public Boolean deleteAnswer(Answer answer) {
        String query = "DELETE FROM answer WHERE id= ?";
        int answerid = answer.getAnswerId();
        return deleteQuery(query, answerid);
    }

    /**
     * @param quiz Object with a valid quiz ID
     * @return calls a private mthod that will execute the given query for given quiz
     * @author M.J. Moshiri
     * <p>
     * Creates a query for deleting Quiz records in dataBase
     */
    public Boolean deleteQuiz(Quiz quiz) {
        String query = "DELETE FROM quiz WHERE id= ?";
        int quizId = quiz.getQuizId();
        return deleteQuery(query, quizId);
    }

    /**
     * @param query a query which is expected to be a delete query in general
     * @param i     the int that should be filled in the query to run
     * @return true if execute was successful otherwise false
     * @author M.J. Moshiri
     * <p>
     * the method execute a query that only need an integer to be completed for executing
     */
    private Boolean deleteQuery(String query, int i) {
        try {
            PreparedStatement ps = getStatementWithKey(query);
            ps.setInt(1, i);
            executeManipulatePreparedStatement(ps);
            executeInsertPreparedStatement(ps);
            return true;
        } catch (Exception throwables) {
            System.out.println("Somthing went wrong while deleting the record");
        }
        return false;
    }

    public User getCoordibatorById(int coordinatorId) {
        User coordinator_user = null;
        String sql = String.format("SELECT * FROM user " +
                "WHERE user_id=%s;", coordinatorId);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
            if (resultSet.next()) {
                int user_id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                coordinator_user = new User(user_id, firstName, lastName);
            } else {
                System.out.println("There is no Coordinator to select.");
            }
            return coordinator_user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }


}
