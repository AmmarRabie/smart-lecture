package cmp.sem.team8.smarlecture.grouplist;

import cmp.sem.team8.smarlecture.group.groupContract;

/**
 * Created by Loai Ali on 3/19/2018.
 */

public class GroupListPresenter implements GroupListContract.Actions {

    private GroupListContract.Views mView;


    public GroupListPresenter(GroupListFragment grouplistfragment) {

    }

    @Override
    public void deleteGroup(String groupID, String UID) {

    }

    @Override
    public void addGroup(String groupName, String UID) {
            if(groupName==null||groupName.isEmpty()){
                mView.showErrorMessage("Group Must Have a Name");
                return;
            }
            if(UID==null||UID.isEmpty()){
                mView.showErrorMessage("Please Re Login");
                return;
            }


    }

    @Override
    public void editGroup(String groupID, String newGroupName, String UID) {

    }

    @Override
    public void getGroups(String UID) {

    }

    @Override
    public void start() {

    }
}
