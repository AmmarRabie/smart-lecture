package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 28/04/2018.
 */

/**
 * Data representation of one note with no data about owner or who is this note about
 * In other words, this is the base note of the system (id and its text)
 */
public class NoteModel {
    private String id;
    private String text;

    public NoteModel(String id, String text) {
        this.id = id;
        this.text = text;
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
}
