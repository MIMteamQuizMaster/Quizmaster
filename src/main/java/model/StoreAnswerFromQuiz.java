package model;

import controller.FillOutFormMultipleAnswersController;
import controller.fx.AnswerFormFX;
import database.mysql.DBAccess;
import database.mysql.GradeDAO;
import database.mysql.UserAnswerLogDAO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StoreAnswerFromQuiz {


    private Quiz quizFromStudent;
    private User student;
    private List<List<AnswerFormFX>> answerformFXs;
    private DBAccess dbAccess;



    public StoreAnswerFromQuiz(Quiz quizFromStudent, User student, List<List<AnswerFormFX>> answerformFXs,
                               DBAccess dbAccess) {
        this.quizFromStudent = quizFromStudent;
        this.student = student;
        this.answerformFXs = answerformFXs;
        this.dbAccess = dbAccess;
    }

    //Vergeijk gegevenantwoord met goede antwoord.
    public List<AnswerFormFX> returnIfAnswerWasRight()
    {
        List<AnswerFormFX> returnValue = new ArrayList<>();
        for (List<AnswerFormFX> answerFormFXES: this.answerformFXs)
        {
            for (AnswerFormFX answerFormFX: answerFormFXES)
            {
                if (answerFormFX.getAnswer().getIsGivenAnswer() && answerFormFX.getAnswer().isCorrect())
                {
                    returnValue.add(answerFormFX);
                }
            }
        }
        return returnValue;
    }

    //Save the given answers to database
    public void saveAnswersToGradeAndAnswerLog()
    {
        GradeDAO gradeDAO = new GradeDAO(this.dbAccess);
        UserAnswerLogDAO userAnswerDAO = new UserAnswerLogDAO(this.dbAccess);
        int key = gradeDAO.storeGrade(createAGradeObject());
        userAnswerDAO.storeToAnswerLog(getListOfGivenAnswers(),
                key, student);
    }

    //create a Grade Object.
    public Grade createAGradeObject()
    {
        Grade returnValue = null;
        returnValue = new Grade(this.quizFromStudent.getIdquiz(),
                calculateGrade(), student.getUserId());

        return returnValue;
    }

    //calculate the grade
    public double calculateGrade()
    {
        double returnValue = 0;
        double numberOfCorrectAnswers = returnIfAnswerWasRight().size();
        double numberOfQuestions = this.quizFromStudent.getQuestions().size();
        returnValue = (numberOfCorrectAnswers/numberOfQuestions)*10.0;

        return round(returnValue,2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //create Grade objrct
    public Grade returnGradeObject()
    {
        Grade returnValue = null;
        returnValue = new Grade(this.quizFromStudent.getIdquiz(), calculateGrade(),
                this.student.getUserId());

        return returnValue;
    }

    private List<AnswerFormFX> getListOfGivenAnswers()
    {
        List<AnswerFormFX> returnValue = new ArrayList<>();
        for (List<AnswerFormFX> answerFormFXES: this.answerformFXs)
        {
            for (AnswerFormFX answerFormFX: answerFormFXES)
            {
                if (answerFormFX.getAnswer().getIsGivenAnswer())
                {
                    returnValue.add(answerFormFX);
                }
            }
        }

        return returnValue;
    }
}
