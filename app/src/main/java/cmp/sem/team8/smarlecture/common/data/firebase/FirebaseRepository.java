package cmp.sem.team8.smarlecture.common.data.firebase;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
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
        getUserRef(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                if (!userSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                UserModel userModel = FirebaseSerializer.serializeUser(userSnapshot);
                callback.onDataFetched(userModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void insertUser(final UserModel userModel, final Insert<Void> callback) {
        HashMap<String, String> values = new HashMap<>();
        values.put(UserEntry.KEY_EMAIL, userModel.getEmail());
        values.put(UserEntry.KEY_NAME, userModel.getName());
        String profileImage = userModel.getProfileImage();
        if (profileImage != null)
            values.put(UserEntry.KEY_PROFILE_IMAGE, profileImage);

        getUserRef(userModel.getId()).setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    callback.onError("Can't insert the user");
                    return;
                }
                callback.onDataInserted(null);
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
                                if (groupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class).equals(userId)) {
                                    callback.onError("You can't add yourself");
                                    return;
                                }
                                if (groupSnapshot.child(GroupEntry.KEY_NAMES_LIST).child(userId).exists()) {
                                    callback.onError("This user is already exists");
                                    return;
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
    public void getSessionsForUser(String userId, final Get<SessionForUserModel> callback, final boolean withClosed, final boolean withOpened, final boolean withNotActive) {
//        final ArrayList<SessionForUserModel> resultList = new ArrayList<>();

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
                                                if (!isValid(SessionStatus.fromString(oneSessionSnapshot.child(FirebaseContract.SessionEntry.KEY_SESSION_STATUS).getValue(String.class))))
                                                    return;
                                                SessionForUserModel sessionForUserModel = FirebaseSerializer
                                                        .serializeSessionForUser
                                                                (userSnapshot, oneSessionSnapshot, oneGroupSnapshot);
//                                                resultList.add(sessionForUserModel);
                                                callback.onDataFetched(sessionForUserModel);
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
    public void getSessionsForUser(String userId, Get<SessionForUserModel> callback) {
        getSessionsForUser(userId, callback, true, true, true);
    }

    @Override
    public void getGroupInvitationsForUser(final String userId, final Get<GroupInvitationModel> callback) {
        Query groupInvitationsKeys =
                getUserRef(userId).child(UserEntry.KEY_INVITATIONS).orderByValue().equalTo(false);

        groupInvitationsKeys.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot invitationsKeysSnapshot) {
                if (!invitationsKeysSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                for (String groupKey : FirebaseSerializer.getKeys(invitationsKeysSnapshot)) {
                    getGroupRef(groupKey).addListenerForSingleValueEvent(new GroupValueEvent());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }

            class GroupValueEvent implements ValueEventListener {
                @Override
                public void onDataChange(DataSnapshot groupSnapshot) {
                    String ownerId = groupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class);
                    getUserRef(ownerId).addListenerForSingleValueEvent(new UserValueEvent(groupSnapshot));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onError(databaseError.getMessage());
                }
            }

            class UserValueEvent implements ValueEventListener {
                private DataSnapshot groupSnapshot;

                UserValueEvent(DataSnapshot groupSnapshot) {
                    this.groupSnapshot = groupSnapshot;
                }

                @Override
                public void onDataChange(DataSnapshot userSnapshot) {
                    callback.onDataFetched(
                            FirebaseSerializer.serializeGroupInvitation(groupSnapshot, userSnapshot));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onError(databaseError.getMessage());
                }
            }
        });
    }


    @Override
    public void getUsersListOfGroup(String groupId, final Get<ArrayList<InvitedUserModel>> callback) {
        final ArrayList<InvitedUserModel> result = new ArrayList<>();
        getGroupRef(groupId).child(GroupEntry.KEY_NAMES_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot groupUsersKeysSnapshot) {
                for (final DataSnapshot oneGroupUserKey : groupUsersKeysSnapshot.getChildren()) {
                    String userId = oneGroupUserKey.getKey();
                    getUserRef(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userSnapshot) {
                            InvitedUserModel newInvitedUser = FirebaseSerializer.serializeInvitedUser(oneGroupUserKey, userSnapshot);
                            result.add(newInvitedUser);
                            if (groupUsersKeysSnapshot.getChildrenCount() == result.size())
                                callback.onDataFetched(result);
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

            }
        });
    }


    @Override
    public void getSessionStatus(String sessionId, final Get<SessionStatus> callback) {
        getSessionRef(sessionId).child(SessionEntry.KEY_SESSION_STATUS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                String sessionStatus = dataSnapshot.getValue().toString();
                callback.onDataFetched(SessionStatus.fromString(sessionStatus));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void setAttendanceStatus(String sessionId, AttendanceStatus status, final Update callback) {
        getSessionRef(sessionId).child(SessionEntry.KEY_ATTENDANCE_STATUS)
                .setValue(status.toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (callback == null)
                            return;
                        if (task.isSuccessful())
                            callback.onUpdateSuccess();
                        else if (task.getException() != null)
                            callback.onError(task.getException().getMessage());
                    }
                });
    }

    @Override
    public void setSessionSecret(String sessionId, String secret, final Update callback) {
        getSessionRef(sessionId).child(SessionEntry.KEY_SECRET)
                .setValue(secret)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (callback == null)
                            return;
                        if (task.isSuccessful())
                            callback.onUpdateSuccess();
                        else if (task.getException() != null)
                            callback.onError(task.getException().getMessage());
                    }
                });
    }

    @Override
    public Listen ListenSessionMembers(String sessionId, final Listen<MemberModel> callback) {
        // expects the names list of this session to be inserted correctly under session snapshot
        // at insertion of the new session, session should get the currently accepted users
        // and put them under the names list of its session key. And all should be false as
        // there is no one take attendance yet.
        DatabaseReference namesListRef = getSessionRef(sessionId).child(SessionEntry.KEY_NAMES_LIST);
//        ValueEventWithRef eventWithRef = new ValueEventWithRef(namesListRef);
        ChildEventListener listener = namesListRef.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(final DataSnapshot memberSnapshot, String s) {
                        continueWithAttendee(memberSnapshot);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot memberSnapshot, String s) {
                        continueWithAttendee(memberSnapshot);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }

                    private void continueWithAttendee(final DataSnapshot attendeeSnapshot) {
                        final String userId = attendeeSnapshot.getKey();
                        getUserRef(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot userSnapshot) {
                                MemberModel newAttendee = FirebaseSerializer.serializeAttendee(attendeeSnapshot, userSnapshot);
                                callback.onDataReceived(newAttendee);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
        );
//        eventWithRef.setListener(listener);
//        listeners.add(eventWithRef);
        return callback;
    }

    @Override
    public void setMemberAttendance(String sessionId, String memberId, boolean isAttend, final Update callback) {
        getSessionRef(sessionId).child(SessionEntry.KEY_NAMES_LIST)
                .child(memberId).child(SessionEntry.KEY_ATTEND).setValue(isAttend).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (callback == null)
                    return;
                if (task.isSuccessful())
                    callback.onUpdateSuccess();
                else if (task.getException() != null)
                    callback.onError(task.getException().getMessage());
            }
        });
    }

    @Override
    public void deleteNote(String sessionId, String memberId, String noteId, final Delete callback) {
        Task<Void> deleteTask = getSessionRef(sessionId).child(SessionEntry.KEY_NAMES_LIST)
                .child(memberId).child(SessionEntry.KEY_NOTES).child(noteId).removeValue();
        if (callback == null)
            return;
        deleteTask.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    callback.onDeleted();
                else if (task.getException() != null)
                    callback.onError(task.getException().getMessage());
            }
        });
    }

    @Override
    public void addNote(String sessionId, String memberId, final String noteText, final Insert<NoteModel> callback) {
        DatabaseReference newNoteRef = getSessionRef(sessionId).child(SessionEntry.KEY_NAMES_LIST)
                .child(memberId).child(SessionEntry.KEY_NOTES).push();
        final String noteId = newNoteRef.getKey();
        newNoteRef.setValue(noteText);
        if (callback == null)
            return;
        callback.onDataInserted(new NoteModel(noteId, noteText));
    }
}










