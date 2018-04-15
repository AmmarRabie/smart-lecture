package cmp.sem.team8.smarlecture.profile;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmp.sem.team8.smarlecture.common.data.FirebaseContract.*;

/**
 * Created by AmmarRabie on 08/03/2018.
 */

class ProfilePresenter implements ProfileContract.Actions {

    private static final String TAG = "ProfilePresenter";

    private ProfileContract.Views mView;
    private FirebaseUser mCurrentUser;

    public ProfilePresenter(ProfileContract.Views view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // try to find the current user

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser == null) {
            mView.showErrorMessage("can't find the current user, try to re-login");
            return;
        }

        getUserInfoAndUpdateView();
//        mView.showUserInfo(mCurrentUser.getDisplayName(), mCurrentUser.getEmail());
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

        mCurrentUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mView.showOnChangePassSuccess();
                } else {
                    mView.showErrorMessage(task.getException().getMessage());
                }
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


        final DatabaseReference thisUserRef = FirebaseDatabase.getInstance().getReference()
                .child(UserEntry.KEY_THIS).child(mCurrentUser.getUid());


        thisUserRef.child(UserEntry.KEY_NAME).setValue(newName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mView.showOnChangeNameSuccess();
                        } else {
                            if (task.getException() != null)
                                mView.showErrorMessage(task.getException().getMessage());
                        }
                    }
                });

    }

    @Override
    public void signOut() {
        if (mCurrentUser == null) {
            mView.showErrorMessage("You are not logged in, try to re-login");
            return;
        }
        FirebaseAuth.getInstance().signOut();
        mView.showOnSignOutSuccess();
    }


    private void getUserInfoAndUpdateView() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference userRef =
                FirebaseDatabase.getInstance().getReference(UserEntry.KEY_THIS).child(currentUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child(UserEntry.KEY_NAME).getValue(String.class);
                String userEmail = dataSnapshot.child(UserEntry.KEY_EMAIL).getValue(String.class);
                mView.showUserInfo(userName, userEmail);

                // remove the value after updating the view
                // this because if i change the name it will trigger the same time edittext view
                // is trying to change its value
                userRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.showErrorMessage(databaseError.getMessage());
                userRef.removeEventListener(this);
            }
        });
    }
}
