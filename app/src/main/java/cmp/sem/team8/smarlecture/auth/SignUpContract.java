package cmp.sem.team8.smarlecture.auth;


import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

interface SignUpContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions>
    {
        /**
         *  show message when sign up succssed
         */
        void showOnSuccess();

        /**
         *  show message when a error occured
         * @param cause
         */
        void showErrorMessage(String cause);
        /**
         *  show progress indicator of a task
         * @param
         */
        void showProgressIndicator();
        /**
         *  hide progress indicator of a task
         * @param
         */
        void hideProgressIndicator();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter
    {
        /**
         *  sign up functionality
         * @param name
         * @param email
         * @param password
         * @param confirmPassword
         * @param profileImage
         */
        void signUp(String name, String email, String password, String confirmPassword, byte[] profileImage);

    }
}
