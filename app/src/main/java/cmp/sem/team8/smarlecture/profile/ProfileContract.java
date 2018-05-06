package cmp.sem.team8.smarlecture.profile;


import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

interface ProfileContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        void showErrorMessage(String cause);

        void showUserInfo(UserModel user);

        void showOnChangeNameSuccess();

        void showOnChangePassSuccess();

        void showOnSignOutSuccess();

        void showProgressIndicator(String progressWorkMessage);

        void hideProgressIndicator();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {
        void changePassword(String oldPass, String pass, String confirmPass);

        void changeName(String newName);

        void signOut();

        void changeProfileImage(byte[] newImageBytes);
    }
}
