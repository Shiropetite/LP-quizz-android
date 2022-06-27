package fr.android.quizz.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String statement;
    private String image;
    private boolean answer;
    private String explanation;

    public Question() { }

    public Question(String statement, String image, boolean answer, String explanation) {
        this.statement = statement;
        this.image = image;
        this.answer = answer;
        this.explanation = explanation;
    }

    public String getStatement() {
        return statement;
    }

    public String getImage() {
        return image;
    }

    public boolean isAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }
}
