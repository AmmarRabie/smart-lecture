package cmp.sem.team8.smarlecture.common.data.model;

/**
 * Created by AmmarRabie on 22/04/2018.
 */

public class InvitedUserModel extends UserModel{
    private boolean accept;


    public InvitedUserModel(UserModel user, boolean accept) {
        super(user.getName(),user.getEmail(),user.getId());
        this.accept = accept;
    }

    public boolean isAccept() {
        return accept;
    }

}
