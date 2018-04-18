package cmp.sem.team8.smarlecture.common.auth.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    public void changePass(String newPass, final UpdatePassCallback callback) {
        mCurrUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                } else if (task.getException() != null)
                    callback.onError(task.getException().getMessage());
            }
        });
    }

}
