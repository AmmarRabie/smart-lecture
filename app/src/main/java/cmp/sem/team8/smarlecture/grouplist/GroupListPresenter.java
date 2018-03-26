package cmp.sem.team8.smarlecture.grouplist;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmp.sem.team8.smarlecture.group.groupContract;

/**
 * Created by Loai Ali on 3/19/2018.
 */

public class GroupListPresenter implements GroupListContract.Actions {

    private GroupListContract.Views mView;


    public GroupListPresenter(GroupListContract.Views view) {
        mView=view;
        mView.setPresenter(this);

    }

    @Override
    public void deleteGroup(String groupID, String UID) {
        FirebaseDatabase.getInstance().getReference("groups").child(groupID).removeValue();

    }

    @Override
    public void addGroup(String groupName, String UID) {
            if(groupName==null||groupName.isEmpty()){
                mView.showErrorMessage("Group Must Have a Name");
                return;
            }
            if(UID==null||UID.isEmpty()){
                mView.showErrorMessage("Please Re-Login");
                return;
            }
        DatabaseReference thisGroup= FirebaseDatabase.getInstance().getReference("groups");
            thisGroup.push().setValue(groupName);
            thisGroup.child("groupOwner").push().setValue(UID);



    }

    @Override
    public void editGroup(String groupID, String newGroupName, String UID) {
        if(newGroupName==null||newGroupName.isEmpty()){
            mView.showErrorMessage("Group can't have an empty name");
            return;
        }
        if(UID==null||UID.isEmpty()){
            mView.showErrorMessage("Please Re-Login");
            return;
        }
        deleteGroup(groupID,UID);
        addGroup(newGroupName,UID);

    }

    @Override
    public void getGroups(String UID) {

    }

    @Override
    public void start() {

    }
}
