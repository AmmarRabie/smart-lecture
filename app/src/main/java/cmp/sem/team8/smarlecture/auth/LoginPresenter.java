package cmp.sem.team8.smarlecture.auth;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class LoginPresenter implements LoginContract.Actions {


    LoginContract.Views mView;

    public LoginPresenter(LoginContract.Views view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // try to find the current user
    }


    @Override
    public void login(String email, String password) {
//        AuthService.login()
        // sign in

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            mView.showErrorMessage("filed email and password ca't be empty");
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,
                password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("dsjksdk", "signInWithEmail:success");
                            mView.showOnSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("sdkjgbsdjk", "signInWithEmail:failure",
                                    task.getException());
                            mView.showErrorMessage(task.getException().getMessage());
                        }
                    }
                });
    }
}
