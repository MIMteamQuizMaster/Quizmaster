package launcher;

import database.mysql.User_has_groupDAO;
import model.Course;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class Main2 {

    public static void main(String[] args) {
        List<Course> newCourseList = new ArrayList<>();
        Course course = new Course("Geschiedenis", new User(1050));
        course.setDbId(12);
        newCourseList.add(course);
        User student = new User(10099);
        User_has_groupDAO dao = new User_has_groupDAO(Main.getDBaccess(), student, newCourseList);
        dao.groupIdPerCourse(newCourseList, student);
    }
}
