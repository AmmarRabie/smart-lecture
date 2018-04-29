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
        void showOnSuccess();

        void showErrorMessage(String cause);

        void showProgressIndicator();

        void hideProgressIndicator();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter
    {
        void signUp(String name, String email, String password, String confirmPassword, byte[] profileImage);

    }
}
