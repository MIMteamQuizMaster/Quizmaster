package controller.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.Class;

public class ObjectConvertor {

    /**
     * Convert User objects to UserFX objects
     * @param userObservableList a list of User objects
     * @return a observableList of UserFx objects
     */
    public static ObservableList<UserFx> convertUserToUserFX(ObservableList<User> userObservableList){
        ObservableList<UserFx> listUsers = FXCollections.observableArrayList();
        for (User u:userObservableList) {
            listUsers.add(new UserFx(u));
        }
        return listUsers;
    }

    /**
     * Convert Course objects to CourseFx objects
     * @param userObservableList list of Course objects
     * @return a observableList CourseFx objects
     */
    public static ObservableList<CourseFx> convertCoursetoCourseFX(ObservableList<Course> userObservableList){
        ObservableList<CourseFx> listedCourses = FXCollections.observableArrayList();
        for (Course c:userObservableList) {
            listedCourses.add(new CourseFx(c));
        }
        return listedCourses;
    }
    /**
     * Convert Quiz objects to QuizFX objects
     * @param userObservableList list of Quiz objects
     * @return an observableList QuizFx objects
     */
    public static ObservableList<QuizFx> convertQuizToQuizFX(ObservableList<Quiz> userObservableList){
        ObservableList<QuizFx> listedQuizes = FXCollections.observableArrayList();
        for (Quiz q:userObservableList) {
            listedQuizes.add(new QuizFx(q));
        }
        return listedQuizes;
    }

    /**
     * Convert Question objects to QuestionFX objects
     * @param list of Question objects
     * @return an observableList QuestionFx objects
     */
    public static ObservableList<QuestionFx> convertQuestionToQuestionFX(ObservableList<Question> list){
        ObservableList<QuestionFx> listedQuestion = FXCollections.observableArrayList();
        for (Question q: list) {
            listedQuestion.add(new QuestionFx(q));
        }
        return listedQuestion;
    }

    /**
     * Convert Answer objects to AnswerFX objects
     * @param list
     * @return an observableList of AnswerFX objects
     */
    public static ObservableList<AnswerFx> convertAnswerToAnswerFX(ObservableList<Answer> list){
        ObservableList<AnswerFx> listedAnswer = FXCollections.observableArrayList();
        for (Answer a: list) {
            listedAnswer.add(new AnswerFx(a));
        }
        return listedAnswer;
    }

    /**
     * Convert Class objects to ClassFX objects
     * @param userObservableList list of Class objects
     * @return a observableList ClassFX objects
     */
    public static ObservableList<ClassFX> convertClassToClassFX(ObservableList<Class> userObservableList){
        ObservableList<ClassFX> listedClasses = FXCollections.observableArrayList();
        for (Class c:userObservableList) {
            listedClasses.add(new ClassFX(c));
        }
        return listedClasses;
    }
}
