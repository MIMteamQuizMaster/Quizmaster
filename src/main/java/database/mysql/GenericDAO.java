package database.mysql;

import model.Course;
import model.Group;
import model.Role;
import model.User;


import java.util.HashMap;
import java.util.List;

public interface GenericDAO<T> {
    boolean isValidUser(int u , String pass);
    User getUser(int user_id);
    boolean saveCourse(Course c);
    boolean deleteCourse(Course c);
    List<Group> getGroupsOfCourse(Course course);
    List<User> getAllUsers();
    User SaveUser(User u );
    String getCredential(int userId);
    void setCredentials(int userId, String password);
    void setEnd(User u );


}
