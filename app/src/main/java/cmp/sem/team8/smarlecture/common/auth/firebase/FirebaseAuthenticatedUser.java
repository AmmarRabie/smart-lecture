package cmp.sem.team8.smarlecture.common.auth.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.auth.AuthenticatedUser;

/**
 * Created by AmmarRabie on 17/04/2018.
 */

/**
 * represents the firebase authenticated user.
 * also considered as adapter for {@link FirebaseUser}
 */
public class FirebaseAuthenticatedUser implements AuthenticatedUser {
    private static final String TAG = "FirebaseAuthenticatedUs";

    private FirebaseUser mCurrUser;


    FirebaseAuthenticatedUser(FirebaseUser mCurrUser) {
        this.mCurrUser = mCurrUser;
    }

    @Override
    public String getUserId() {
        return mCurrUser.getUid();
    }


    @Override
    public String getEmail() {
        return mCurrUser.getEmail();
    }

    @Override
    public void delete(final DeleteCallback callback) {
        FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onDeleteSuccess();
                } else if (task.getException() != null)
                    callback.onError(task.getException().getMessage());
            }
        });
    }

    @Override
    public void changePass(String oldPass, final String newPass, final UpdatePassCallback callback) {
        // try to sign in the user first so that get fresh token
        FirebaseAuthService.getInstance().signIn(getEmail(), oldPass, new AuthService.OnAuthActionComplete<String>() {
            @Override
            public void onSuccess(String s) {
                // password is correct, request changing the password
                mCurrUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else if (task.getException() != null) // issues with new password
                            callback.onError(task.getException().getMessage());
                    }
                });
            }

            @Override
            public void onError(String error) {
                // can't sign in, password is incorrect or there is no internet connection
                callback.onError("old password is incorrect or poor internet connection");
            }
        });
    }

}
