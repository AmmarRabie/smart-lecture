package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 22/04/2018.
 */

public class InvitedUserModel {
    private String userId;
    private boolean accept;


    public InvitedUserModel(String userId, boolean accept) {
        this.userId = userId;
        this.accept = accept;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
