package cmp.sem.team8.smarlecture.common.auth.firebase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.auth.AuthenticatedUser;

/**
 * Created by AmmarRabie on 17/04/2018.
 */

/**
 * The implementation of the AuthService using FirebaseAuth.
 * This class is also considered an adapter for the {@link FirebaseAuth}
 */
public class FirebaseAuthService implements AuthService {

    private static FirebaseAuthService INSTANCE;

    private AuthenticatedUser mCurrentUser = null;

    private FirebaseAuthService() {
    }


    public static FirebaseAuthService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new FirebaseAuthService();
        return INSTANCE;
    }

    @Override
    public void signIn(String email, final String pass, final OnAuthActionComplete<String> callback) {
        Task<AuthResult> task = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass);
        if (callback == null) return;
        task.addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(getCurrentUser().getUserId());
                            updateUser();
                            return;
                        }
                        if (task.getException() != null)
                            callback.onError(task.getException().getMessage());
                        else callback.onError("Can't sign in");
                    }
                });
    }

    @Override
    public void signUp(String email, final String pass, final OnAuthActionComplete<String> callback) {
        Task<AuthResult> task = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass);
        if (callback == null) return;
        task.addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(getCurrentUser().getUserId());
                            updateUser();
                            return;
                        }
                        if (task.getException() != null)
                            callback.onError(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        updateUser();
    }


    @Override
    public void sendPasswordResetEmail(String email, final OnAuthActionComplete<Void> callback) {
        Task<Void> task = FirebaseAuth.getInstance().sendPasswordResetEmail(email);
        if (callback == null) return;

        task.addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(null);
                            return;
                        }
                        if (task.getException() != null)
                            callback.onError(task.getException().getMessage());
                    }
                }
        );
    }


    @Override
    public AuthenticatedUser getCurrentUser() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            mCurrentUser = null;
            return null;
        }
        if (mCurrentUser != null) return mCurrentUser;
        mCurrentUser = new FirebaseAuthenticatedUser(currentUser);
        return mCurrentUser;
    }

    private void updateUser() {
        // force the getCurrentUser to create a new user
        mCurrentUser = null;
    }
}
