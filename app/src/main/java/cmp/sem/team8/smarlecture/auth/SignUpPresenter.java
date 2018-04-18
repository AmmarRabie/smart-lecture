package cmp.sem.team8.smarlecture.auth;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class SignUpPresenter implements SignUpContract.Actions {


    private SignUpContract.Views mView;
    private boolean isInTask;

    private OnCompleteListener<Void> deleteTaskListener;

    private OnCompleteListener loggingListener;


    public SignUpPresenter(SignUpContract.Views view) {
        mView = view;
        isInTask = false;

        deleteTaskListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> deleteUserTask) {

                if (deleteUserTask.isSuccessful()) {
                    Log.e("SignUpPresenter: ", "the user can't write his data in the " +
                            "database although he is authenticated");
                    mView.showErrorMessage("can't sign up right now");
                    endTask();
                } else {
                    // else the user is signed in although we can't insert him at the database, throw exception
//                    throw new RuntimeException("The user is authenticated and not inserted");
                    Log.wtf("SignUpPresenter: ", "The user is authenticated and not inserted");
                }
            }
        };

        loggingListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull final Task writeTask) {
                if (writeTask.isSuccessful()) {
                    mView.showOnSuccess();
                    endTask();
                } else {

                    // [start try to delete the user from authenticated users]
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(deleteTaskListener);
                    // [end try to delete the user from authenticated users]
                }
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


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,
                password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUpPresenter: ", "createUserWithEmailAndPassword:success");
                            insertUser(name, email, task.getResult().getUser().getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUpPresenter: ", "createUserWithEmailAndPassword:failure",
                                    task.getException());
                            mView.showErrorMessage(task.getException().getMessage());
                            endTask();
                        }
                    }
                });
    }

    private void insertUser(String name, String email, String id) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference(UserEntry.KEY_THIS);
        DatabaseReference thisUserReference = usersReference.child(id);
        thisUserReference.child(UserEntry.KEY_NAME).setValue(name).addOnCompleteListener(loggingListener);
        thisUserReference.child(UserEntry.KEY_NAME).setValue(email).addOnCompleteListener(loggingListener);
    }
}
