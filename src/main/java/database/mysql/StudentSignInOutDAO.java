package database.mysql;

import model.Course;
import model.Question;
import model.Quiz;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentSignInOutDAO extends AbstractDAO {

    /**
     * @author Ismael Ben Cherif
     * Returns the Courses a student is signed in for and the student hasn't signed in for.
     *
     */

    private User student;
    private CourseDAO courseDAO;
    private CoordinatorDAO coordinatorDAO;
    private QuizDAO quizDAO;
    private UserDAO userDAO;

    public StudentSignInOutDAO(DBAccess dBaccess) {
        super(dBaccess);
        this.courseDAO = new CourseDAO(dBaccess);
        this.coordinatorDAO = new CoordinatorDAO(dBaccess);
        this.quizDAO = new QuizDAO(dBaccess);
        this.userDAO = new UserDAO(dBaccess);
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    /**
     * @author Ismael Ben Cherif
     * Returns a list of courses with the quizzes, questions and
     * mixed answers objects in it that the student hasn't registered for.
     * @return
     */
    public List<Course> returnCoursesToRegisterFor()
    {
        List<Course> returnValue = new ArrayList<>();
        List<String> courseIds = courseDAO.courseIdsToRegisterForEachStudent(this.student);
        List<Course> courseList = getAllcoursesNotAssignedToStudent(courseIds);
        returnValue = returnListOfFilledCourses(courseList);
        for (Course course: returnValue)
        {
            for (Quiz quiz: course.getQuizzes())
            {
                for (Question question: quiz.getQuestions())
                {
                    question.mixAnswers();
                }
            }
        }
        return returnValue;
    }

    /**
     * @author Ismael Ben Cherif
     * Returns a list of courses with the quizzes, questions and
     * mixed answers objects in it that the student has registered for.
     * @return
     */
    public List<Course> returnCoursesAllreadyRegisterFor()
    {
        List<Course> returnValue = new ArrayList<>();
        List<String> courseIds = courseDAO.courseIdsToRegisterForEachStudent(this.student);
        List<Course> courseList = getAllcoursesAssignedToStudent(courseIds);
        returnValue = returnListOfFilledCourses(courseList);
        for (Course course: returnValue)
        {
            for (Quiz quiz: course.getQuizzes())
            {
                for (Question question: quiz.getQuestions())
                {
                    question.mixAnswers();
                }
            }
        }
        return returnValue;
    }

    /**
     * @author Ismael Ben Cherif
     * Gets all courses the student hasn't signed in for yet.
     * @param registeredCourses
     * @return
     */
    public List<Course> getAllcoursesNotAssignedToStudent(List<String> registeredCourses)
    {
        List<Course> courses = new ArrayList<>();
        String stringRegesteredCourses = String.join(", ", registeredCourses);
        String sql;
        if (registeredCourses.size()!=0) {
            sql = String.format("SELECT * " +
                    "FROM course " +
                    "WHERE id NOT IN (%s);", stringRegesteredCourses);
        }
        else
        {
            sql = "SELECT * FROM course";
        }
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
            Course newCourse;
            while (resultSet.next())
            {
                int courseId = resultSet.getInt(1);
                int coordinator_id = resultSet.getInt(2);
                String name = resultSet.getString(3);
                newCourse = new Course(name, this.userDAO.getUser(coordinator_id));
                newCourse.setDbId(courseId);
                courses.add(newCourse);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return courses;
    }

    /**
     * @author Ismael Ben Cherif
     * Adds the objects of quizzes, questions and answers to the courses.
     * @param courseList
     * @return
     */
    public List<Course> returnListOfFilledCourses(List<Course> courseList)
    {
        List<Course> returnValue= new ArrayList<>();
        for (Course course : courseList)
        {
            course.setQuizzes(quizDAO.getQuizOfCourse(course,false));
            returnValue.add(course);
        }
        return returnValue;
    }

    /**
     * @author Ismael Ben Cherif
     * Gets all courses the student has signed in for.
     * @param registeredCourses
     * @return
     */
    public List<Course> getAllcoursesAssignedToStudent(List<String> registeredCourses)
    {
        if (registeredCourses.size()==0)
        {
            return new ArrayList<>();
        }
        List<Course> courses = new ArrayList<>();
        String stringRegesteredCourses = String.join(", ", registeredCourses);
        String sql;
            sql = String.format("SELECT * " +
                    "FROM course " +
                    "WHERE id IN (%s);", stringRegesteredCourses);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
            Course newCourse;
            while (resultSet.next())
            {
                int courseId = resultSet.getInt(1);
                int coordinator_id = resultSet.getInt(2);
                String name = resultSet.getString(3);
                newCourse = new Course(name, this.userDAO.getUser(coordinator_id));
                newCourse.setDbId(courseId);
                courses.add(newCourse);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return courses;
    }
}
