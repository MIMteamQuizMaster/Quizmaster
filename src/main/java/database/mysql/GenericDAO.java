package database.mysql;

import model.Course;
import model.Group;
import model.User;

import java.util.ArrayList;
import java.util.List;

public interface GenericDAO<T> {
    boolean isValidUser(int u , String pass);
    User getUser(int user_id);
    boolean saveCourse(Course c);
    boolean deleteCourse(Course c);
    List<Group> getGroupsOfCourse(Course course);
}
