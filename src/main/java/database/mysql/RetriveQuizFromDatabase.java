package database.mysql;

import database.mysql.AnswerDAO;
import database.mysql.DBAccess;
import database.mysql.QuestionDAO;
import launcher.Main;
import model.Answer;
import model.Question;
import model.Quiz;

import java.util.ArrayList;
import java.util.List;

public class RetriveQuizFromDatabase {

    private DBAccess dbAccess = Main.getDBaccess();
    private QuestionDAO questionDAO = new QuestionDAO(dbAccess);
    private AnswerDAO answerDAO = new AnswerDAO(dbAccess);
    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();

    /**
     * @author Ismael Ben Cherif
     * Returns a quiz qith the answers mixed.
     * @param quiz
     * @return
     */


    public Quiz returnQuizFromDatabase(Quiz quiz)
    {
        this.questions = questionDAO.getQuestions (quiz);
        for (Question question: this.questions)
        {
            question.mixAnswers();
        }
        quiz.setQuestions(this.questions);

        return quiz;
    }

}
