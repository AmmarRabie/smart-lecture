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
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserAttendanceModel;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;
import cmp.sem.team8.smarlecture.model.ObjectiveModel;

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
                UserModel userModel = FirebaseSerializer.serializeUser(userSnapshot);
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
                    .child(GroupEntry.KEY_NAMES_LIST_grade).setValue(grade.get(i));
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
        listenUserEvent.setFirebaseListener(valueEventListener);
        listeners.add(listenUserEvent);
    }

    @Override
    public void getSessionsOfGroup(String groupId, Get<ArrayList<SessionModel>> callback) {

    }

    @Override
    public void getGroupAndItsSessionNameList(String groupId, final Get<GroupStatisticsModel> callback) {

        final GroupStatisticsModel group = new GroupStatisticsModel(null, null);

        getReference(GroupEntry.KEY_THIS).child(groupId).child(GroupEntry.KEY_NAMES_LIST).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    synchronized public void onDataChange(DataSnapshot dataSnapshot) {
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
        getReference(FirebaseContract.SessionEntry.KEY_THIS).orderByChild(FirebaseContract.SessionEntry.KEY_FOR_GROUP_ID).equalTo(groupId).addValueEventListener(new ValueEventListener() {
            String SessionID;

            @Override
            synchronized public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<ArrayList<String>> sessionMem = new ArrayList<>();

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

                        for (DataSnapshot ch : dataSnapshot.getChildren()) {
                            HashMap<String, Object> ob = (HashMap<String, Object>) ch.getValue();
                            //ob.clear();
                            String UserId = ch.getKey();
                            long num = (Long) ob.get(GroupEntry.KEY_NAMES_LIST_grade);
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
                            UserModel userModel = FirebaseSerializer.serializeUser(userSnapshot);
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
                        value.put(GroupEntry.KEY_NAMES_LIST_invite, false);
                        value.put(GroupEntry.KEY_NAMES_LIST_grade, 0);
                        getGroupRef(groupId).child(GroupEntry.KEY_NAMES_LIST)
                                .child(userSnapshot.getKey()).setValue(value); // false mean not accepted yet

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
    public void getUsersListOfGroupTemp(String groupId, Get<ArrayList<UserAttendanceModel>> callback) {

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
    public void getJoinedSessionInfo(final String sessionID, final String groupID, final Get<SessionForUserModel> callback) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mSessionRef = mRef.child(FirebaseContract.SessionEntry.KEY_THIS).child(sessionID);
        mSessionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    callback.onDataNotAvailable();
                    return;
                }
                final String sessionName = dataSnapshot.child(FirebaseContract.SessionEntry.KEY_FOR_SESSION_NAME_).getValue(String.class);
                DatabaseReference mGroupRef = mRef.child(GroupEntry.KEY_THIS).child(groupID);
                mGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            callback.onDataNotAvailable();
                            return;
                        }
                        final String groupName = dataSnapshot.child(GroupEntry.KEY_NAME).getValue(String.class);
                        final String ownerID = dataSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class);
                        DatabaseReference mUserRef = mRef.child(UserEntry.KEY_THIS).child(ownerID);
                        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    callback.onDataNotAvailable();
                                    return;
                                }
                                String OwnerName = dataSnapshot.child(UserEntry.KEY_NAME).getValue(String.class);
                                SessionForUserModel sessionInfo = new SessionForUserModel(sessionID, SessionStatus.OPEN, AttendanceStatus.CLOSED, sessionName, groupID, groupName, ownerID, OwnerName);
                                callback.onDataFetched(sessionInfo);


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
    public void listenForSessionStatus(String sessionID, final Listen<String> callback) {


        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child(FirebaseContract.SessionEntry.KEY_THIS)
                .child(sessionID).child(FirebaseContract.SessionEntry.KEY_SESSION_STATUS);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) return;
                String sessionStatus = dataSnapshot.getValue(String.class);
                callback.onDataReceived(sessionStatus);
                callback.increment();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
//        eventWithRef.setFirebaseListener(firebaseListener);
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
                    SessionModel addedSession = FirebaseSerializer.serializeSession(session);
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
                if (task.isSuccessful())
                    //callback.onDataInserted(null);
                    groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists())
                                return;
                            for (DataSnapshot member : dataSnapshot.getChildren()) {

                                //if true then the user accepted the invitation request
                                if (member.child("invite").getValue().equals(true)) {
                                    mSessionRef.child(SessionEntry.KEY_NAMES_LIST).child(member.getKey()).child(SessionEntry.KEY_ATTEND).setValue(false);
                                }
                            }
                            callback.onDataInserted(null);
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
    }

    public void getGroupInfoForExport(final String groupId, final Get<FileModel> callback) {
        final FileModel result = new FileModel();
        final ArrayList<SessionModel> groupSessions = new ArrayList<>();
        final ArrayList<FileModel.GroupMember> groupMembers = new ArrayList<>();

        getGroupRef(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
            private boolean isLastSession = false;
            private int sessionHandledCount = 0;
            private int currMembersHandled = 0;

            @Override
            public void onDataChange(DataSnapshot groupSnapshot) {
                GroupModel group = FirebaseSerializer.serializeGroup(groupSnapshot);
                result.setGroup(group);
                withGroupSnapshot(groupSnapshot);
            }

            private void withGroupSnapshot(DataSnapshot groupSnapshot) {
                final DataSnapshot sessionsKeysSnapshot = groupSnapshot.child(GroupEntry.KEY_SESSIONS);

                for (final DataSnapshot sessionKey : sessionsKeysSnapshot.getChildren()) {
                    String currSessionId = sessionKey.getKey();
                    getSessionRef(currSessionId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot sessionSnapshot) {
                            SessionModel sessionModel = FirebaseSerializer.serializeSession(sessionSnapshot);
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
                long membersCount = sessionMembersSnapshot.getChildrenCount();
                for (final DataSnapshot oneSessionMemberKeySnapshot : sessionMembersSnapshot.getChildren()) {
                    String currMemberKey = oneSessionMemberKeySnapshot.getKey();
                    FileModel.GroupMember existedGroupMember = findGroupMemberById(currMemberKey);
                    if (existedGroupMember != null) {
                        // add to this group member, this contribution (member model)
                        boolean isAttend = ((boolean) oneSessionMemberKeySnapshot.child(SessionEntry.KEY_ATTEND).getValue());
                        ArrayList<NoteModel> thisSessionMemberNotes = FirebaseSerializer.serializeNotes(oneSessionMemberKeySnapshot.child(SessionEntry.KEY_NOTES));
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
                        ArrayList<NoteModel> thisSessionMemberNotes = FirebaseSerializer.serializeNotes(sessionMemberKeySnapshot.child(SessionEntry.KEY_NOTES));
                        newGroupMember.getInSessions().add(new FileModel.SessionMember(sessionId, isAttend, thisSessionMemberNotes));
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
                SessionModel session = FirebaseSerializer.serializeSession(sessionSnapshot);
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
                } else
                    callback.onError(task.getException().getMessage());
            }
        });
    }


    @Override
    public void deleteSession(String sessoinId, final boolean isOffline, final Delete callback) {
        if (isOffline)
            callback.onDeleted();
        FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(sessoinId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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
        FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(sessionId).child(SessionEntry.KEY_FOR_GROUP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
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
                    GroupModel groupModel = FirebaseSerializer.serializeGroup(groupSnapShot);


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
                        ArrayList<GroupMessageModel> groupMessages = FirebaseSerializer.serializeGroupMessages(groupSnapshot, messagesSnapshot);
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
}

