package model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private List<Question> questions = new ArrayList<>();
    private double succsesDefinition;
    private String name;
    private int idquiz;
    private int idcourse;

    public Quiz(double succsesDefinition, String name) {
        this.succsesDefinition = succsesDefinition;
        this.name = name;
    }

    public void addQuestion(Question q){
        questions.add(q);
    }
    public Question showQuestion(int i){
        return questions.get(i);
    }
    public int getTotal(){
        return questions.size();
    }

}
