package database.mysql;

import com.mysql.cj.xdevapi.Result;
import model.Answer;
import model.Course;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends AbstractDAO {

    public CourseDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public Course getCourseById(int course_id) {
        String sql = "SELECT name, coordinator_id FROM course WHERE id = " + course_id;

        try {
            Course mpCourse;
            PreparedStatement preparedStatement = getStatement(sql);
            ResultSet rs = executeSelectPreparedStatement(preparedStatement);

            // need coordinator to create new user

//            String answerToQuestion = rs.getString(4);
//            int correct = rs.getInt(3);
//            boolean trueFalse = false;
//            if (correct == 1)
//            {
//                trueFalse = true;
//            }
//            answer = new Answer(trueFalse, answerToQuestion);
//            answer.setId(rs.getInt(1));
//            answer.setQuestionId(rs.getInt(2));
//            answers.add(answer);

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }


        return null;
    }

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




//    public List<Hond> getHondByKlant(mpKlant) {
//        String sql = "Select k.klant from Hond h, Klant k where k.klantnr = h.klantnr";
//
//        List<Hond> result = new ArrayList<>();
//        try {
//            setupPreparedStatement(sql);
//            ResultSet rs = executeSelectStatement();
//
//            while(rs.next()) {
//                int chipnr = rs.getInt("chipnr");
//                String hondnaam = rs.getString("hondnaam");
//                String ras = rs.getString("ras");
//                int klantnr = rs.getInt("klantnr");
//                String telefoon = rs.getString("telefoon");
//                Klant resultElement = new Klant(klantnr, voorletters, voorvoegsels, achternaam, telefoon);
//                result.add(resultElement);
//            }
//
//
//        } catch (SQLException e) {
//            System.out.println("SQL error " + e.getMessage());
//        }
//        return result;
//    }
//
//    public Klant getKlantById (int klantnummer) {
//        String sql = "SELECT * FROM Klant WHERE klantnr = ?";
//        Klant result = null;
//        try {
//            setupPreparedStatement(sql);
//            preparedStatement.setInt(1, klantnummer);
//            ResultSet rs = executeSelectStatement();
//
//            while (rs.next()) {
//
//                int klantnr = rs.getInt("klantnr");
//                String voorletters = rs.getString("voorletters");
//                String voorvoegsel = rs.getString("voorvoegsels");
//                String achternaam = rs.getString("achternaam");
//                String telefoon = rs.getString("telefoon");
//
//                result = new Klant(klantnr, voorletters, voorvoegsel, achternaam, telefoon);
//            }
//        } catch (SQLException e) {
//            System.out.println("SQL error " + e.getMessage());
//        }
//        return result;
//    }
//    // commit test
}