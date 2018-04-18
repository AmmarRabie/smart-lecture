package cmp.sem.team8.smarlecture.auth;


import android.util.Log;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.auth.AuthenticatedUser;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.model.UserModel;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class SignUpPresenter implements SignUpContract.Actions {


    private AuthService mAuthService;
    private AppDataSource mDataSource;
    private SignUpContract.Views mView;
    private boolean isInTask;

    private AuthenticatedUser.DeleteCallback deleteTaskListener;


    public SignUpPresenter(AuthService authService, AppDataSource dataSource, SignUpContract.Views view) {
        mDataSource = dataSource;
        mAuthService = authService;
        mView = view;
        isInTask = false;

        deleteTaskListener = new AuthenticatedUser.DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                Log.e("SignUpPresenter: ", "the user can't write his data in the " +
                        "database although he is authenticated");
                mView.showErrorMessage("can't sign up right now");
                endTask();

            }

            @Override
            public void onError(String error) {
                // else the user is signed in although we can't insert him at the database, throw exception
//                    throw new RuntimeException("The user is authenticated and not inserted");
                Log.wtf("SignUpPresenter: ", "The user is authenticated and not inserted");
            }
        };
        mView.setPresenter(this);
    }

    private boolean startTask() {
        if (isInTask)
            return false;

        isInTask = true;
        mView.showProgressIndicator();
        return true;
    }

    private boolean endTask() {
        if (!isInTask)
            return false;

        isInTask = false;
        mView.hideProgressIndicator();
        return true;
    }

    @Override
    public void start() {
    }

    @Override
    public void signUp(final String name, final String email,
                       String password, String confirmPassword) {


        if (!startTask())
            return;

        // sign up

        if (email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                name == null || name.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            mView.showErrorMessage("all fields can't be empty");
            endTask();
            return;
        }
        if (!password.equals(confirmPassword)) {
            mView.showErrorMessage("two passwords are different");
            endTask();
            return;
        }

        mAuthService.signUp(email, password, new AuthService.OnAuthActionComplete<String>() {
            @Override
            public void onSuccess(String userId) {
                Log.d("SignUpPresenter: ", "createUserWithEmailAndPassword:success");
                insertUser(name, email, userId);
            }

            @Override
            public void onError(String error) {
                // If sign in fails, display a message to the user.
                Log.w("SignUpPresenter: ", "createUserWithEmailAndPassword:failure");
                mView.showErrorMessage(error);
                endTask();
            }
        });
    }

    private void insertUser(String name, String email, String id) {
        UserModel newUserModel = new UserModel(name, email, id);
        mDataSource.insertUser(newUserModel, new AppDataSource.Insert<String>() {
            @Override
            public void onDataInserted(String feedback) {
                mView.showOnSuccess();
                endTask();
            }

            @Override
            public void onError(String cause) {
                mAuthService.getCurrentUser().delete(deleteTaskListener);
            }
        });
    }
}
