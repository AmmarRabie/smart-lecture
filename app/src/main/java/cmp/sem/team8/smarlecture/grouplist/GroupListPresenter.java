package cmp.sem.team8.smarlecture.grouplist;

import android.util.Log;

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
    public void deleteGroup(String groupID) {
        FirebaseDatabase.getInstance().getReference("groups").child(groupID).removeValue();
    }

    @Override
    public void addGroup(String groupName) {
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
        DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("groups");
        DatabaseReference newGroupRef = groupsRef.push();
        newGroupRef.child("group_owner").setValue(userID);
        newGroupRef.child("name").setValue(groupName);
    }

    @Override
    public void editGroup(String groupID, String newGroupName) {
        if (mCurrentUser == null) {
            mView.showErrorMessage("can't find a user, try to re-login");
            Log.e(TAG, "addGroup: can't find current user");
            return;
        }
        if (newGroupName == null || newGroupName.isEmpty()) {
            mView.showErrorMessage("Group can't have an empty name");
            return;
        }
        deleteGroup(groupID);
        addGroup(newGroupName);
    }

    @Override
    public void start() {
        // fetch the all groups name of this user
        if (mCurrentUser == null) {
            mView.showErrorMessage("can't find a user, try to re-login");
            Log.e(TAG, "addGroup: can't find current user");
            return;
        }
        String id = mCurrentUser.getUid();
        Query userGroups = FirebaseDatabase.getInstance().getReference("groups")
                .orderByChild("group_owner")
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
                mView.showGroupList(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.showErrorMessage(databaseError.getMessage());
            }
        });
    }
}
