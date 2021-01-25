package database.mysql;
import model.Course;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CourseDAO extends AbstractDAO {

    public CourseDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    /**
     * Use parameter to retrieve the selected course from the database. Use retrieved coordinator_id to retrieve corresponding coordinator.
     * Build new course object.
     * @param course_id the id of the course.
     * @return the new course object created from the database.
     * @author M.J. Alden-Montague
     */
    public Course getCourseById(int course_id) {
        String sql_course = "SELECT name, coordinator_user_id, startDate, endDate FROM course WHERE id = " + course_id;
        Course course = null;

        try {
            Course mpCourse;
            PreparedStatement psCourse = getStatement(sql_course);
            ResultSet rsCourse = executeSelectPreparedStatement(psCourse);
            rsCourse.next();
            String courseName = rsCourse.getString(1);
            int coordinatorId = rsCourse.getInt(2);
            String startDate = rsCourse.getString(3);
            String endDate = rsCourse.getString(4);

            String sql_coordinator = "SELECT firstname, lastname FROM user WHERE user_id = " + coordinatorId;
            PreparedStatement psCoordinator = getStatement(sql_coordinator);
            ResultSet rsCoordinator = executeSelectPreparedStatement(psCoordinator);
            rsCoordinator.next();
            String firstName = rsCoordinator.getString(1);
            String lastName = rsCoordinator.getString(2);

            User coordinator = new User(coordinatorId,firstName,lastName);
            course = new Course(courseName,coordinator);
            course.setDbId(course_id);
            course.setStartDate(startDate);
            course.setEndDate(endDate);

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }

        return course;
    }

    /**
     * Insert passed course into database, extracting each attribute from preparedStatement
     * @param mpCourse is the course passed by the user
     * @author M.J. Alden-Montague
     */
    public void storeCourse(Course mpCourse) {
        String sql = "INSERT INTO course(coordinator_user_id, name, startDate, endDate)" + "VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            preparedStatement.setInt(1, mpCourse.getCoordinator().getUserId());
            preparedStatement.setString(2, mpCourse.getName());
            preparedStatement.setString(3, mpCourse.getStartDate());
            preparedStatement.setString(4, mpCourse.getEndDate());
            executeManipulatePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public List<String> courseIdsToRegisterForEachStudent(User student)
    {
        List<String> courseIdsList = new ArrayList<>();
        student.getUserId();
        String sql = String.format("SELECT course_id FROM user_has_group" +
                "WHERE student_user_id = %s", student);
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
            while (resultSet.next())
            {
                String course_id = String.valueOf(resultSet.getInt(1));
                courseIdsList.add(course_id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courseIdsList;
    }

}