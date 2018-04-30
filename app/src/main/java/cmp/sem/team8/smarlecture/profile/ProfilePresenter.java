package cmp.sem.team8.smarlecture.profile;


import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.auth.AuthenticatedUser;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class ProfilePresenter implements ProfileContract.Actions {

    private static final String TAG = "ProfilePresenter";
    private AppDataSource mDataSource;

    private AuthService mAuthService;
    private ProfileContract.Views mView;
    private AuthenticatedUser mCurrentUser;

    public ProfilePresenter(AuthService authService, AppDataSource dataSource, ProfileContract.Views view) {
        this.mAuthService = authService;
        mDataSource = dataSource;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // try to find the current user
        mCurrentUser = mAuthService.getCurrentUser();
        if (mCurrentUser == null) {
            mView.showErrorMessage("can't find the current user, try to re-login");
            return;
        }
        getUserInfoAndUpdateView();
    }


    @Override
    public void changePassword(String pass, String confirmPass) {
        if (mCurrentUser == null) {
            mView.showErrorMessage("You are not logged in, try to re-login");
            return;
        }
        if (pass == null || pass.isEmpty() ||
                confirmPass == null || confirmPass.isEmpty()) {
            mView.showErrorMessage("can't be empty");
            return;
        }
        if (!pass.equals(confirmPass)) {
            mView.showErrorMessage("two password are different");
            return;
        }
        mCurrentUser.changePass(pass, new AuthenticatedUser.UpdatePassCallback() {
            @Override
            public void onSuccess() {
                mView.showOnChangePassSuccess();
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
            }
        });
    }


    @Override
    public void changeName(final String newName) {
        if (mCurrentUser == null) {
            mView.showErrorMessage("You are not logged in, try to re-login");
            return;
        }
        if (newName == null || newName.isEmpty()) {
            mView.showErrorMessage("your name can't be empty");
            return;
        }

        mDataSource.updateUserName(mCurrentUser.getUserId(), newName, new AppDataSource.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.showOnChangeNameSuccess();
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
            }
        });
    }

    @Override
    public void signOut() {
        if (mCurrentUser == null) {
            mView.showErrorMessage("You are not logged in, try to re-login");
            return;
        }
        mAuthService.signOut();
        mView.showOnSignOutSuccess();
    }

    @Override
    public void changeProfileImage(byte[] newImageBytes) {
        mDataSource.updateUserProfileImage(mCurrentUser.getUserId(), newImageBytes, null);
    }

    private void getUserInfoAndUpdateView() {
        mView.showProgressIndicator("Loading your profile...");
        FirebaseRepository.getInstance()
                .getUser(mCurrentUser.getUserId(), new AppDataSource.Get<UserModel>() {
                    @Override
                    public void onDataFetched(UserModel user) {
                        mView.showUserInfo(user);
                        mView.hideProgressIndicator();
                    }
                });
    }
}
