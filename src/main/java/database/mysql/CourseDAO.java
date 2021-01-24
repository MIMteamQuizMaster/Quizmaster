package database.mysql;
import model.Course;
import model.Group;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CourseDAO extends AbstractDAO {
    private UserDAO userDAO;

    public CourseDAO(DBAccess dBaccess) {
        super(dBaccess);
        userDAO = new UserDAO(dBaccess);

    }

    /**
     * Use parameter to retrieve the selected course from the database. Use retrieved coordinator_id to retrieve corresponding coordinator.
     * Build new course object.
     * @param course_id the id of the course.
     * @return the new course object created from the database.
     */
    public Course getCourseById(int course_id) {
        String sql_course = "SELECT name, coordinator_user_id, startDate, endDate FROM course WHERE id = " + course_id;
        Course course = null;

        try {
            Course mpCourse;
            PreparedStatement psCourse = getStatement(sql_course);
            ResultSet rsCourse = executeSelectPreparedStatement(psCourse);
            rsCourse.next();
            String courseName = rsCourse.getString(1);
            int coordinatorId = rsCourse.getInt(2);
            String startDate = rsCourse.getString(3);
            String endDate = rsCourse.getString(4);

            String sql_coordinator = "SELECT firstname, lastname FROM user WHERE user_id = " + coordinatorId;
            PreparedStatement psCoordinator = getStatement(sql_coordinator);
            ResultSet rsCoordinator = executeSelectPreparedStatement(psCoordinator);
            rsCoordinator.next();
            String firstName = rsCoordinator.getString(1);
            String lastName = rsCoordinator.getString(2);

            User coordinator = new User(coordinatorId,firstName,lastName);
            course = new Course(courseName,coordinator);
            course.setDbId(course_id);
            course.setStartDate(startDate);
            course.setEndDate(endDate);

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }

        return course;
    }

    /**
     * Insert passed course into database, extracting each attribute from preparedStatement
     * @param mpCourse is the course passed by the user
     */
    public void storeCourse(Course mpCourse) {
        String sql = "INSERT INTO course(coordinator_user_id, name, startDate, endDate)" + "VALUES (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            preparedStatement.setInt(1, mpCourse.getCoordinator().getUserId());
            preparedStatement.setString(2, mpCourse.getName());
            preparedStatement.setString(3, mpCourse.getStartDate());
            preparedStatement.setString(4, mpCourse.getEndDate());
            executeManipulatePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    /**
     * @author M.J. Moshiri
     * @return
     */
    public List<Course> getAllCourses(){
        List<Course> courseList = new ArrayList<>();
        String query="SELECT c.id, c.name , c.startDate, c.endDate, c.coordinator_user_id as coordinator FROM course c";

        try {
            PreparedStatement preparedStatement = getStatement(query);
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);
            while (rs.next()){
                int dbId = rs.getInt("id");
                String name = rs.getString("name");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");
                int coordinator_id = rs.getInt("coordinator");
                /// if user is not valid coordinator would be null
                User coordinator = userDAO.getUser(coordinator_id);
                Course course = new Course(dbId,name,coordinator,startDate,endDate);
                course.setGroups(getGroupsOfCourse(course));
                courseList.add(course);

            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            System.out.println("fault in getAllCourses");
        }

        return courseList;
    }

    /**
     * @author M.J. Moshiri
     * @return
     */
    public List<Group> getGroupsOfCourse(Course course){
        List<Group> groupList = new ArrayList<>();
        String query = "SELECT * FROM `group` WHERE course_id=?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,course.getDbId());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                int db =rs.getInt("id");
                String name = rs.getString("name");
                int docentID = rs.getInt("docent");
                User docent = null;
                if (docentID!=0){docent= userDAO.getUser(docentID);}
                Group group = new Group(db,name,docent);
                group.setStudents(getStudentsOfGroup(group));
                groupList.add(group);

            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            System.out.println("fout in getGroupsOfCourse");
        }
        return groupList;
    }

    /**
     * @author M.J. Moshiri
     * @return
     */
    public List<User> getStudentsOfGroup(Group g){
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user_has_group WHERE group_id = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,g.getDbID());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                int student_id = rs.getInt("student_user_id");
                User student = userDAO.getUser(student_id);
                if(student!=null){
                    userList.add(student);
                }
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
            System.out.println("fout in getStudentsOfGroup");
        }
        return userList;
    }

    /**
     * @author M.J. Moshiri
     * @return
     */
    public List<User> getALlValidCoordinators(){
        List<User> userList = new ArrayList<>();
        String query = "SELECT u.user_id FROM user u, user_role ur, role r where u.user_id = ur.user_id and ur.role_id = r.id and r.name = \"Coordinator\" and \n" +
                "( u.deletionDate > ? or u.deletionDate is null) and  ( ur.endDate > ? or ur.endDate is null);";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setDate(1,java.sql.Date.valueOf(java.time.LocalDate.now()));
            ps.setDate(2,java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                User coordinator = userDAO.getUser(rs.getInt("user_id"));
                if(coordinator!=null){
                    userList.add(coordinator);
                }

            }
            return userList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * @author M.J. Moshiri
     * @return
     */
    public boolean assignCoordinatorToCourse(User coordinator , Course course){
        String query = "UPDATE course SET coordinator_user_id = ? where id = ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,coordinator.getUserId());
            ps.setInt(2,course.getDbId());
            executeManipulatePreparedStatement(ps);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;

    }

    /**
     * @author M.J. Moshiri
     * @return
     */
    public List<User> getStudentsWithNoGroupAssigned(Course course){
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM student_has_course\n" +
                "where course_id = ?\n" +
                "and student_user_id not in \n" +
                "(select student_user_id from user_has_group where group_id in (SELECT id FROM `group` where course_id = ?))";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,course.getDbId());
            ps.setInt(2,course.getDbId());
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                int student_id = rs.getInt("student_user_id");
                User u = userDAO.getUser(student_id);
                if(u!= null){
                    userList.add(u);
                }
            }
            return userList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}