package cmp.sem.team8.smarlecture.common.data.firebase;


import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;
import cmp.sem.team8.smarlecture.common.data.model.FileModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupMessageModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupStatisticsModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * This is the implementation of the {@link cmp.sem.team8.smarlecture.common.data.DataService}
 * using firebase database
 * <p>
 * this class do the logic of querying data from firebase database
 */
public class FirebaseRepository extends FirebaseRepoHelper {

    private static FirebaseRepository INSTANCE = null;

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
                UserModel userModel = Serializer.user(userSnapshot);
                setProfileImage(userModel);
            }

            private void setProfileImage(final UserModel userModel) {
                getProfileImageRef(userModel.getId()).getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        userModel.setProfileImage(bytes);
                        callback.onDataFetched(userModel);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        callback.onDataFetched(userModel);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getUserGrade(final int Grade, String userId, final Get<UserGradeModel> callback) {
        getUserRef(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot userSnapshot) {
                if (!userSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                UserModel userModel = Serializer.user(userSnapshot);
                UserGradeModel model = new UserGradeModel(userModel.getName(), userModel.getEmail(), userModel.getId(), Integer.toString(Grade));
                callback.onDataFetched(model);
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
        final byte[] profileImage = userModel.getProfileImage();

        getUserRef(userModel.getId()).setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    callback.onError("Can't insert the user");
                    return;
                }
                callback.onDataInserted(null);
                if (profileImage != null)
                    insertProfileImage();
            }

            private void insertProfileImage() {
                getProfileImageRef(userModel.getId()).putBytes(profileImage);
            }
        });
    }

    @Override
    public void updateGroupGrades(String groupId, ArrayList<String> ids, ArrayList<Integer> grade, final Update callback) {

        for (int i = 0; i < ids.size(); i++) {
            getReference(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_NAMES_LIST).child(ids.get(i))
                    .child(GroupEntry.KEY_NAMES_LIST_GRADE).setValue(grade.get(i));
        }

        callback.onUpdateSuccess();
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
    public void updateUserProfileImage(String userId, byte[] newImageBytes, final Update callback) {
        getProfileImageRef(userId).putBytes(newImageBytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (callback != null)
                            callback.onUpdateSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                if (callback != null)
                    callback.onError(e.getMessage());
            }
        });
    }


    @Override
    public void getGroupAndItsSessionNameList(String groupId, final Get<GroupStatisticsModel> callback) {

        final GroupStatisticsModel group = new GroupStatisticsModel(null, null);

        getReference(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_NAMES_LIST).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    synchronized public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() == 0) {
                            callback.onDataNotAvailable();
                            return;
                        }
                        ArrayList<String> groupp = new ArrayList<>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            groupp.add(ds.getKey());
                        }
                        group.setGroupMembers(groupp);
                    }

                    @Override
                    synchronized public void onCancelled(DatabaseError databaseError) {

                    }
                });

        //  getReference(GroupEntry.KEY_SESSIONS).child(groupId).child(GroupEntry.KEY_SESSIONS).addListenerForSingleValueEvent(new ValueEventListener() {
        getReference(FirebaseContract.SessionEntry.KEY_THIS).orderByChild(FirebaseContract.SessionEntry.KEY_FOR_GROUP_ID).equalTo(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            String SessionID;

            @Override
            synchronized public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<ArrayList<String>> sessionMem = new ArrayList<>();

                if (dataSnapshot.getChildrenCount() == 0) {
                    callback.onDataNotAvailable();
                    return;
                }

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SessionID = ds.getKey();
                    // DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child(FirebaseContract.SessionEntry.KEY_THIS).child(FirebaseContract.SessionEntry.KEY_NAMES_LIST);

                    getReference(FirebaseContract.SessionEntry.KEY_THIS).child(SessionID).child(FirebaseContract.SessionEntry.KEY_NAMES_LIST).addListenerForSingleValueEvent(new SessionValueEvent(SessionID, sessionMem, dataSnapshot.getChildrenCount()));
                }

            }

            @Override
            synchronized public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }

            class SessionValueEvent implements ValueEventListener {

                String sessionID;
                ArrayList<ArrayList<String>> sessionMem;
                long count;

                SessionValueEvent(String SessionID, ArrayList<ArrayList<String>> sessionMem, long count) {
                    this.sessionID = SessionID;
                    this.sessionMem = sessionMem;
                    this.count = count;

                }

                @Override

                synchronized public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> userIdList = new ArrayList<>();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        userIdList.add(dss.getKey());
                    }
                    sessionMem.add(userIdList);

                    if (sessionMem.size() == count) {
                        group.setSessionMembers(sessionMem);
                        callback.onDataFetched(group);
                    }
                }

                @Override
                synchronized public void onCancelled(DatabaseError databaseError) {
                    callback.onError(databaseError.getMessage());
                }
            }
        });


    }

    @Override
    public void getGroupGrade(String groupId, final Get<ArrayList<UserGradeModel>> callback) {

        getReference(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_NAMES_LIST).
                addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    synchronized public void onDataChange(DataSnapshot dataSnapshot) {
                        final ArrayList<UserGradeModel> users = new ArrayList<>();

                        if (dataSnapshot.getChildrenCount() == 0)
                            callback.onDataNotAvailable();

                        for (DataSnapshot ch : dataSnapshot.getChildren()) {
                            HashMap<String, Object> ob = (HashMap<String, Object>) ch.getValue();
                            //ob.clear();
                            String UserId = ch.getKey();
                            long num = (Long) ob.get(GroupEntry.KEY_NAMES_LIST_GRADE);
                            String attendaceGrade = Long.toString(num);
                            getUserRef(UserId).addListenerForSingleValueEvent(new UserValueEvent(attendaceGrade, users, dataSnapshot.getChildrenCount()));
                        }
                    }

                    @Override
                    synchronized public void onCancelled(DatabaseError databaseError) {

                    }

                    class UserValueEvent implements ValueEventListener {

                        String attendanceGrade;
                        ArrayList<UserGradeModel> users;
                        long count;

                        UserValueEvent(String attendanceGrade, ArrayList<UserGradeModel> users, long count) {
                            this.attendanceGrade = attendanceGrade;
                            this.users = users;
                            this.count = count;

                        }

                        @Override

                        synchronized public void onDataChange(DataSnapshot userSnapshot) {
                            if (!userSnapshot.exists()) {
                                callback.onDataNotAvailable();
                                return;
                            }
                            UserModel userModel = Serializer.user(userSnapshot);
                            UserGradeModel userGradeModel = new UserGradeModel(userModel.getName(), userModel.getEmail(), userModel.getId(), attendanceGrade);
                            users.add(userGradeModel);

                            if (users.size() == count) {
                                callback.onDataFetched(users);
                            }
                        }

                        @Override
                        synchronized public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.getMessage());
                        }
                    }

                });

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
                        HashMap<String, Object> value = new HashMap<>();
                        value.put(GroupEntry.KEY_NAMES_LIST_IS_MEMBER, false);
                        value.put(GroupEntry.KEY_NAMES_LIST_GRADE, 0);
                        getGroupRef(groupId).child(GroupEntry.KEY_NAMES_LIST)
                                .child(userSnapshot.getKey()).setValue(value); // false mean not accepted yet

                        // insert the group to the requested groups of the user
                        getUserRef(userSnapshot.getKey()).child(UserEntry.KEY_INVITATIONS)
                                .child(groupId)
                                .setValue(false);  // false mean not accepted (following) yet
                        final UserModel user = Serializer.user(userSnapshot);
                        getProfileImageRef(user.getId()).getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                user.setProfileImage(bytes);
                                callback.onDataInserted(user);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callback.onDataInserted(user);
                            }
                        });
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
                .child(userId).child(GroupEntry.KEY_NAMES_LIST_IS_MEMBER).setValue(true);

        // subscribe user to this group
        FirebaseMessaging.getInstance().subscribeToTopic(groupId);

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
                ArrayList<String> groupsKeys = Serializer.getKeys(groupsKeysSnapshot);
                for (final String oneGroupKey : groupsKeys) {

                    getGroupRef(oneGroupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot oneGroupSnapshot) {
                            getUserRef(oneGroupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot userSnapshot) {
                                    final ArrayList<String> sessionsKeys = Serializer.getKeys(oneGroupSnapshot.child(GroupEntry.KEY_SESSIONS));
                                    for (final String oneSessionKey : sessionsKeys) {
                                        getSessionRef(oneSessionKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot oneSessionSnapshot) {
                                                if (!isValid(SessionStatus.fromString(oneSessionSnapshot.child(FirebaseContract.SessionEntry.KEY_SESSION_STATUS).getValue(String.class))))
                                                    return;
                                                SessionForUserModel sessionForUserModel = Serializer
                                                        .sessionForUser
                                                                (userSnapshot, oneSessionSnapshot, oneGroupSnapshot);
                                                setProfileImage(sessionForUserModel);
                                            }

                                            private void setProfileImage(final SessionForUserModel sessionForUserModel) {
                                                getProfileImageRef(sessionForUserModel.getOwner().getId()).getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {
                                                        sessionForUserModel.getOwner().setProfileImage(bytes);
                                                        callback.onDataFetched(sessionForUserModel);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        e.printStackTrace();
                                                        callback.onDataFetched(sessionForUserModel);
                                                    }
                                                });
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
                for (String groupKey : Serializer.getKeys(invitationsKeysSnapshot)) {
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
                    callback.onDataFetched(Serializer.groupInvitation(groupSnapshot, userSnapshot));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onError(databaseError.getMessage());
                }
            }
        });
    }


    @Override
    public void getGroupMembers(String groupId, final Get<ArrayList<InvitedUserModel>> callback) {
        final ArrayList<InvitedUserModel> result = new ArrayList<>();
        getGroupRef(groupId).child(GroupEntry.KEY_NAMES_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot groupUsersKeysSnapshot) {
                for (final DataSnapshot oneGroupUserKey : groupUsersKeysSnapshot.getChildren()) {
                    String userId = oneGroupUserKey.getKey();
                    getUserRef(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userSnapshot) {
                            final InvitedUserModel newInvitedUser = Serializer.invitedUser(oneGroupUserKey, userSnapshot);
                            getProfileImageRef(newInvitedUser.getId()).getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    newInvitedUser.setProfileImage(bytes);
                                    result.add(newInvitedUser);
                                    if (groupUsersKeysSnapshot.getChildrenCount() == result.size())
                                        callback.onDataFetched(result);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    result.add(newInvitedUser);
                                    if (groupUsersKeysSnapshot.getChildrenCount() == result.size())
                                        callback.onDataFetched(result);
                                }
                            });
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
    public void setSessionStatus(String sessionId, SessionStatus status, final Insert<Void> callback) {
        Task<Void> task = getSessionRef(sessionId).child(SessionEntry.KEY_SESSION_STATUS).setValue(status.toString());
        if (callback == null)
            return;
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onDataInserted(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onError(e.getMessage());
            }
        });
    }


    @Override
    public void getJoinedSessionInfo(final String sessionID, final String groupID, final Get<SessionForUserModel> callback) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mSessionRef = mRef.child(FirebaseContract.SessionEntry.KEY_THIS).child(sessionID);
        mSessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot sessionSnapshot) {
                if (!sessionSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                DatabaseReference mGroupRef = mRef.child(GroupEntry.KEY_THIS).child(groupID);
                mGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot groupSnapshot) {
                        if (!groupSnapshot.exists()) {
                            callback.onDataNotAvailable();
                            return;
                        }
                        final String ownerID = groupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class);
                        DatabaseReference mUserRef = mRef.child(UserEntry.KEY_THIS).child(ownerID);
                        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot ownerSnapshot) {
                                if (!ownerSnapshot.exists()) {
                                    callback.onDataNotAvailable();
                                    return;
                                }
                                callback.onDataFetched(Serializer.sessionForUser(ownerSnapshot, sessionSnapshot, groupSnapshot));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                callback.onError(databaseError.getMessage());

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.getMessage());

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());

            }
        });
    }

    @Override
    public Listen listenSessionStatus(final String sessionID, final Listen<SessionStatus> callback) {
        DatabaseReference statusRef = getSessionRef(sessionID).child(SessionEntry.KEY_SESSION_STATUS);
        ValueEventListener valueEventListener = statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot statusSnapshot) {
                if (!statusSnapshot.exists()) return;
                String sessionStatus = statusSnapshot.getValue(String.class);
                callback.onDataReceived(SessionStatus.fromString(sessionStatus));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        addNewListener(callback, valueEventListener, statusRef);
        return callback;
    }

    @Override
    public void getObjectives(final String sessionID, final Get<ArrayList<ObjectiveModel>> callback) {
        //check that the session exist
        FirebaseDatabase.getInstance().getReference().child(FirebaseContract.SessionEntry.KEY_THIS).child(sessionID).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DatabaseReference mObjectivesRef = FirebaseDatabase.getInstance().getReference()
                                    .child(FirebaseContract.SessionEntry.KEY_THIS)
                                    .child(sessionID)
                                    .child(FirebaseContract.SessionEntry.KEY_FOR_OBJECTIVES_LIST);
                            mObjectivesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    ArrayList<ObjectiveModel> objectiveList = new ArrayList<>();
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {

                                        if (!dataSnapshot.exists())
                                            continue;

                                        String key = child.getKey();
                                        String name = child.child(FirebaseContract.ObjectiveEntry.KEY_DESC).getValue(String.class);
                                        Float averageRating = child.child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).getValue(Float.class);


                                        Integer numberUsersRated = child.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).getValue(Integer.class);
                                        //Integer numberUsersRated=Integer.valueOf(child.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).getValue(String.class));
                                        //child.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).getValue(Integer.class);

                                        ObjectiveModel thisObjective = new ObjectiveModel();

                                        thisObjective.setmObjectiveDescription(name);

                                        thisObjective.setmObjectiveID(key);

                                        thisObjective.setmNumberofUsersRatedThisObjective(numberUsersRated);

                                        thisObjective.setmObjectivesAverageRating(averageRating);

                                        thisObjective.setmSessionID(sessionID);

                                        objectiveList.add(thisObjective);
                                    }
                                    callback.onDataFetched(objectiveList);
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
    public void updateObjectivesRating(final String sessionID, final String objectiveID, final Float newObjectiveRating, final Integer newNumberUsersRated, final Update callback) {
        //check that the session id is correct
        FirebaseDatabase.getInstance().getReference().child(FirebaseContract.SessionEntry.KEY_THIS).child(sessionID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance().getReference().
                            child(FirebaseContract.SessionEntry.KEY_THIS).
                            child(sessionID).
                            child(FirebaseContract.SessionEntry.KEY_FOR_OBJECTIVES_LIST).
                            child(objectiveID)
                            .child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).setValue(newObjectiveRating);

                    FirebaseDatabase.getInstance().getReference().
                            child(FirebaseContract.SessionEntry.KEY_THIS).
                            child(sessionID).
                            child(FirebaseContract.SessionEntry.KEY_FOR_OBJECTIVES_LIST).
                            child(objectiveID)
                            .child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).setValue(newNumberUsersRated);
                    callback.onUpdateSuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());

            }
        });
    }

    @Override
    public void insertObjective(final String sessionID, final String addedObjectiveDescription, final boolean isOffline, final Insert<ObjectiveModel> callback) {
        DatabaseReference mobjectiveRef = FirebaseDatabase.getInstance().
                getReference().child(SessionEntry.KEY_THIS).child(sessionID)
                .child(SessionEntry.KEY_FOR_OBJECTIVES_LIST).push();
        final String objectiveID = mobjectiveRef.getKey();


        if (isOffline) {

            ObjectiveModel addedObjective = new ObjectiveModel();

            addedObjective.setmSessionID(sessionID);

            addedObjective.setmObjectiveID(objectiveID);

            addedObjective.setmObjectiveDescription(addedObjectiveDescription);

            addedObjective.setmObjectivesAverageRating(0);

            addedObjective.setmNumberofUsersRatedThisObjective(0);

            callback.onDataInserted(addedObjective);
        }
        mobjectiveRef.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).setValue(0);

        mobjectiveRef.child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).setValue(0);

        mobjectiveRef.child(FirebaseContract.ObjectiveEntry.KEY_DESC).setValue(addedObjectiveDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    if (!isOffline) {

                        ObjectiveModel addedObjective = new ObjectiveModel();

                        addedObjective.setmSessionID(sessionID);

                        addedObjective.setmObjectiveID(objectiveID);

                        addedObjective.setmObjectiveDescription(addedObjectiveDescription);

                        addedObjective.setmNumberofUsersRatedThisObjective(0);

                        addedObjective.setmObjectivesAverageRating(0);


                        callback.onDataInserted(addedObjective);

                    }
                } else {

                    callback.onError(task.getException().getMessage());
                }
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
                                MemberModel newAttendee = Serializer.sessionMember(attendeeSnapshot, userSnapshot);
                                callback.onDataReceived(newAttendee);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
        );
        addNewListener(callback, listener, namesListRef);
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
    public void editObjective(String objectiveID, String sessionID, String objectiveDescription, final boolean isOffline, final Update callback) {
        if (isOffline)
            callback.onUpdateSuccess();
        else {
            DatabaseReference mObjectiveRef = FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(sessionID).child(SessionEntry.KEY_FOR_OBJECTIVES_LIST).child(objectiveID).child(FirebaseContract.ObjectiveEntry.KEY_DESC);
            mObjectiveRef.setValue(objectiveDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        callback.onUpdateSuccess();
                    else
                        callback.onError(task.getException().getMessage());
                }
            });

        }


    }

    @Override
    public void deleteObjective(String objectiveID, String sesisonID, boolean isOffline, final Delete callback) {
        if (isOffline) {
            callback.onDeleted();
        } else {
            DatabaseReference mObjectiveRef = FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(sesisonID).child(SessionEntry.KEY_FOR_OBJECTIVES_LIST).child(objectiveID);
            mObjectiveRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        callback.onDeleted();
                    else
                        callback.onError(task.getException().getMessage());
                }
            });
        }
    }

    @Override
    public void getGroupSessions(String groupId, final Get<ArrayList<SessionModel>> callback) {
        DatabaseReference mGroupRef = FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS);
        mGroupRef.orderByChild(SessionEntry.KEY_FOR_GROUP_ID).equalTo(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;


                ArrayList<SessionModel> sessionsList = new ArrayList<>();
                for (DataSnapshot session : dataSnapshot.getChildren()) {
                    if (!session.exists())
                        continue;
                    SessionModel addedSession = Serializer.session(session);
                    sessionsList.add(addedSession);


                }
                callback.onDataFetched(sessionsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());

            }
        });

    }

    @Override
    public void addSession(final String groupId, String sessionId, String sessionName, final Insert<Void> callback) {
        final DatabaseReference mSessionRef = FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(sessionId);
        final DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_NAMES_LIST);

        mSessionRef.child(SessionEntry.KEY_SESSION_STATUS).setValue(SessionStatus.NOT_ACTIVATED.toString());
        mSessionRef.child(SessionEntry.KEY_ATTENDANCE_STATUS).setValue(AttendanceStatus.NOT_ACTIVATED.toString());
        mSessionRef.child(SessionEntry.KEY_FOR_GROUP_ID).setValue(groupId);
        mSessionRef.child(SessionEntry.KEY_FOR_SESSION_NAME_).setValue(sessionName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onDataInserted(null);
                if (task.isSuccessful())
                    //callback.onDataInserted(null);
                    groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists())
                                return;
                            for (DataSnapshot member : dataSnapshot.getChildren()) {
                                //if true then the user accepted the invitation request
                                if (member.child(GroupEntry.KEY_NAMES_LIST_IS_MEMBER).getValue().equals(true)) {
                                    mSessionRef.child(SessionEntry.KEY_NAMES_LIST).child(member.getKey()).child(SessionEntry.KEY_ATTEND).setValue(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.getMessage());
                        }
                    });
                else
                    callback.onError(task.getException().getMessage());
            }
        });
        getGroupRef(groupId).child(GroupEntry.KEY_SESSIONS).child(sessionId).setValue(true);
    }

    public void getGroupInfoForExport(final String groupId, final Get<FileModel> callback) {
        // 1- set the group
        // 2- loop over all group sessions
        //      - loop over all session members
        //          - if new member (first time to appear in the list) -> put him in the groupMembers list
        //          - if not, get him from the list add this session to his inSessions

        final FileModel result = new FileModel();
        final ArrayList<SessionModel> groupSessions = new ArrayList<>();
        final ArrayList<FileModel.GroupMember> groupMembers = new ArrayList<>();

        getGroupRef(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            private boolean isLastSession = false;
            private int sessionHandledCount = 0;
            private int currMembersHandled = 0;
            private DataSnapshot groupSnapshot;

            @Override
            public void onDataChange(DataSnapshot groupSnapshot) {
                GroupModel group = Serializer.group(groupSnapshot);
                result.setGroup(group);
                this.groupSnapshot = groupSnapshot;
                withGroupSnapshot();
            }

            private void withGroupSnapshot() {
                final DataSnapshot sessionsKeysSnapshot = groupSnapshot.child(GroupEntry.KEY_SESSIONS);
                if (!sessionsKeysSnapshot.exists() || !groupSnapshot.child(GroupEntry.KEY_NAMES_LIST).exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                for (final DataSnapshot sessionKey : sessionsKeysSnapshot.getChildren()) {
                    String currSessionId = sessionKey.getKey();
                    getSessionRef(currSessionId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot sessionSnapshot) {
                            SessionModel sessionModel = Serializer.session(sessionSnapshot);
                            groupSessions.add(sessionModel);

                            if (++sessionHandledCount == sessionsKeysSnapshot.getChildrenCount())
                                isLastSession = true;
                            currMembersHandled = 0;
                            withSessionMembersList(sessionKey.getKey(), sessionSnapshot.child(SessionEntry.KEY_NAMES_LIST));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            callback.onError(databaseError.getMessage());
                        }

                    });
                }
            }

            private void withSessionMembersList(final String sessionId, DataSnapshot sessionMembersSnapshot) {
                if (!sessionMembersSnapshot.exists()) {
                    // if this session doesn't have any members, then check to return data (may be the last session in the group)
                    returnDataIfLastSession();
                    return;
                }
                long membersCount = sessionMembersSnapshot.getChildrenCount();
                for (final DataSnapshot oneSessionMemberKeySnapshot : sessionMembersSnapshot.getChildren()) {
                    String currMemberKey = oneSessionMemberKeySnapshot.getKey();
                    FileModel.GroupMember existedGroupMember = findGroupMemberById(currMemberKey);
                    if (existedGroupMember != null) {
                        // add to this group member, this contribution (member model)
                        boolean isAttend = ((boolean) oneSessionMemberKeySnapshot.child(SessionEntry.KEY_ATTEND).getValue());
                        ArrayList<NoteModel> thisSessionMemberNotes = Serializer.notes(oneSessionMemberKeySnapshot.child(SessionEntry.KEY_NOTES));
                        existedGroupMember.getInSessions().add(new FileModel.SessionMember(sessionId, isAttend, thisSessionMemberNotes));

                        if (++currMembersHandled == membersCount)
                            returnDataIfLastSession();
                        continue; // continue and don't fetch the data again
                    }
                    withSessionMemberSnapshot(oneSessionMemberKeySnapshot, sessionId, currMemberKey, membersCount);
                }

            }

            private void withSessionMemberSnapshot(final DataSnapshot sessionMemberKeySnapshot,
                                                   final String sessionId,
                                                   String currMemberKey,
                                                   final long membersCount) {
                getUser(currMemberKey, new Get<UserModel>() {
                    @Override
                    public void onDataFetched(UserModel thisUser) {
                        final FileModel.GroupMember newGroupMember = new FileModel.GroupMember(thisUser);
                        boolean isAttend = ((boolean) sessionMemberKeySnapshot.child(SessionEntry.KEY_ATTEND).getValue());
                        ArrayList<NoteModel> thisSessionMemberNotes = Serializer.notes(sessionMemberKeySnapshot.child(SessionEntry.KEY_NOTES));
                        newGroupMember.getInSessions().add(new FileModel.SessionMember(sessionId, isAttend, thisSessionMemberNotes));
                        DataSnapshot gradeSnapshot = groupSnapshot.child(GroupEntry.KEY_NAMES_LIST)
                                .child(thisUser.getId()).child(GroupEntry.KEY_NAMES_LIST_GRADE);
                        if (gradeSnapshot.exists())
                            newGroupMember.setAttendanceGrade(gradeSnapshot.getValue(float.class));
//                        if (grade != null)
                        groupMembers.add(newGroupMember);

                        if (++currMembersHandled == membersCount)
                            returnDataIfLastSession();
                    }
                });
            }

            private void returnDataIfLastSession() {
                if (!isLastSession)
                    return;
                result.setSessions(groupSessions);
                result.setMembers(groupMembers);
                callback.onDataFetched(result);
            }

            private FileModel.GroupMember findGroupMemberById(String memberId) {
                for (FileModel.GroupMember groupMember : groupMembers)
                    if (groupMember.getId().equals(memberId))
                        return groupMember;
                return null;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }


    @Override
    public void getSessionById(String sessionId, final Get<SessionModel> callback) {
        getSessionRef(sessionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot sessionSnapshot) {
                if (!sessionSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                SessionModel session = Serializer.session(sessionSnapshot);
                callback.onDataFetched(session);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }


    @Override
    public void editSession(String sessionId, String sessionName, final boolean isOffline, final Update callback) {
        if (isOffline)
            callback.onUpdateSuccess();

        FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS)
                .child(sessionId).child(SessionEntry.KEY_FOR_SESSION_NAME_).
                setValue(sessionName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!isOffline)
                        callback.onUpdateSuccess();
                } else if (task.getException() != null)
                    callback.onError(task.getException().getMessage());
            }
        });
    }


    @Override
    public void deleteSession(final String sessoinId, final boolean isOffline, final Delete callback) {
        if (isOffline)
            callback.onDeleted();
        getSessionRef(sessoinId).child(SessionEntry.KEY_FOR_GROUP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot groupIdSnapshot) {
                getGroupRef(groupIdSnapshot.getValue(String.class))
                        .child(GroupEntry.KEY_SESSIONS).child(sessoinId)
                        .removeValue();
                getSessionRef(sessoinId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (!isOffline)
                                callback.onDeleted();

                        } else callback.onError(task.getException().getMessage());


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void deleteGroupById(final String groupId, final boolean isOffline, final Delete callback) {
        FirebaseDatabase.getInstance().getReference().child(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_SESSIONS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot sessionSnapShot : dataSnapshot.getChildren()) {
                        String sessionKey = sessionSnapShot.getKey();
                        FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(sessionKey).removeValue();
                    }
                }
                if (isOffline)
                    callback.onDeleted();
                FirebaseDatabase.getInstance().getReference().child(GroupEntry.KEY_THIS).child(groupId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (!isOffline)
                                callback.onDeleted();
                        } else
                            callback.onError(task.getException().getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());

            }
        });
    }

    @Override
    public void addGroup(String userId, final String groupName, final boolean isOffline, final Insert<String> callback) {

        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference().child(GroupEntry.KEY_THIS);
        DatabaseReference newGroupRef = groupRef.push();
        final String groupId = newGroupRef.getKey();
        if (isOffline)
            callback.onDataInserted(groupId);

        newGroupRef.child(GroupEntry.KEY_OWNER_ID).setValue(userId);
        newGroupRef.child(GroupEntry.KEY_NAME).setValue(groupName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    if (!isOffline)
                        callback.onDataInserted(groupId);
                } else {

                    callback.onError(task.getException().getMessage());
                }
            }
        });

    }

    @Override
    public void updateGroup(String groupId, String groupName, final boolean isOffline, final Update callback) {
        if (isOffline)
            callback.onUpdateSuccess();
        FirebaseDatabase.getInstance().getReference().child(GroupEntry.KEY_THIS).child(groupId)
                .child(GroupEntry.KEY_NAME).setValue(groupName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!isOffline)
                        callback.onUpdateSuccess();
                } else {

                    callback.onError(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void getGroupId(String sessionId, final Get<String> callback) {
        getSessionRef(sessionId).child(SessionEntry.KEY_FOR_GROUP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                callback.onDataFetched(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void getGroupsForUser(String userId, final Get<ArrayList<GroupModel>> callback) {
        Query userGroups = FirebaseDatabase.getInstance().getReference(GroupEntry.KEY_THIS)
                .orderByChild(GroupEntry.KEY_OWNER_ID)
                .equalTo(userId);
        userGroups.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<GroupModel> list = new ArrayList<>();
                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists())
                        continue;
                    GroupModel groupModel = Serializer.group(groupSnapShot);


                    list.add(groupModel);
                }
                callback.onDataFetched(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());

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

    @Override
    public Listen listenAttendanceStatus(String sessionId, final Listen<AttendanceStatus> callback) {
//        listeners.add(callback);
        DatabaseReference attendanceRef = getSessionRef(sessionId).child(SessionEntry.KEY_ATTENDANCE_STATUS);
        ValueEventListener listener = attendanceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot statusSnapshot) {
                if (!statusSnapshot.exists())
                    return;
                AttendanceStatus status = AttendanceStatus.fromString(
                        statusSnapshot.getValue(String.class));
                callback.onDataReceived(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        addNewListener(callback, listener, attendanceRef);
        return callback;
    }

    @Override
    public void getGroupMessages(final String groupId, final Get<ArrayList<GroupMessageModel>> callback) {
        getGroupRef(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot groupSnapshot) {
                getGroupMessagesRef(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot messagesSnapshot) {
                        ArrayList<GroupMessageModel> groupMessages = Serializer.groupMessages(groupSnapshot, messagesSnapshot);
                        if (groupMessages != null)
                            callback.onDataFetched(groupMessages);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onError(databaseError.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public Listen ListenSessionQuestions(String sessionId, final Listen<QuestionModel> callback) {
        DatabaseReference questionsRef = getSessionRef(sessionId).child(SessionEntry.KEY_QUESTIONS);
        ChildEventListener childEventListener = questionsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot newQuestionSnapshot, String s) {
                String ownerId = newQuestionSnapshot.child(SessionEntry.KEY_QUESTION_OWNER).getValue(String.class);
                getUserRef(ownerId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot ownerSnapshot) {
                        QuestionModel question = Serializer.question(newQuestionSnapshot, ownerSnapshot);
                        callback.onDataReceived(question);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });
        addNewListener(callback, childEventListener, questionsRef);
        return callback;
    }

    @Override
    public void addQuestionToSession(String sessionId, String userId, final String text, final Insert<QuestionModel> callback) {
        final DatabaseReference questionRef = getSessionRef(sessionId).child(SessionEntry.KEY_QUESTIONS).push();
        HashMap<String, String> values = new HashMap<>();
        values.put(SessionEntry.KEY_QUESTION_TEXT, text);
        values.put(SessionEntry.KEY_QUESTION_OWNER, userId);
        questionRef.setValue(values);
        getUser(userId, new Get<UserModel>() {
            @Override
            public void onDataFetched(UserModel owner) {
                if (callback != null)
                    callback.onDataInserted(new QuestionModel(questionRef.getKey(), owner, text));
            }
        });
    }

    @Override
    public void getObjectivesCount(String sessionId, final Get<Long> callback) {
        DatabaseReference mObjectiveRef = getSessionRef(sessionId).child(SessionEntry.KEY_FOR_OBJECTIVES_LIST);
        mObjectiveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onDataFetched(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }

    @Override
    public void sendGroupNotification(final String groupId, final String message, final Insert<Void> callback) {
        getGroupRef(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot groupSnapshot) {
                GroupModel group = Serializer.group(groupSnapshot);
                if (group == null) {
                    if (callback != null)
                        callback.onError("Error in sending message");
                    return;
                }
                DatabaseReference notificationRef = getReference(FirebaseContract.GroupMessagesEntry.KEY_THIS).child(groupId)
                        .push();
                notificationRef.child(FirebaseContract.GroupMessagesEntry.KEY_BODY)
                        .setValue(message);
                notificationRef.child(FirebaseContract.GroupMessagesEntry.KEY_TITLE)
                        .setValue(group.getName());
                if (callback != null)
                    callback.onDataInserted(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (callback != null)
                    callback.onError(databaseError.getMessage());
            }
        });
    }
}
