package database.mysql;

import controller.fx.AnswerFormFX;
import model.Answer;
import model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserAnswerLogDAO extends AbstractDAO {
    public UserAnswerLogDAO(DBAccess dBaccess) {
        super(dBaccess);
    }

    public void storeToAnswerLog(List<AnswerFormFX> answers, int quizLogKey, User student)
    {
        for (AnswerFormFX answerFormFX: answers)
        {
            String create_col = "alter table user_answer_log\n" + "add user_quiz_log_id int;";
            String sql = "Insert into user_answer_log(user_id, answer_id, user_quiz_log_id) values(?,?,?) ;";
            try {
                PreparedStatement preparedStatement = getStatementWithKey(sql);
                PreparedStatement createColPreparedStatement = getStatementWithKey(create_col);
                preparedStatement.setInt(1,student.getUserId());
                preparedStatement.setInt(2, answerFormFX.getAnswer().getId());
                preparedStatement.setInt(3, quizLogKey);
                executeInsertPreparedStatement(preparedStatement);
                executeInsertPreparedStatement(createColPreparedStatement);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }
}
