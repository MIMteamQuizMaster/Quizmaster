package controller.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import model.Class;
import model.Grade;

import java.util.List;

public class ObjectConvertor {

    /**
     * @param userObservableList a list of User objects
     * @return a observableList of UserFx objects
     * @author M.J. Moshiri
     * <p>
     * Convert User objects to UserFX objects
     */
    public static ObservableList<UserFx> convertUserToUserFX(List<User> userObservableList) {
        ObservableList<UserFx> listUsers = FXCollections.observableArrayList();
        for (User u : userObservableList) {
            listUsers.add(new UserFx(u));
        }
        return listUsers;
    }

    /**
     * @param userObservableList list of Course objects
     * @return a observableList CourseFx objects
     * @author M.J. Moshiri
     * <p>
     * Convert Course objects to CourseFx objects
     */
    public static ObservableList<CourseFx> convertCoursetoCourseFX(List<Course> userObservableList) {
        ObservableList<CourseFx> listedCourses = FXCollections.observableArrayList();
        for (Course c : userObservableList) {
            listedCourses.add(new CourseFx(c));
        }
        return listedCourses;
    }

    /**
     * @param groupList list of Group objects
     * @return a observableList GroupFx objects
     * @author M.J. Moshiri
     * <p>
     * Convert Group objects to GroupFX objects
     */
    public static ObservableList<GroupFx> convertGrouptoGroupFX(List<Group> groupList) {
        ObservableList<GroupFx> list = FXCollections.observableArrayList();
        for (Group c : groupList) {
            list.add(new GroupFx(c));
        }
        return list;
    }

    /**
     * @param userObservableList list of Quiz objects
     * @return an observableList QuizFx objects
     * @author M.J. Moshiri
     * <p>
     * Convert Quiz objects to QuizFX objects
     */
    public static ObservableList<QuizFx> convertQuizToQuizFX(List<Quiz> userObservableList) {
        ObservableList<QuizFx> listedQuizes = FXCollections.observableArrayList();
        for (Quiz q : userObservableList) {
            listedQuizes.add(new QuizFx(q));
        }
        return listedQuizes;
    }

    /**
     * @param list of Question objects
     * @return an observableList QuestionFx objects
     * @author M.J. Moshiri
     * <p>
     * Convert Question objects to QuestionFX objects
     */
    public static ObservableList<QuestionFx> convertQuestionToQuestionFX(List<Question> list) {
        ObservableList<QuestionFx> listedQuestion = FXCollections.observableArrayList();
        for (Question q : list) {
            listedQuestion.add(new QuestionFx(q));
        }
        return listedQuestion;
    }

    /**
     * @param list of Answer Objects
     * @return an observableList of AnswerFX objects
     * @author M.J. Moshiri
     * <p>
     * Convert Answer objects to AnswerFX objects
     */
    public static ObservableList<AnswerFx> convertAnswerToAnswerFX(List<Answer> list) {
        ObservableList<AnswerFx> listedAnswer = FXCollections.observableArrayList();
        for (Answer a : list) {
            listedAnswer.add(new AnswerFx(a));
        }
        return listedAnswer;
    }

    /**
     * Convert Class objects to ClassFX objects
     *
     * @param userObservableList list of Class objects
     * @return a observableList ClassFX objects
     */
    public static ObservableList<ClassFX> convertClassToClassFX(ObservableList<Class> userObservableList) {
        ObservableList<ClassFX> listedClasses = FXCollections.observableArrayList();
        for (Class c : userObservableList) {
            listedClasses.add(new ClassFX(c));
        }
        return listedClasses;
    }

    /**
     * Convert Class objects to ClassFX objects
     *
     * @param userObservableList list of Class objects
     * @return a observableList ClassFX objects
     */
    public static ObservableList<GradeFX> convertGradeToGradeFX(ObservableList<Grade> userObservableList) {
        ObservableList<GradeFX> listedClasses = FXCollections.observableArrayList();
        for (Grade g : userObservableList) {
            listedClasses.add(new GradeFX(g));
        }
        return listedClasses;
    }
}
