package database.mysql;

import launcher.Main;
import model.Course;
import model.Role;
import model.StudentSignInOut;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User_has_groupDAO extends AbstractDAO {
    public User_has_groupDAO(DBAccess dBaccess, User user) {
        super(dBaccess);
        this.student = user;
    }

    private GroupDAO groupDAO = new GroupDAO(Main.getDBaccess());
    private CourseDAO courseDAO = new CourseDAO(Main.getDBaccess());
    private StudentSignInOut ssiso= new StudentSignInOut();
    private List<Integer> group_id;
    private User student;

    public void addStudentToCourseAndGroup(List<Course> courseList)
    {
        int student_id = this.student.getUserId();
        for (Course course: courseList)
        {
            int course_id = course.getDbId();
            String sql = String.format("SELECT u.group_id, g.course_id, count(u.group_id) AS group_count FROM user_has_group AS u " +
                    "INNER JOIN `group` AS g ON g.id = u.group_id " +
                    "WHERE g.course_id = %d " +
                    "GROUP BY u.group_id " +
                    "ORDER BY count(u.group_id) " +
                    "LIMIT 1;", course_id);
            try {
                PreparedStatement preparedStatement = getStatement(sql);
                ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
                if (resultSet.next())
                {
                    int group_id = resultSet.getInt(1);
                    int group_size = resultSet.getInt(3);
                    if (group_size >= 10)
                    {
                        int numberOfGroupsForCourse = courseDAO.returnNumberOfGroupsPerCourse(course);
                        groupDAO.createNewGroup(course, ssiso.generatedGroupName(course, numberOfGroupsForCourse),
                                student, ssiso.getRandomTeacher(courseDAO.getAllValidUsersByRole(Role.TEACHER)));
                        courseDAO.createStudentHasCourse(student, course);
                        //create new group and add student and teacher to it and
                        // ad student to course
                    }
                    else
                    {
                        courseDAO.createStudentHasCourse(student, course);
                        groupDAO.createUserHasGroup(group_id,student);
                        //add student to excisting group and add student to course
                    }
                }
                else {
                   //create new group and add student and teacher to it and
                    // ad student to course
                    groupDAO.createNewGroup(course, ssiso.generatedGroupName(course),
                            student, ssiso.getRandomTeacher(courseDAO.getAllValidUsersByRole(Role.TEACHER)));
                    courseDAO.createStudentHasCourse(student, course);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public void deleteStudentFromCourseAndGroup(List<Course> courseList)
    {
        for (Course course: courseList)
        {
            courseDAO.deleteStudentFromCourseAndGroup(course,this.student);
        }
    }

}
