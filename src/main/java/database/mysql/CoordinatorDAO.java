package database.mysql;

import controller.fx.QuizFx;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


public class CoordinatorDAO extends AbstractDAO {
    User coordinator;

    public CoordinatorDAO(DBAccess dBaccess) {
        super(dBaccess);
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

                course.setQuizzes(getQuizOfCourse(course)); // add quizes to Course object

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

    public ObservableList<Quiz> getQuizOfCourse(Course course) {
        ObservableList<Quiz> quizList = FXCollections.observableArrayList();
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
                quiz.setQuestions(getQuestions(quiz)); // find questions of the quiz and add to its object
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

    public ObservableList<Question> getQuestions(Quiz quiz) {
        ObservableList<Question> questionsList = FXCollections.observableArrayList();
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
                question.setAnswers(getAllAnswers(question)); // find answers

                questionsList.add(question);
            }
            return questionsList;
        } catch (SQLException throwables) {

            System.out.println("Somthing went wrong while getting Question lists");
            throwables.printStackTrace();
        }
        return null;
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
            System.out.println(throwables.getCause());
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
            ps.setInt(5,id);
            int key = executeInsertPreparedStatement(ps);

            if (id == 0) {
                quiz.setIdquiz(key);
            }
            quiz.setIdquiz(key);
            return quiz;
        } catch (SQLException throwables) {
            System.out.println("Somthing went wrong while adding quiz");
        }
        return null;
    }
}
