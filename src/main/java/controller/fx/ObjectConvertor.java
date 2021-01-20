package controller.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import java.util.List;

public class ObjectConvertor {

    /**
     * @author M.J. Moshiri
     *
     * Convert User objects to UserFX objects
     * @param userObservableList a list of User objects
     * @return a observableList of UserFx objects
     */
    public static ObservableList<UserFx> convertUserToUserFX(List<User> userObservableList){
        ObservableList<UserFx> listUsers = FXCollections.observableArrayList();
        for (User u:userObservableList) {
            listUsers.add(new UserFx(u));
        }
        return listUsers;
    }

    /**
     * @author M.J. Moshiri
     *
     * Convert Course objects to CourseFx objects
     * @param userObservableList list of Course objects
     * @return a observableList CourseFx objects
     */
    public static ObservableList<CourseFx> convertCoursetoCourseFX(List<Course> userObservableList){
        ObservableList<CourseFx> listedCourses = FXCollections.observableArrayList();
        for (Course c:userObservableList) {
            listedCourses.add(new CourseFx(c));
        }
        return listedCourses;
    }
    /**
     * @author M.J. Moshiri
     *
     * Convert Quiz objects to QuizFX objects
     * @param userObservableList list of Quiz objects
     * @return an observableList QuizFx objects
     */
    public static ObservableList<QuizFx> convertQuizToQuizFX(List<Quiz> userObservableList){
        ObservableList<QuizFx> listedQuizes = FXCollections.observableArrayList();
        for (Quiz q:userObservableList) {
            listedQuizes.add(new QuizFx(q));
        }
        return listedQuizes;
    }

    /**
     * @author M.J. Moshiri
     *
     * Convert Question objects to QuestionFX objects
     * @param list of Question objects
     * @return an observableList QuestionFx objects
     */
    public static ObservableList<QuestionFx> convertQuestionToQuestionFX(List<Question> list){
        ObservableList<QuestionFx> listedQuestion = FXCollections.observableArrayList();
        for (Question q: list) {
            listedQuestion.add(new QuestionFx(q));
        }
        return listedQuestion;
    }

    /**
     * @author M.J. Moshiri
     *
     * Convert Answer objects to AnswerFX objects
     * @param list of Answer Objects
     * @return an observableList of AnswerFX objects
     */
    public static ObservableList<AnswerFx> convertAnswerToAnswerFX(List<Answer> list){
        ObservableList<AnswerFx> listedAnswer = FXCollections.observableArrayList();
        for (Answer a: list) {
            listedAnswer.add(new AnswerFx(a));
        }
        return listedAnswer;
    }
}
