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

/**
 * @author Ismael Ben Cherif
 * This class ads and removes students from a course and group.
 */
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

    /**
     * @author Ismael Ben Cherif
     * Adds students to a group and course.
     */
    public void addStudentToCourseAndGroup(List<Course> courseList)
    {
        int student_id = this.student.getUserId();
        for (Course course: courseList)
        {
            int course_id = course.getCourseId();
            String sql = String.format("SELECT u.group_id, g.course_id, count(u.group_id) AS group_count FROM student_has_group AS u " +
                    "INNER JOIN `group` AS g ON g.id = u.group_id " +
                    "WHERE g.course_id = %d " +
                    "GROUP BY u.group_id " +
                    "ORDER BY u.group_id DESC " +
                    "LIMIT 1;", course_id);
            try {
                PreparedStatement preparedStatement = getStatement(sql);
                ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
                if (resultSet.next())
                {
                    int group_id = resultSet.getInt(1);
                    int group_size = resultSet.getInt(3);
                    if (group_size >= 10)//@author Ismael Ben Cherif: If a group for a course has 10 people
                        //or more, an new group is created and a random teacher is assigned to that group.
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
                        //@author Ismael Ben Cherif: add student to excisting group and add student to course
                    }
                }
                else {
                   //@author Ismael Ben Cherif: create new group and add student and teacher to it and
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

    /**
     * @author Ismael Ben Cherif
     * A mehod that deletes the student from the groups and courses provided.
     */
    public void deleteStudentFromCourseAndGroup(List<Course> courseList)
    {
        for (Course course: courseList)
        {
            courseDAO.deleteStudentFromCourseAndGroup(course,this.student);
        }
    }

    /**
     * @author Ismael Ben Cherif
     * Selects a random teacher provided by the List of teachers
     */
    public User getRandomTeacher(List<User> teachers)
    {
        User returnValue;
        int i = random.nextInt(teachers.size());
        returnValue = teachers.get(i);
        return returnValue;
    }

    /**
     * @author Ismael Ben Cherif
     * Genreates a group name for the first group of that course.
     */
    public String generatedGroupName(Course course)
    {
        StringBuilder groupName = new StringBuilder();
        String firstPart = course.getName() + " ";
        groupName.append(firstPart);
        String secondPart = String.format("%03d", 1);
        groupName.append(secondPart);
        return groupName.toString();
    }

    /**
     * @author Ismael Ben Cherif
     * Generates a groupname depending on the number of groups.
     * If theyre are 999 groups the user wil get a message to delete older
     * groups and the naming of the groups will start at 001 again.
     */
    public String generatedGroupName(Course course, int number)
    {
        if (999%number == 0)
        {
            if (AlertHelper.confirmationDialog("Verwijder oudere groepen " +
                    "De benaming begint weer vanaf 1 en dubbele " +
                    "namen kunnen gaan voorkomen als oude groepen " +
                    "niet verwijderd worden.")) {
                int maxNumber = 999;
                int multiplacation = number / maxNumber;
                int newNumber = 1 + (number - (multiplacation * maxNumber));
                StringBuilder groupName = new StringBuilder();
                String firstPart = course.getName() + " ";
                groupName.append(firstPart);
                String secondPart = String.format("%03d", newNumber);
                groupName.append(secondPart);
                return groupName.toString();
            }
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
        return "nameless";
    }
}
