package cmp.sem.team8.smarlecture.profile;


import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

interface ProfileContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        void showOnSuccess();

        void showErrorMessage(String cause);

        void showUserInfo(String name, String email);

        void showOnChangeNameSuccess();
        void showOnChangePassSuccess();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {
        void changePassword(String pass, String confirmPass);
        void changeName(String newName);
    }
}
