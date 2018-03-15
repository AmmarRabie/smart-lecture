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

        void showOnSuccess();

        void showErrorMessage(String cause);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {
        void login(String email, String password);
    }
}
