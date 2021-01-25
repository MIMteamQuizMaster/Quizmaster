package database.mysql;

import launcher.Main;
import model.Course;
import model.Group;
import model.User;

import java.util.List;


public class DomainClass implements GenericDAO {
    DBAccess dbAccess;
    UserDAO userDAO;
    CourseDAO courseDAO;


    public DomainClass() {
        dbAccess = Main.getDBaccess();
        userDAO = new UserDAO(dbAccess);
        courseDAO = new CourseDAO(dbAccess);
    }


    @Override
    public boolean isValidUser(int u, String pass) {
        return userDAO.isValidUser(u,pass);
    }

    @Override
    public User getUser(int user_id) {
        return userDAO.getUser(user_id);
    }

    @Override
    public boolean saveCourse(Course c) {
        return courseDAO.saveCourse(c);
    }

    @Override
    public boolean deleteCourse(Course c) {
        return false;
    }

    @Override
    public List<Group> getGroupsOfCourse(Course course) {
        return courseDAO.getGroupsOfCourse(course);
    }
}
