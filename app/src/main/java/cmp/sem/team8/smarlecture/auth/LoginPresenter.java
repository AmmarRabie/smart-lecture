package cmp.sem.team8.smarlecture.auth;


import android.util.Log;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.auth.AuthenticatedUser;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class LoginPresenter implements LoginContract.Actions {

    private static final String TAG = "LoginPresenter";

    private AuthService mAuthService;
    private LoginContract.Views mView;
    private AppDataSource mDataSource;
    private boolean mForceLogin;
    private boolean mFoundUser;

    public LoginPresenter(AuthService authService, AppDataSource dataSource, LoginContract.Views view, boolean forceLogin) {
        mAuthService = authService;
        mDataSource = dataSource;
        mView = view;
        mForceLogin = forceLogin;
        mFoundUser = false;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // try to find the current user if not force log in
        if (mForceLogin)
            return;

        // fetch the current user name if he is exist
        AuthenticatedUser currentUser = mAuthService.getCurrentUser();
        if (currentUser != null) {
            mFoundUser = true;
            mView.showProgressIndicator("try finding a user");
            getUserNameAndCallViewSuccess();
        }

    }


    @Override
    public void login(String email, String password) {
//        AuthService.login()
        // sign in

        if (mFoundUser) {
            Exception e = new Exception("log in called although the user is found");
            Log.e(TAG, "login: log in called although the user is found", e);
        }

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            mView.showErrorMessage("filed email and password ca't be empty");
            return;
        }

        mView.showProgressIndicator("logging in...");
        mAuthService.signIn(email,
                password, new AuthService.OnAuthActionComplete<String>() {
                    @Override
                    public void onSuccess(String s) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signIn: success");
                        getUserNameAndCallViewSuccess();
                    }

                    @Override
                    public void onError(String error) {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signIn:failure " + error);
                        mView.showErrorMessage(error);
                    }
                });
    }

    @Override
    public void forgotPassword(String email) {

        if (email == null || email.isEmpty()) {
            mView.showErrorMessage("Email can't be empty");
            return;
        }
        mView.showProgressIndicator("sending...");
        mAuthService.sendPasswordResetEmail(email, new AuthService.OnAuthActionComplete<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mView.showOnResetPasswordEmailSend();
            }

            @Override
            public void onError(String error) {
                mView.showErrorMessage(error);
            }
        });
    }


    private void getUserNameAndCallViewSuccess() {

        AuthenticatedUser currentUser = FirebaseAuthService.getInstance().getCurrentUser();

        mDataSource.getUser(currentUser.getUserId(), new AppDataSource.Get<UserModel>() {
            @Override
            public void onDataFetched(UserModel data) {
                mView.showOnSuccess(data.getName());
            }

            @Override
            public void onDataNotAvailable() {
                Log.e(TAG,
                        "onDataNotAvailable: data of the user is not available," +
                                " can't find the id or user doesn't have name");
                mView.showErrorMessage("There is a problem with your account");
            }

            @Override
            public void onError(String cause) {
                super.onError(cause);
                mView.showErrorMessage(cause);
            }
        });
    }
}