package cmp.sem.team8.smarlecture.auth;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class LoginPresenter implements LoginContract.Actions {

    private static final String TAG = "LoginPresenter";

    LoginContract.Views mView;
    private boolean mForceLogin;
    private boolean mFoundUser;

    public LoginPresenter(LoginContract.Views view, boolean forceLogin) {
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
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mView.showOnResetPasswordEmailSend();
                        } else {
                            mView.showErrorMessage(task.getException().getMessage());
                        }
                    }
                });

    }


    private void getUserNameAndCallViewSuccess() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference userNameRef =
                FirebaseDatabase.getInstance().
                        getReference("user").child(currentUser.getUid())
                        .child("name");
        userNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                mView.showOnSuccess(userName);

                userNameRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.showErrorMessage(databaseError.getMessage());
            }
        });
    }


}
