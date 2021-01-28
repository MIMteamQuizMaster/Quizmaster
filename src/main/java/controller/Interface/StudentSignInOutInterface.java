package controller.Interface;

import database.mysql.DBAccess;
import database.mysql.User_has_groupDAO;
import model.Course;
import model.User;

import java.util.List;

public class StudentSignInOutInterface {

    /**
     * @author Ismael Ben Cherif
     * Deze klas z0rgt voor de link tussende controller en de user
     * User_has_groupDAO.
     */

    private DBAccess dbAccess;
    private List<Course> courseList;
    private User student;
    private User_has_groupDAO dao;

    public StudentSignInOutInterface(DBAccess dbAccess, User student) {
        this.dbAccess = dbAccess;;
        this.student = student;
        this.dao = new User_has_groupDAO(this.dbAccess, this.student);
    }

    /**
     * author Ismael Ben Cherif
     * Zorgt voor het opslaan van de studenten die een cursus hebben gekozen in een groep en cursus.
     * @param courseList
     */
    public void addStudentsToGroupAndClass(List<Course> courseList)
    {
        this.dao.addStudentToCourseAndGroup(courseList);
    }


    /**
     * author Ismael Ben Cherif
     * Zorgt voor het verwijderen van de studenten die een cursus hebben gekozen in een groep en cursus.
     * @param courseList
     */
    public void deleteStudentFromCourseAndGroup(List<Course> courseList)
    {
        this.dao.deleteStudentFromCourseAndGroup(courseList);
    }
}
