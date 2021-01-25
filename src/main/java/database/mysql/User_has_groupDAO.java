package database.mysql;

import model.Course;
import model.User;
import model.AtudentSignInOut;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User_has_groupDAO extends AbstractDAO {
    public User_has_groupDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    private List<Integer> group_id;

    public List<AtudentSignInOut> groupIdPerCourse(List<Course> courseList, User student)
    {
        List<AtudentSignInOut> returnValue = new ArrayList<>();
        for (Course course: courseList)
        {
            int course_id = course.getDbId();
            String sql = String.format("SELECT count(group_id) AS group_size, group_id, course_id " +
                    "FROM user_has_group " +
                    "group by group_id " +
                    "HAVING course_id = %d;", course_id);
            try {
                PreparedStatement preparedStatement = getStatement(sql);
                ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
                if (resultSet.getFetchSize() == 0)
                {
                    returnValue.add(1);
                }
                else {
                    while (resultSet.next()) {
                        int group_id = resultSet.getInt(2);
                        int group_size = resultSet.getInt(1);
                        if (group_size >= 10)
                        {
                            group_id++;
                            returnValue.add(group_id);
                        }
                        else
                        {
                            returnValue.add(group_id);
                        }
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return returnValue;
    }

    public List<Integer> teacher_ids()
    {
        List<Integer> returnValue = new ArrayList<>();
        String sql = "SELECT user_id, role_id FROM user_role " +
                "WHERE role_id = 2;";
        try {
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet resultSet = executeSelectPreparedStatement(preparedStatement);
            while (resultSet.next())
            {
                int teacher_id = resultSet.getInt(1);
                returnValue.add(teacher_id);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return returnValue;
    }

    public boolean createAGroupForStudent(List<Course> courseList)
    {
        for (Course course: courseList)
        {

        }
        return false;
    }

}
