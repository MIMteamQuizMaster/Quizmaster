package database.mysql;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import model.Course;
import model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CoordinatorDao extends AbstractDAO {
    User coordinator;
    public CoordinatorDao(DBAccess dBaccess) {
        super(dBaccess);
    }

    public void setCoordinator(User coordinator) {
        this.coordinator = coordinator;
    }

    public ObservableList<Course> getMyCourses(){
        ObservableList<Course> courseList = FXCollections.observableArrayList();
        String query = "SELECT * FROM course WHERE coordinator_id=? and endDate > ?";
        try {
            PreparedStatement ps = getStatement(query);
            ps.setInt(1,this.coordinator.getUserId());
            ps.setDate(2,java.sql.Date.valueOf(java.time.LocalDate.now()));
            ResultSet rs = executeSelectPreparedStatement(ps);
            while(rs.next()){
                String name = rs.getString("name");

                String startDate = rs.getDate("startDate").toString();
                String endDate = rs.getDate("endDate").toString();
//                assert this.coordinator.getUserId() == rs.getInt("coordinator_id");
                Course temp = new Course(name, this.coordinator);
                temp.setStartDate(startDate);
                temp.setEndDate(endDate);
                courseList.add(temp);
            }
            return courseList;
        } catch (SQLException throwables) {
            System.out.println(throwables.getCause());
            System.out.println("Somthing went wrong while getting course lists");
            throwables.printStackTrace();
        }
        return null;
    }
}
