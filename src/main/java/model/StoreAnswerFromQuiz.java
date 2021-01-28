package model;

import controller.FillOutFormMultipleAnswersController;
import controller.fx.AnswerFormFX;

import java.util.ArrayList;
import java.util.List;

public class StoreAnswerFromQuiz {


    Quiz quizFromStudent;
    User student;
    List<AnswerFormFX> answers;
    //ontvang de ingevulde vragen per quiz.



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

    }
}
