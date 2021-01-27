package controller.Interface;

import database.mysql.DBAccess;
import database.mysql.User_has_groupDAO;
import model.Course;
import model.User;

import java.util.List;

public class StudentSignInOutInterface {

    private DBAccess dbAccess;
    private List<Course> courseList;
    private User student;
    private User_has_groupDAO dao;

    public StudentSignInOutInterface(DBAccess dbAccess, User student) {
        this.dbAccess = dbAccess;;
        this.student = student;
        this.dao = new User_has_groupDAO(this.dbAccess, this.student);
    }

    public void addStudentsToGroupAndClass(List<Course> courseList)
    {
        this.dao.addStudentToCourseAndGroup(courseList);
    }

    public void deleteStudentFromCourseAndGroup(List<Course> courseList)
    {
        this.dao.deleteStudentFromCourseAndGroup(courseList);
    }
}
