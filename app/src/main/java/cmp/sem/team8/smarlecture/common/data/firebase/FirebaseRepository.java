package cmp.sem.team8.smarlecture.common.data.firebase;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

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
    public void insertUser(final UserModel userModel, final Insert<Void> callback) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference(UserEntry.KEY_THIS);
        final DatabaseReference thisUserReference = usersReference.child(userModel.getId());
        thisUserReference.child(UserEntry.KEY_NAME).setValue(userModel.getName())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    private OnCompleteListener<Void> onEmailInserted = new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                callback.onError("Can't insert the user");
                                return;
                            }
                            callback.onDataInserted(null);
                        }
                    };

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            callback.onError("Can't insert the user");
                            return;
                        }
                        thisUserReference.child(UserEntry.KEY_EMAIL).setValue(userModel.getEmail())
                                .addOnCompleteListener(onEmailInserted);
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


    @Override
    public void getSessionsOfGroup(String groupId, Get<ArrayList<SessionModel>> callback) {

    }

    @Override
    public void inviteUserToGroup(String email, final String groupId, final Insert<UserModel> callback) {
        getReference(UserEntry.KEY_THIS)
                .orderByChild(UserEntry.KEY_EMAIL)
                .equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot usersQuerySnapshot) {
                        if (!usersQuerySnapshot.exists()) {
                            callback.onError("This email doesn't exists");
                            return;
                        }
                        // get the user
                        final DataSnapshot userSnapshot = usersQuerySnapshot.getChildren().iterator().next();
                        final String userId = userSnapshot.getKey();

                        // get group snapshot
                        getGroupRef(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot groupSnapshot) {
                                if (!groupSnapshot.exists()) {
                                    callback.onError("This group doesn't exist");
                                    return;
                                }
                                GroupModel groupModel = FirebaseSerializer.serializeGroup(groupSnapshot);
                                if (groupModel.getOwnerId().equals(userId)) {
                                    callback.onError("You can't add yourself");
                                    return;
                                }
                                for (InvitedUserModel invitedUserModel : groupModel.getUsersList()) {
                                    if (invitedUserModel.getUserId().equals(userId)) {
                                        callback.onError("This user is already exists");
                                        return;
                                    }
                                }
                                continueWithUserSnapshot(userSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onError(databaseError.getMessage());
                            }
                        });
                    }

                    private void continueWithUserSnapshot(DataSnapshot userSnapshot) {

                        // insert the user into fetched group
                        getGroupRef(groupId).child(GroupEntry.KEY_NAMES_LIST)
                                .child(userSnapshot.getKey()).setValue(false); // false mean not accepted yet

                        // insert the group to the requested groups of the user
                        getUserRef(userSnapshot.getKey()).child(UserEntry.KEY_INVITATIONS)
                                .child(groupId)
                                .setValue(false);  // false mean not accepted (following) yet

                        callback.onDataInserted(FirebaseSerializer.serializeUser(userSnapshot));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.getMessage());
                    }
                });
    }


    @Override
    public void acceptFollowingGroup(String userId, String groupId, Update callback) {
        DatabaseReference userRequestedGroups =
                getReference(UserEntry.KEY_THIS).child(userId).child(UserEntry.KEY_INVITATIONS);
        userRequestedGroups
                .child(groupId).setValue(true);
        getReference(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_NAMES_LIST)
                .child(userId).setValue(true);
        callback.onUpdateSuccess();
    }

    @Override
    public void refuseFollowingGroup(String userId, String groupId, final Update callback) {
        DatabaseReference userRequestedGroups =
                getReference(UserEntry.KEY_THIS).child(userId).child(UserEntry.KEY_INVITATIONS);
        userRequestedGroups
                .child(groupId).removeValue();
        getReference(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_NAMES_LIST)
                .child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onUpdateSuccess();
            }
        });
    }

    @Override
    public void getSessionsForUser(String userId, final Get<ArrayList<SessionForUserModel>> callback, final boolean withClosed, final boolean withOpened, final boolean withNotActive) {
        getSessionsForUser2(userId, callback, withClosed, withOpened, withNotActive);
        /*final ArrayList<SessionForUserModel> resultList = new ArrayList<>();

        // search on only groups he is actually following (exclude invitation)
        Query followedGroups = getReference(UserEntry.KEY_THIS).child(userId)
                .child(UserEntry.KEY_INVITATIONS).orderByValue().equalTo(true);

        // loop over followed groups and for each group id loop over its sessions
        followedGroups.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot groupsSnapshot) {
                for (final DataSnapshot oneGroupSnapshot : groupsSnapshot.getChildren()) {
                    final String groupId = oneGroupSnapshot.getKey();
                    getGroupRef(groupId).child(GroupEntry.KEY_SESSIONS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot sessionsSnapshot) {
                            for (final DataSnapshot oneSessionSnapshot : sessionsSnapshot.getChildren()) {
                                SessionModel sessionModel = FirebaseSerializer.serializeSession(oneSessionSnapshot);
                                if (sessionModel == null) {
                                    callback.onDataNotAvailable();
                                    return;
                                }
                                if (!isvalid(sessionModel.getSessionStatus())) continue;
                                String groupOwnerId = oneGroupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class);
                                getUserRef(groupOwnerId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot userSnapshot) {
                                        resultList.add(FirebaseSerializer.serializeSessionForUser(userSnapshot, oneSessionSnapshot, oneGroupSnapshot));
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        callback.onError(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        private boolean isvalid(SessionStatus sessionStatus) {
                            switch (sessionStatus) {
                                case OPEN:
                                    if (withOpened) return true;
                                case CLOSED:
                                    if (withClosed) return true;
                                case NOT_ACTIVATED:
                                    if (withNotActive) return true;
                            }
                            return false;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.getMessage());
                        }
                    });
                }
                callback.onDataFetched(resultList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }

        });*/
    }


    public void getSessionsForUser2(String userId, final Get<ArrayList<SessionForUserModel>> callback, final boolean withClosed, final boolean withOpened, final boolean withNotActive) {
        final ArrayList<SessionForUserModel> resultList = new ArrayList<>();

        // search on only groups he is actually following (exclude invitation)
        Query followedGroups = getReference(UserEntry.KEY_THIS).child(userId)
                .child(UserEntry.KEY_INVITATIONS).orderByValue().equalTo(true);

        // loop over followed groups and for each group id loop over its sessions
        followedGroups.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot groupsKeysSnapshot) {
                if (!groupsKeysSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                ArrayList<String> groupsKeys = FirebaseSerializer.getKeys(groupsKeysSnapshot);
                for (final String oneGroupKey : groupsKeys) {

                    getGroupRef(oneGroupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot oneGroupSnapshot) {
                            getUserRef(oneGroupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot userSnapshot) {
                                    final ArrayList<String> sessionsKeys = FirebaseSerializer.getKeys(oneGroupSnapshot.child(GroupEntry.KEY_SESSIONS));
                                    for (final String oneSessionKey : sessionsKeys) {
                                        getSessionRef(oneSessionKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot oneSessionSnapshot) {
//                                                if (!isValid(SessionStatus.fromString(oneSessionSnapshot.child(FirebaseContract.SessionEntry.KEY_SESSION_STATUS).getValue(String.class)))) return;
                                                resultList.add(FirebaseSerializer
                                                        .serializeSessionForUser
                                                                (userSnapshot, oneSessionSnapshot, oneGroupSnapshot));
                                                callback.onDataFetched(resultList);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                callback.onError(databaseError.getMessage());
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    callback.onError(databaseError.getMessage());
                                }
                            });
                        }

                        private boolean isValid(SessionStatus sessionStatus) {
                            switch (sessionStatus) {
                                case OPEN:
                                    if (withOpened) return true;
                                case CLOSED:
                                    if (withClosed) return true;
                                case NOT_ACTIVATED:
                                    if (withNotActive) return true;
                            }
                            return false;
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.getMessage());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }

        });
    }


    @Override
    public void getSessionsForUser(String userId, Get<ArrayList<SessionForUserModel>> callback) {
        getSessionsForUser(userId, callback, true, true, true);
    }

    @Override
    public void getUsersListOfGroup(String groupId, Get<ArrayList<UserModel>> callback) {

    }

}
