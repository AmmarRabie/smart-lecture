package cmp.sem.team8.smarlecture.common.auth;

import android.support.annotation.Nullable;

/**
 * Created by AmmarRabie on 17/04/2018.
 */

public interface AuthService{
    void signIn(String name, String pass, OnAuthActionComplete callback);

    interface OnAuthActionComplete
    {
        void onSuccess();
        void onError(String error);
    }

    /**
     * Authenticate a new user into the system, this function authenticate the user only and
     * doesn't insert him into the database system. The function validate the inputs
     * @param name the name of the new user
     * @param email the email of the new user
     * @param pass the password of the new user
     * @param callback the callback called in cases of success or a failure
     */
    void signUp(String name, String email, String pass, OnAuthActionComplete callback);

    /**
     * Sign out the current user, if there is not a user then it do nothing
     */
    void signOut();

    void sendPasswordResetEmail(String email, OnAuthActionComplete callback);

    AuthenticatedUser getCurrentUser();

}
