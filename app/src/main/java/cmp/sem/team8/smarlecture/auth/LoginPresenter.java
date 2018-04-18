package cmp.sem.team8.smarlecture.auth;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cmp.sem.team8.smarlecture.AppUseCases;
import cmp.sem.team8.smarlecture.IAppUseCases;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.model.UserModel;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class LoginPresenter implements LoginContract.Actions {

    private static final String TAG = "LoginPresenter";

    private LoginContract.Views mView;
    private AppDataSource mDataSource;
    private boolean mForceLogin;
    private boolean mFoundUser;

    public LoginPresenter(AppDataSource dataSource, LoginContract.Views view, boolean forceLogin) {
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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,
                password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail: success");
                            getUserNameAndCallViewSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure",
                                    task.getException());
                            mView.showErrorMessage(task.getException().getMessage());
                        }
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
        AppUseCases.getInstance().forgetPassword(email, new IAppUseCases.OnResetEmailPasswordListener() {
            @Override
            public void OnResetEmailSent() {
                mView.showOnResetPasswordEmailSend();
            }

            @Override
            public void onError() {
                mView.showErrorMessage("no internet connection");
            }
        });
    }


    private void getUserNameAndCallViewSuccess() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mDataSource.getUser(currentUser.getUid(), new AppDataSource.Get<UserModel>() {
            @Override
            public void onDataFetched(UserModel data) {
                mView.showOnSuccess(data.getName());
            }

            @Override
            public void onDataNotAvailable() {
                Log.e(TAG,
                        "onDataNotAvailable: data of the user is not available," +
                                " can't find the id or user doesn't have name");
                mView.showOnSuccess(" "); // show empty user name
            }
        });
    }
}