package model;

import java.util.List;

public class Administrator extends User  {

    public Administrator(int userId, String firstname, String lastname) {
        super(userId, firstname, lastname);
    }

    public Course makeCourse(String name, Coordinator coordinator) {
        return new Course(name,coordinator);
    }

    public Group makeGroup(String name,Teacher teacher,Coordinator coordinator,List<Student> student) {
        return new Group(name,teacher,coordinator, student);
    }

    public void setTeacherToGroup(Teacher t, Group g) {
        g.setTeacher(t);
    }

    public void setCoordinatorToGroup(Coordinator t, Group g) {
        g.setCoordinator(t);
    }

    public void addStudentToGroup(Student t, Group g) {
        g.addStudentToGroup(t);
    }

    public void setCoordinatorToCourse(Coordinator t, Course c) {
        c.setCoordinator(t);
    }

    public boolean deleteCourse(Course c) {
        return true;
    }

    public boolean deleteGroup(Group g) {
        return true;
    }

    @Override
    public boolean loginUser(String username, String password) {
        return false;
    }
}
