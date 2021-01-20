package model;

import org.junit.Assert;
import org.junit.Test;

public class QuizTest {
    /**
     * @verifies return size of Questions list
     * @see Quiz#getTotal()
     * @author M.J. Alden-Montague
     */
    @Test
    public void getTotal_shouldReturnSizeOfQuestionsList() throws Exception {
        //TODO auto-generated
        //Assertions.fail("Not yet implemented");
        //Arrange
        Quiz quiz = new Quiz("test",7.0);
        Question question1 = new Question(1,"question 1?");
        Question question2 = new Question(2,"question 2?");
        Question question3 = new Question(3,"question 3?");
        Question question4 = new Question(4,"question 4?");
        Question question5 = new Question(5,"question 5?");
        Question question6 = new Question(6,"question 6?");
        quiz.addQuestion(question1);
        quiz.addQuestion(question2);
        quiz.addQuestion(question3);
        quiz.addQuestion(question4);
        quiz.addQuestion(question5);
        quiz.addQuestion(question6);
        //Act
        int total = quiz.getTotal();
        int total2 = quiz.getQuestions().size();
        //Assert
        Assert.assertEquals(total,total2);
    }
}
