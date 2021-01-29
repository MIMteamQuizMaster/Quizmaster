package controller.Interface;

import controller.fx.AnswerFormFX;
import database.mysql.DBAccess;
import model.Quiz;
import model.StoreAnswerFromQuiz;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class FillOutFormInterface {

    private Quiz quizFromStudent;
    private User student;
    private List<List<AnswerFormFX>> answerformFXs = new ArrayList<List<AnswerFormFX>>();
    private DBAccess dbAccess;


    public FillOutFormInterface(Quiz quizFromStudent, User student, List<List<AnswerFormFX>> answerformFXs,
                                DBAccess dbAccess) {
        this.quizFromStudent = quizFromStudent;
        this.student = student;
        this.answerformFXs = answerformFXs;
        this.dbAccess = dbAccess;
    }

    public void storeAnswers()
    {
        StoreAnswerFromQuiz storeAnswerFromQuiz = new StoreAnswerFromQuiz(this.quizFromStudent,
                this.student,this.answerformFXs,this.dbAccess);
        storeAnswerFromQuiz.saveAnswersToGradeAndAnswerLog();
    }

}
