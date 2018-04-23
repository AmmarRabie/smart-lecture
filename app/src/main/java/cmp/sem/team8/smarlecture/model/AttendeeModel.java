package cmp.sem.team8.smarlecture.model;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public class AttendeeModel {
    private String user;
    private boolean isAttend;

    public AttendeeModel(String user, boolean isAttend) {
        this.user = user;
        this.isAttend = isAttend;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isAttend() {
        return isAttend;
    }

    public void setAttend(boolean attend) {
        isAttend = attend;
    }
}
