package cmp.sem.team8.smarlecture.common.auth;

/**
 * Created by AmmarRabie on 17/04/2018.
 */

/**
 * This interface represents the user model for authenticated user.
 * Used primary with {@link AuthService}
 */
public interface AuthenticatedUser {

    /**
     * Should return the user id, can't be null
     *
     * @return the id of this authenticated user
     */


    String getUserId();

    String getEmail();

    void delete(DeleteCallback callback);

    void changePass(String oldPassword, String NewPassword, UpdatePassCallback callback);


    interface DeleteCallback {
        void onDeleteSuccess();

        void onError(String error);
    }

    interface UpdatePassCallback {
        void onSuccess();

        void onError(String cause);
    }
}
