package controller.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Course;
import model.Question;
import model.Quiz;
import model.User;

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
     * Convert User objects to UserFX objects
     * @param userObservableList list of User objects
     * @return a observableList UserFx objects
     */
    public static ObservableList<CourseFx> convertCoursetoCourseFX(ObservableList<Course> userObservableList){
        ObservableList<CourseFx> listedCourses = FXCollections.observableArrayList();
        for (Course c:userObservableList) {
            listedCourses.add(new CourseFx(c));
        }
        return listedCourses;
    }
    /**
     * Convert User objects to UserFX objects
     * @param userObservableList list of User objects
     * @return a observableList UserFx objects
     */
    public static ObservableList<QuizFx> convertQuizToQuizFX(ObservableList<Quiz> userObservableList){
        ObservableList<QuizFx> listedQuizes = FXCollections.observableArrayList();
        for (Quiz q:userObservableList) {
            listedQuizes.add(new QuizFx(q));
        }
        return listedQuizes;
    }

    /**
     * Convert User objects to UserFX objects
     * @param list
     * @return
     */
    public static ObservableList<QuestionFx> convertQuestionToQuestionFX(ObservableList<Question> list){
        ObservableList<QuestionFx> listedQuizes = FXCollections.observableArrayList();
        for (Question q: list) {
            listedQuizes.add(new QuestionFx(q));
        }
        return listedQuizes;
    }
}
