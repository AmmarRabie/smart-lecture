package cmp.sem.team8.smarlecture.common.data.model;

import java.util.ArrayList;

/**
 * Created by AmmarRabie on 27/04/2018.
 */

public class AttendeeModel extends UserModel {
    private boolean isAttend;

    public AttendeeModel(String name, String email, String id,boolean isAttend) {
        super(name, email, id);
        this.isAttend = isAttend;
    }

    public AttendeeModel(UserModel user, boolean isAttend) {
        this(user.getName(),user.getEmail(),user.getId(),isAttend);
    }

    public void setAttend(boolean attend) {
        isAttend = attend;
    }

    public boolean isAttend() {
        return isAttend;
    }


}
