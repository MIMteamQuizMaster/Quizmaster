package model;

import java.util.List;
import java.util.Random;

public class StudentSignInOut {

    private List<Integer> student_idList;
    private List<Integer> teacher_idList;
    private List<Integer> group_idList;
    private List<Integer> course_idList;
    private List<Integer> id;

    private Random random = new Random();

    private int student_id;
    private int teacher_id;
    private int group_id;
    private int course_id;

    public StudentSignInOut() {
    }

    public StudentSignInOut(int student_id, int teacher_id, int group_id, int course_id) {
        this.student_id = student_id;
        this.teacher_id = teacher_id;
        this.group_id = group_id;
        this.course_id = course_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public List<Integer> getStudent_idList() {
        return student_idList;
    }

    public void setStudent_idList(List<Integer> student_idList) {
        this.student_idList = student_idList;
    }

    public List<Integer> getTeacher_idList() {
        return teacher_idList;
    }

    public void setTeacher_idList(List<Integer> teacher_idList) {
        this.teacher_idList = teacher_idList;
    }

    public List<Integer> getGroup_idList() {
        return group_idList;
    }

    public void setGroup_idList(List<Integer> group_idList) {
        this.group_idList = group_idList;
    }

    public List<Integer> getCourse_idList() {
        return course_idList;
    }

    public void setCourse_idList(List<Integer> course_idList) {
        this.course_idList = course_idList;
    }

    public List<Integer> getId() {
        return id;
    }

    public void setId(List<Integer> id) {
        this.id = id;
    }

    public int getRandomTeacher(List<Integer> teachers)
    {
        int returnValue = 0;
        int i = random.nextInt(teachers.size());
        returnValue = teachers.get(i);
        return returnValue;
    }

    public String generatedGroupName(Course course)
    {
        StringBuilder groupName = new StringBuilder();
        String firstPart = course.getName() + " ";
        groupName.append(firstPart);
        char secondPart = (char) 65;
        groupName.append(secondPart);
        return groupName.toString();
    }

    public String generatedGroupName(Course course, int number)
    {
        StringBuilder groupName = new StringBuilder();
        String firstPart = course.getName() + " ";
        groupName.append(firstPart);
        char secondPart = (char) (65+number);
        groupName.append(secondPart);
        return groupName.toString();
    }
}
