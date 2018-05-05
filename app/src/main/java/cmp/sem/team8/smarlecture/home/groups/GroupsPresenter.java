package cmp.sem.team8.smarlecture.home.groups;


import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.model.*;

/**
 * Created by Loai Ali on 3/19/2018.
 */

public class GroupsPresenter implements GroupsContract.Actions {

    private static final String TAG = "GroupsPresenter";

    private FirebaseUser mCurrentUser;

    private GroupsContract.Views mView;
    private DataService mDataSource;


    public GroupsPresenter(GroupsContract.Views grouplistView, DataService dataSource) {
        mView = grouplistView;
        mView.setPresenter(this);
        mDataSource = dataSource;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void deleteGroup(final String groupID) {

        mDataSource.deleteGroupById(groupID, mView.getOfflineState(), new DataService.Delete() {
            @Override
            public void onDeleted() {
                mView.onDeleteSuccess(groupID);
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
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
        final String userID = mCurrentUser.getUid();
        if (groupName == null || groupName.isEmpty()) {
            mView.showErrorMessage("Group Must Have a Name");
            return;
        }
        mDataSource.addGroup(userID, groupName, mView.getOfflineState(), new DataService.Insert<String>() {
            @Override
            public void onDataInserted(String feedback) {
                mView.onAddSuccess(feedback, groupName, userID);
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
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
        mDataSource.updateGroup(groupID, newGroupName, mView.getOfflineState(), new DataService.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.onEditSuccess(groupID, newGroupName);
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
            }
        });
    }

    @Override
    public void joinSession(final String sessionId) {


        mDataSource.getSessionStatus(sessionId, new DataService.Get<DataService.SessionStatus>() {
            @Override
            public void onDataFetched(DataService.SessionStatus data) {
                if (data == null) {
                    mView.showErrorMessage("Session Doesn't Exist");
                    return;
                }
                if (data.equals(DataService.SessionStatus.CLOSED)) {
                    mView.showErrorMessage("Session has been closed");
                    return;
                }
                mDataSource.getGroupId(sessionId, new DataService.Get<String>() {
                    @Override
                    public void onDataFetched(String data) {
                        mView.startJoinSessionView(sessionId, data);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        mView.showErrorMessage("Session doesn't belong to a group");
                    }

                    @Override
                    public void onError(String cause) {
                        mView.showErrorMessage(cause);
                    }
                });


            }

            @Override
            public void onDataNotAvailable() {
                mView.showErrorMessage("Session Doesn't Exist");
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
        mDataSource.getGroupsForUser(id, new DataService.Get<ArrayList<GroupModel>>() {
            @Override
            public void onDataFetched(ArrayList<GroupModel> data) {
                mView.showGroupList(data);
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
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
