package cmp.sem.team8.smarlecture.auth;


import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

interface LoginContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        /**
         *  show message when a task succssed
         * @param userName
         */
        void showOnSuccess(String userName);
        /**
         *  show message when a error occured
         * @param cause
         */
        void showErrorMessage(String cause);

        /**
         *  show special kind of message when resetPasswordEmailSend
         */
        void showOnResetPasswordEmailSend();

        /**
         *  show progress indicator of a task
         * @param progressWorkMessage
         */
        void showProgressIndicator(String progressWorkMessage);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {
        /**
         *  logic of login functionality
         * @param email of user
         * @param password of user
         */
        void login(String email, String password);

        /**
         *  logic of forget password functionality
         * @param email of user
         */
        void forgotPassword(String email);
    }
}
