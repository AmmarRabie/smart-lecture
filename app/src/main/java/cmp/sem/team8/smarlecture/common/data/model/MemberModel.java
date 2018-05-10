package cmp.sem.team8.smarlecture.common.data.model;

import java.util.ArrayList;

/**
 * Created by AmmarRabie on 27/04/2018.
 */

/**
 * Data represents one member in one session providing who is this user ({@link UserModel})
 * This can be called a session member
 */
public class MemberModel extends UserModel {
    private boolean isAttend;
    private ArrayList<NoteModel> notes;

    public MemberModel(String name, String email, String id, boolean isAttend) {
        super(name, email, id);
        this.isAttend = isAttend;
        notes = new ArrayList<>();
    }

    public MemberModel(String name, String email, String id, boolean isAttend, ArrayList<NoteModel> notes) {
        super(name, email, id);
        this.isAttend = isAttend;
        this.notes = notes;
    }

    public MemberModel(UserModel user, boolean isAttend) {
        this(user.getName(), user.getEmail(), user.getId(), isAttend);
    }

    public MemberModel(UserModel user, boolean isAttend, ArrayList<NoteModel> notes) {
        this(user.getName(), user.getEmail(), user.getId(), isAttend);
        this.notes.addAll(notes);
    }

    public boolean isAttend() {
        return isAttend;
    }

    public void setAttend(boolean attend) {
        isAttend = attend;
    }

    public ArrayList<NoteModel> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NoteModel> notes) {
        this.notes = notes;
    }
}
