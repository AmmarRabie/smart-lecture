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

    /**
     * get the email of the user
     *
     * @return the email of the authenticated user, can't be null
     */
    String getEmail();

    /**
     * Delete this user from system users. After calling this with success, this user will not be
     * able to sign in with his email again. He should sign up again
     *
     * @param callback whether or not the deletion is success
     */
    void delete(DeleteCallback callback);

    /**
     * change the password of this user. This method doesn't require fresh tokens as it sign in
     * the user again getting a fresh token and then change his password so it need the old pass
     *
     * @param oldPassword the current password of this user that wanted to change.
     *                    if it is incorrect the onError method
     *                    will be called
     * @param NewPassword the new password for this email. Should be more that 6 chars
     * @param callback    whether or not the change password success
     */
    void changePass(String oldPassword, String NewPassword, UpdatePassCallback callback);

    /**
     * callback for the caller of {@link #delete(DeleteCallback)}
     */
    interface DeleteCallback {
        /**
         * fired when the deletion of the user is successfully completed
         */
        void onDeleteSuccess();

        /**
         * If there is an error while deletion. this method will be fired
         *
         * @param error Readable message of the error occurred
         */
        void onError(String error);
    }

    /**
     * callback for the caller of {@link #changePass(String, String, UpdatePassCallback)}
     */
    interface UpdatePassCallback {
        /**
         * fired when the deletion of the user is successfully completed
         */
        void onSuccess();

        /**
         * When there is an error while updating the user password
         *
         * @param cause Readable message of the error occurred. May be connection error,
         *              old password is not correct or new pass is not valid.
         * @see #changePass(String, String, UpdatePassCallback)
         */
        void onError(String cause);
    }
}
