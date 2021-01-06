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

    public setTeacherToGroup(Teacher t, Group g) {
    }

    public setCoordinatorToGroup(Coordinator t, Group g) {
    }

    public setStudentToGroup(Student t, Group g) {
    }

    public setCoordinatorToCourse(Coordinator t, Group g) {
    }

    public boolean deleteCourse(Course c) {
    }

    public boolean deleteGroup(Group g) {
    }

    @Override
    public boolean loginUser(String username, String password) {
        return false;
    }
}
