package cmp.sem.team8.smarlecture.common.data.firebase;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;
import cmp.sem.team8.smarlecture.model.GroupModel;
import cmp.sem.team8.smarlecture.model.SessionModel;
import cmp.sem.team8.smarlecture.model.UserModel;

/**
 * This is the implementation of the AppDataSource using firebase database
 * <p>
 */
public class FirebaseRepository extends FirebaseRepoHelper {

    private static FirebaseRepository INSTANCE = null;
    private ListenersList listeners;

    public static FirebaseRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseRepository();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().getReference().keepSynced(true);
        }
        return INSTANCE;
    }

    @Override
    public void getUser(final String userId, final Get<UserModel> callback) {
        final DatabaseReference userRef =
                FirebaseDatabase.getInstance().
                        getReference(UserEntry.KEY_THIS).child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                String userName = dataSnapshot.child(UserEntry.KEY_NAME).getValue(String.class);
                String userEmail = dataSnapshot.child(UserEntry.KEY_EMAIL).getValue(String.class);
                UserModel user = new UserModel(userName, userEmail, userId);
                callback.onDataFetched(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void insertUser(final UserModel userModel, final Insert<String> callback) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference(UserEntry.KEY_THIS);
        final DatabaseReference thisUserReference = usersReference.child(userModel.getIdentity());
        thisUserReference.child(UserEntry.KEY_NAME).setValue(userModel.getName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            callback.onError("Can't insert the user");
                            return;
                        }
                        thisUserReference.child(UserEntry.KEY_EMAIL).setValue(userModel.getEmail())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            callback.onError("Can't insert the user");
                                            return;
                                        }
                                        callback.onDataInserted("sba7elfoll");
                                    }
                                });
                    }
                });
    }

    @Override
    public void updateUserName(String userId, String newName, final Update callback) {
        final DatabaseReference thisUserRef = FirebaseDatabase.getInstance()
                .getReference(UserEntry.KEY_THIS)
                .child(userId);


        thisUserRef.child(UserEntry.KEY_NAME).setValue(newName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onUpdateSuccess();
                        } else {
                            if (task.getException() != null)
                                callback.onError(task.getException().getMessage());
                            else callback.onError("Can't update user name");
                        }
                    }
                });
    }

    @Override
    public void getGroupById(String groupId, Get<GroupModel> callback) {

    }

    @Override
    public void getGroupSessions(String groupId, Get<ArrayList<SessionModel>> callback) {

    }

    @Override
    public void deleteGroupById(String groupId, Delete callback) {

    }

    @Override
    public void updateGroup(GroupModel updatingValues, Update callback) {

    }

    @Override
    public void updateGroupById(String groupId, String newGroupName, Update callback) {

    }

    @Override
    public void deleteNamesList(String groupId, Delete callback) {

    }

    @Override
    public void deleteNameOfNamesList(String groupId, String nameId, Delete callback) {

    }

    @Override
    public void updateNameOfNamesList(String groupId, String nameId, String newName, Update callback) {

    }

    @Override
    public void insertNameInNamesList(String groupId, String userId, Insert<String> callback) {

    }

    @Override
    public void getSessionById(String sessionId, Get<SessionModel> callback) {

    }

    @Override
    public void getSessionStatus(String sessionId, Get<SessionStatus> callback) {

    }

    @Override
    public void getAttendanceStatus(String sessionId, Get<AttendanceStatus> callback) {

    }

    @Override
    public void updateSession(SessionModel sessionModel, Update callback) {

    }

    @Override
    public void updateSessionStatus(String sessionId, SessionStatus status, Update callback) {

    }

    @Override
    public void updateAttendanceStatus(String sessionId, AttendanceStatus status, Update callback) {

    }

    @Override
    public void updateSessionSecret(String sessionId, String secret, Update callback) {

    }


    @Override
    public void listenUser(String userId, final Listen<UserModel> callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);
        final ValueEventWithRef listenUserEvent = new ValueEventWithRef(userRef);
        final ValueEventListener valueEventListener = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (isDead(callback)) return;
                if (true /*validate your conditions that data received success fully*/) {
                    callback.onDataReceived(new UserModel("", "", ""));
                    callback.increment();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                forget(callback);
            }
        });
        listenUserEvent.setListener(valueEventListener);
        listeners.add(listenUserEvent);
    }

}
