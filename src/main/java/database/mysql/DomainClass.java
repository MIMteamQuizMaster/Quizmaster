package database.mysql;

import launcher.Main;
import model.Course;
import model.Group;
import model.Role;
import model.User;

import java.util.HashMap;
import java.util.List;


public class DomainClass implements GenericDAO {
    DBAccess dbAccess;
    UserDAO userDAO;
    CourseDAO courseDAO;
    TechnischBeheerderDAO technischBeheerderDAO;


    public DomainClass() {
        dbAccess = Main.getDBaccess();
        userDAO = new UserDAO(dbAccess);
        courseDAO = new CourseDAO(dbAccess);
        technischBeheerderDAO = new TechnischBeheerderDAO(dbAccess);
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

    @Override
    public List<User> getAllUsers() {
        return technischBeheerderDAO.getAllUsers();
    }

    @Override
    public User SaveUser(User u) {
        return technischBeheerderDAO.saveUser(u);
    }

    @Override
    public String getCredential(int userId) {
        return technischBeheerderDAO.getCredential(userId);
    }

    @Override
    public void setCredentials(int userId, String password) {
        technischBeheerderDAO.setCredential(userId,password);
    }

    @Override
    public void setEnd(User u) {
        technischBeheerderDAO.setEnd(u);
    }
}
