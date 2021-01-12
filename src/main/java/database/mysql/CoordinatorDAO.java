package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CoordinatorDAO extends AbstractDAO {
    User coordinator;
    public CoordinatorDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }

    public ObservableList<Course> getMyCourses(){
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        String query = "SELECT * FROM course WHERE coordinator_id=? and endDate > ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,this.coordinator.getUserId());
            ps.setDate(2,java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                String name = rs.getString("name");
                int courseDbid = rs.getInt( "idcourse");
                String startDate = rs.getDate("startDate").toString();
                String endDate = rs.getDate("endDate").toString();

                // maak Course Object
                Course course = new Course(name, this.coordinator);
                course.setDbId(courseDbid);
                course.setStartDate(startDate);
                course.setEndDate(endDate);
                /// add quizes to Course object
                course.setQuizzes(getQuizOfCourse(course));;
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

    private ObservableList<Quiz> getQuizOfCourse(Course course){
        ObservableList<Quiz> quizList = FXCollections.observableArrayList();
        String query = "SELECT * FROM quiz WHERE course_idcourse =?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,course.getDbId());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                String name = rs.getString("name");
                int quizID = rs.getInt("id");
                int timelimit = rs.getInt( "timelimit_minutes");
                double successDefinition = rs.getDouble("successDefinition");
                Quiz temp = new Quiz(name,successDefinition);
                temp.setTimeLimit(timelimit);
                temp.setIdquiz(quizID);
                ObservableList<Question> questions = getQuestions(temp);
                temp.setQuestions(questions);
                quizList.add(temp);
            }
            return quizList;
        } catch (SQLException throwables) {
            System.out.println(throwables.getCause());
            System.out.println("Somthing went wrong while getting quiz lists");
            throwables.printStackTrace();
        }
        return null;
    }

    private ObservableList<Question> getQuestions (Quiz quiz){
        ObservableList<Question> questionsList = FXCollections.observableArrayList();

        String query = "SELECT * FROM question WHERE quiz_id=?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,quiz.getIdquiz());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                String question = rs.getString("question");
                int id = rs.getInt("id");
                Question q = new Question(id,question);
                ObservableList<Answer> answers =getAllAnswers(q);
                q.setAnswers(answers);
                questionsList.add(q);
            }
            return questionsList;
        } catch (SQLException throwables) {

            System.out.println("Somthing went wrong while getting Question lists");
            throwables.printStackTrace();
        }
        return null;
    }

    private ObservableList<Answer> getAllAnswers(Question question){
        String query = "SELECT * from answer where question_id = ?";
        ObservableList<Answer> possibleAnswers = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,question.getQuestionId());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                int answerdbId = rs.getInt("id");
                String answer = rs.getString("answer");
                boolean isCorrect = rs.getBoolean("isCorrect");
                Answer a = new Answer(isCorrect,answer);
                a.setId(answerdbId);

            }
            return possibleAnswers;
        } catch (SQLException throwables) {
            System.out.println(throwables.getCause());
            System.out.println("Somthing went wrong while getting answer lists");
            throwables.printStackTrace();
        }
        return null;
    }
}
