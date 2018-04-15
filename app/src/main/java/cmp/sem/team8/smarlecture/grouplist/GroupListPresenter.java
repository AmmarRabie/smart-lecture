package cmp.sem.team8.smarlecture.grouplist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.common.MapUtils;
import cmp.sem.team8.smarlecture.common.data.FirebaseContract;
import cmp.sem.team8.smarlecture.common.data.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.model.GroupModel;

/**
 * Created by Loai Ali on 3/19/2018.
 */

public class GroupListPresenter implements GroupListContract.Actions {

    private static final String TAG = "GroupListPresenter";

    private FirebaseUser mCurrentUser;

    private GroupListContract.Views mView;


    public GroupListPresenter(GroupListContract.Views grouplistView) {
        mView = grouplistView;
        mView.setPresenter(this);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void deleteGroup(final String groupID) {
        // remove sessions related to this group first and after this remove the group
        getGroupRef(groupID).child(GroupEntry.KEY_SESSIONS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                        String sessionKey = sessionSnapshot.getKey();
                        getSessionRef(sessionKey).removeValue();
                    }
                }
                // remove after ending, this is out of if because we want to delete the group
                // even we don't have any session related to it
                final boolean isOffline = mView.getOfflineState();
                if (isOffline)
                    mView.onDeleteSuccess(groupID);
                getGroupRef(groupID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (mView != null && !isOffline)
                                mView.onDeleteSuccess(groupID);
                        } else {
                            if (mView != null)
                                mView.showErrorMessage(task.getException().getMessage());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mView != null)
                    mView.showErrorMessage("can't delete");
            }
        });

    }

    @Override
    public void addGroup(final String groupName) {
        if (mCurrentUser == null) {
            mView.showErrorMessage("can't find a user, try to re-login");
            Log.e(TAG, "addGroup: can't find current user");
            return;
        }
        String userID = mCurrentUser.getUid();
        if (groupName == null || groupName.isEmpty()) {
            mView.showErrorMessage("Group Must Have a Name");
            return;
        }
        DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference(GroupEntry.KEY_THIS);
        DatabaseReference newGroupRef = groupsRef.push();

        final String groupID = newGroupRef.getKey();

        final boolean isOffline = mView.getOfflineState();
        if (isOffline)
            mView.onAddSuccess(groupID, groupName);
        newGroupRef.child(GroupEntry.KEY_OWNER_ID).setValue(userID);
        newGroupRef.child(GroupEntry.KEY_NAME).setValue(groupName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (mView != null && !isOffline)
                        mView.onAddSuccess(groupID, groupName);
                } else {
                    if (mView != null)
                        mView.showErrorMessage(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void editGroup(final String groupID, final String newGroupName) {
        if (mCurrentUser == null) {
            mView.showErrorMessage("can't find a user, try to re-login");
            Log.e(TAG, "addGroup: can't find current user");
            return;
        }
        if (newGroupName == null || newGroupName.isEmpty()) {
            mView.showErrorMessage("Group can't have an empty name");
            return;
        }

        final boolean isOffline = mView.getOfflineState();
        if (isOffline)
            mView.onEditSuccess(groupID, newGroupName);
        getGroupRef(groupID).child(GroupEntry.KEY_NAME).setValue(newGroupName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (mView != null && !isOffline)
                        mView.onEditSuccess(groupID, newGroupName);
                } else {
                    if (mView != null)
                        mView.showErrorMessage(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void joinSession(final String sessionId) {
        DatabaseReference thisSession = FirebaseDatabase.getInstance()
                .getReference(SessionEntry.KEY_THIS).child(sessionId);
        thisSession.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String groupId = null;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {

                        if (childDataSnapshot.getKey().equals(SessionEntry.KEY_SESSION_STATUS)) {
                            if (childDataSnapshot.getValue().equals(FirebaseContract.SessionEntry.AttendanceStatus.CLOSED.toString())) {
                                mView.showErrorMessage("Session has been closed");
                                return;
                            }
                        } else if (childDataSnapshot.getKey().equals(SessionEntry.KEY_FOR_GROUP_ID)) {
                            groupId = childDataSnapshot.getValue().toString();
                        }
                    }


                    if (groupId != null)
                        mView.startJoinSessionView(sessionId, groupId);

                } else {
                    mView.showErrorMessage("Session not exists");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void start() {
        mView.handleOfflineStates();

        // fetch the all groups name of this user
        if (mCurrentUser == null) {
            mView.showErrorMessage("can't find a user, try to re-login");
            Log.e(TAG, "addGroup: can't find current user");
            return;
        }
        String id = mCurrentUser.getUid();
        Query userGroups = FirebaseDatabase.getInstance().getReference(GroupEntry.KEY_THIS)
                .orderByChild(GroupEntry.KEY_OWNER_ID)
                .equalTo(id);
        userGroups.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, Object>> list = new ArrayList<>();
                for (DataSnapshot groupSnapShot : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists())
                        continue;
                    GroupModel groupModel = groupSnapShot.getValue(GroupModel.class);
                    groupModel.setId(groupSnapShot.getKey());
                    HashMap<String, Object> thisGroup = MapUtils.toHashMap(groupModel);

                    list.add(thisGroup);
                }
                if (mView != null)
                    mView.showGroupList(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mView != null)
                    mView.showErrorMessage(databaseError.getMessage());
            }
        });
    }


    private DatabaseReference getGroupRef(String groupID) {
        return FirebaseDatabase.getInstance().getReference(GroupEntry.KEY_THIS).child(groupID);
    }

    private DatabaseReference getSessionRef(String groupID) {
        return FirebaseDatabase.getInstance().getReference(SessionEntry.KEY_THIS).child(groupID);
    }
}
