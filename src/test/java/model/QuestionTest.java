package model;

import org.junit.Assert;
import org.junit.Test;

public class QuestionTest {

    /**
     * @verifies mix answer and add them to mixedAnswers variable so  that the size of Answers and mixed Answer be the same
     * @see Question#mixAnswers()
     */
    @Test
    public void mixAnswers_shouldMixAnswerAndAddThemToMixedAnswersVariableSoThatTheSizeOfAnswersAndMixedAnswerBeTheSame() throws Exception {
        //TODO auto-generated
        // Arrange
        Question question = new Question("Hoe oud be jij");
        question.addAnswer(new Answer(false,"13"));
        question.addAnswer(new Answer(true,"30"));
        question.addAnswer(new Answer(false,"1"));
        question.addAnswer(new Answer(false,"88"));
        // Act
        question.mixAnswers();
        int a = question.getAnswers().size();
        int b = question.getMixedAnswers().size();
        // Assert
        Assert.assertEquals(a,b);
//        Assert.fail("Not yet implemented");
    }
}
