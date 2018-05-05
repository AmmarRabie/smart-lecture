package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 05/05/2018.
 */

public class QuestionModel {
    private UserModel owner;
    private String id;
    private String text;

    public QuestionModel(String questionId, UserModel owner, String text) {
        this.id = questionId;
        this.text = text;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserModel getOwner() {
        return owner;
    }
}
