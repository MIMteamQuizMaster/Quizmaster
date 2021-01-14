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

public class CreateQuizFromDatabase {

    private DBAccess dbAccess = Main.getDBaccess();
    private QuestionDAO questionDAO = new QuestionDAO(dbAccess);
    private AnswerDAO answerDAO = new AnswerDAO(dbAccess);
    private Quiz quiz;
    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();


    public Quiz returnQuizFromDatabase()
    {
        this.quiz = new Quiz(0.55, "Math");
        this.quiz.setIdquiz(1);
        this.questions = questionDAO.getQuestionsForQuize(this.quiz);
        for (Question question: this.questions)
        {
            this.answers = answerDAO.getAnswersForQuestion(question);
            question.addAnswersToQuestion(this.answers);
        }
        for (Question question: this.questions)
        {
            question.mixAnswers();
        }
        this.quiz.importQuestions(this.questions);

        return this.quiz;
    }

}
