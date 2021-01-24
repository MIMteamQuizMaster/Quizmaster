package model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CourseTest {
    /**
     * @author Ismael Ben Cherif
     * @verifies add a group to a course.
     * @see Course#addGroup(Group)
     */
    @Test
    public void addGroup_shouldAddAGroupToACourse() throws Exception {
        //Arrange
        List<User> students = new ArrayList<>();
        students.add(new User(8, "h", "G"));
        students.add(new User(99,"hg","grt"));
        Course newCourse = new Course("Math", new User(1, "Hans","Mer"));
        Group newGroup  = new Group("Tek",
                new User(4, "H", "M"), students);
        //Act
        newCourse.addGroup(newGroup);
        //Assert
        Assert.assertEquals(newGroup, newCourse.getGroups().get(0));
    }
}
