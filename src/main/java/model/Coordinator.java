package model;

import java.util.List;

public class Coordinator extends Teacher implements Login {
    public Coordinator(int userId, String firstname, String lastname) {
        super(userId, firstname, lastname);
    }

    public boolean addQuiz(Course c, Quiz q){
        return c.addQuiz(this,q);
    }


}
