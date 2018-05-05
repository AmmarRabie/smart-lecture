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

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.util.MapUtils;
import cmp.sem.team8.smarlecture.common.data.model.*;

/**
 * Created by Loai Ali on 3/19/2018.
 */

public class GroupListPresenter implements GroupListContract.Actions {

    private static final String TAG = "GroupListPresenter";

    private FirebaseUser mCurrentUser;

    private GroupListContract.Views mView;
    private AppDataSource mDataSource;


    public GroupListPresenter(GroupListContract.Views grouplistView, AppDataSource dataSource) {
        mView = grouplistView;
        mView.setPresenter(this);
        mDataSource = dataSource;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void deleteGroup(final String groupID) {

        mDataSource.deleteGroupById(groupID, mView.getOfflineState(), new AppDataSource.Delete() {
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
        mDataSource.addGroup(userID, groupName, mView.getOfflineState(), new AppDataSource.Insert<String>() {
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
        mDataSource.updateGroup(groupID, newGroupName, mView.getOfflineState(), new AppDataSource.Update() {
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


        mDataSource.getSessionStatus(sessionId, new AppDataSource.Get<AppDataSource.SessionStatus>() {
            @Override
            public void onDataFetched(AppDataSource.SessionStatus data) {
                if (data == null) {
                    mView.showErrorMessage("Session Doesn't Exist");
                    return;
                }
                if (data.equals(AppDataSource.SessionStatus.CLOSED)) {
                    mView.showErrorMessage("Session has been closed");
                    return;
                }
                mDataSource.getGroupId(sessionId, new AppDataSource.Get<String>() {
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
        mDataSource.getGroupsForUser(id, new AppDataSource.Get<ArrayList<GroupModel>>() {
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
