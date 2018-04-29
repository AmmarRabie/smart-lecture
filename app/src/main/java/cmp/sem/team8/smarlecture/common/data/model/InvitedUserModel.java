package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 22/04/2018.
 */

public class InvitedUserModel {
    private UserModel user;
    private boolean accept;


    public InvitedUserModel(UserModel user, boolean accept) {
        this.user = user;
        this.accept = accept;
    }

    public UserModel getUser() {
        return user;
    }

    public boolean isAccept() {
        return accept;
    }

}
