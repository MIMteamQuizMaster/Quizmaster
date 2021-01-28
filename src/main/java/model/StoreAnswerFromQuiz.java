package model;

import controller.FillOutFormMultipleAnswersController;
import controller.fx.AnswerFormFX;

import java.util.ArrayList;
import java.util.List;

public class StoreAnswerFromQuiz {


    Quiz quizFromStudent;
    User student;
    List<List<AnswerFormFX>> answerformFXs = new ArrayList<List<AnswerFormFX>>();
    //ontvang de ingevulde vragen per quiz.


    public StoreAnswerFromQuiz(Quiz quizFromStudent, User student, List<List<AnswerFormFX>> answerformFXs) {
        this.quizFromStudent = quizFromStudent;
        this.student = student;
        this.answerformFXs = answerformFXs;
    }

    //Vergeijk gegevenantwoord met goede antwoord.
    public List<Boolean> returnIfAnswerWasRightOrWrong()
    {
        List<Boolean> returnValue = new ArrayList<>();


        return returnValue;
    }

    //Save the answers to database
    public void saveAnswersToGradeAndAnswerLog()
    {

    }

    //calculate the grade
    public double calculateGrade()
    {
        double returnValue = 0;

        return returnValue;
    }

    //create Grade objrct
    public Grade returnGradeObject(User student, Quiz quiz, double grade)
    {
        Grade returnValue;

        return null;
    }
}
