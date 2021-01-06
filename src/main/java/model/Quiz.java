package model;

import java.util.List;

public class Quiz {
    private List<Question> questions;
    private double succsesDefinition;

    public Quiz(double succsesDefinition) {
        this.succsesDefinition = succsesDefinition;
    }
    public void addQuestion(Question q){
        questions.add(q);
    }
    public Question showQuestion(int i){
        return questions.get(i);
    }

}
