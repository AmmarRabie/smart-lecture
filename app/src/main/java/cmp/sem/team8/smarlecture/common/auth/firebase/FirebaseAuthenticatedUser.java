package cmp.sem.team8.smarlecture.common.auth.firebase;

import cmp.sem.team8.smarlecture.common.auth.AuthenticatedUser;

/**
 * Created by AmmarRabie on 17/04/2018.
 */

public class FirebaseAuthenticatedUser implements AuthenticatedUser {


    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public void delete(DeleteCallback callback) {

    }

    @Override
    public void changePass(String NewPassword, UpdatePassCallback callback) {

    }
}
