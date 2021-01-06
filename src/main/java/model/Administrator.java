package model;

import java.util.List;

public class Administrator extends User  {

    public Administrator(String name, String password, List<Role> role) {
        super(name, password, role);
    }

    public boolean makeCourse() {
        return true;
    }

    public boolean makeGroup() {
        return true;
    }

    public void setTeacherToGroup(Teacher t, Group g) {
        g.setTeacher(t);
    }

    public void setCoordinatorToGroup(Coordinator t, Group g) {
        g.setCoordinator(t);
    }

    public void setStudentToGroup(Student t, Group g) {
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
