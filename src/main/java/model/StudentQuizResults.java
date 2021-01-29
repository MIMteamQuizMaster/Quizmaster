package model;

import controller.fx.GradeFX2;
import controller.fx.ObjectConvertor;
import database.mysql.DBAccess;
import database.mysql.GradeDAO;
import javafx.collections.ObservableList;

import java.util.List;

public class StudentQuizResults {

    private User student;
    private DBAccess dbAccess;

    public StudentQuizResults(User student, DBAccess dbAccess) {
        this.student = student;
        this.dbAccess = dbAccess;
    }

    //ophalen en maken van grade object
    public List<Grade> grades()
    {
        GradeDAO dao = new GradeDAO(this.dbAccess);
        return dao.getAllGradesForResultScreen(this.student);
    }

    public ObservableList<GradeFX2> observableListGardes()
    {
        return ObjectConvertor.convertGradeToGradeFX2(grades());
    }
}
