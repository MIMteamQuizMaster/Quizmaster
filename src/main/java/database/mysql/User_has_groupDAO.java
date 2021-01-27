package database.mysql;

import controller.AlertHelper;
import model.Course;
import model.Role;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class User_has_groupDAO extends AbstractDAO {
    public User_has_groupDAO(DBAccess dBaccess, User user) {
        super(dBaccess);
        this.student = user;
        this.groupDAO = new GroupDAO(dBaccess);
        this.courseDAO = new CourseDAO(dBaccess);
    }

    private GroupDAO groupDAO;
    private CourseDAO courseDAO;
    private User student;
    private Random random = new Random();

    public void addStudentToCourseAndGroup(List<Course> courseList)
    {
        int student_id = this.student.getUserId();
        for (Course course: courseList)
        {
            int course_id = course.getDbId();
            String sql = String.format("SELECT u.group_id, g.course_id, count(u.group_id) AS group_count FROM student_has_group AS u " +
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
                        groupDAO.createNewGroup(course, generatedGroupName(course, numberOfGroupsForCourse),
                                student, getRandomTeacher(courseDAO.getAllValidUsersByRole(Role.TEACHER)));
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
                    groupDAO.createNewGroup(course, generatedGroupName(course),
                            student, getRandomTeacher(courseDAO.getAllValidUsersByRole(Role.TEACHER)));
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

    public User getRandomTeacher(List<User> teachers)
    {
        User returnValue;
        int i = random.nextInt(teachers.size());
        returnValue = teachers.get(i);
        return returnValue;
    }

    public String generatedGroupName(Course course)
    {
        StringBuilder groupName = new StringBuilder();
        String firstPart = course.getName() + " ";
        groupName.append(firstPart);
        String secondPart = String.format("%03d", 1);
        groupName.append(secondPart);
        return groupName.toString();
    }

    public String generatedGroupName(Course course, int number)
    {
        if (999%number == 0)
        {
            AlertHelper.confirmationDialog("Verwijder oudere groepen " +
                    "De benaming begint weer vanaf 1 en dubbele " +
                    "namen kunnen gaan voorkomen als oude groepen " +
                    "niet verwijderd worden.");
            int maxNumber = 999;
            int multiplacation = number/maxNumber;
            int newNumber = 1+(number-(multiplacation*maxNumber));
            StringBuilder groupName = new StringBuilder();
            String firstPart = course.getName() + " ";
            groupName.append(firstPart);
            String secondPart = String.format("%03d",newNumber);
            groupName.append(secondPart);
            return groupName.toString();
        }
        else
        {
            int maxNumber = 999;
            int multiplacation = number/maxNumber;
            int newNumber = 1+(number-(multiplacation*maxNumber));
            StringBuilder groupName = new StringBuilder();
            String firstPart = course.getName() + " ";
            groupName.append(firstPart);
            String secondPart = String.format("%03d",newNumber);
            groupName.append(secondPart);
            return groupName.toString();
        }
    }
}
