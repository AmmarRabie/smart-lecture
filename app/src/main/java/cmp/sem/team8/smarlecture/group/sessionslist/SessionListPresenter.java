package cmp.sem.team8.smarlecture.group.sessionslist;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cmp.sem.team8.smarlecture.grouplist.GroupListContract;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.*;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public class SessionListPresenter implements SessionListContract.Actions {
    private static final String TAG = "SessionListPresenter";

    private DatabaseReference mGroupRef;
    private final String GROUP_ID;
    private AppDataSource mDataSource;
    private SessionListContract.Views mView;

    private static final int minId = 0;
    private static final int maxId = 10000000;


    public SessionListPresenter(SessionListContract.Views view, final String groupID, AppDataSource dataSource) {
        mView = view;
        mDataSource = dataSource;
        GROUP_ID = groupID;
        mGroupRef = null;
        if (groupID == null) {
            Log.e(TAG, "StudentListPresenter: group passed as null");
            return;
        }
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.handleOfflineStates();


        FirebaseDatabase.getInstance().getReference(SessionEntry.KEY_THIS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            mGroupRef = FirebaseDatabase.getInstance().
                                    getReference(SessionEntry.KEY_THIS);
                            addSessions();
                        } else {
                            Log.e(TAG, "onDataChange: the Gruop presenter is called with invalid group id");
                            mView.showOnErrorMessage("group doesn't exist");
                            mGroupRef = null;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mGroupRef = null;
                        mView.showOnErrorMessage(databaseError.getMessage());
                    }
                });
    }


    private String generateUniqueId() {
        Random rand = new Random(System.currentTimeMillis());
        //get the range, casting to long to avoid overflow problems
        long range = (long) maxId - (long) minId + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * rand.nextDouble());
        Integer randomNumber = (int) (fraction + minId);

        return randomNumber.toString();
    }


    @Override
    public void addSession(final String sessionName) {
        if (sessionName == null || sessionName.isEmpty())
            mView.showOnErrorMessage("Session can't have an empty name");

        final String sessionID = generateUniqueId();
        mDataSource.addSession(GROUP_ID, sessionID, sessionName, new AppDataSource.Insert<Void>() {
            @Override
            public void onDataInserted(Void feedback) {
                mView.OnAddSuccess(new cmp.sem.team8.smarlecture.common.data.model.SessionModel(sessionID, GROUP_ID, AppDataSource.AttendanceStatus.NOT_ACTIVATED, AppDataSource.SessionStatus.NOT_ACTIVATED, sessionName));
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });

    }

    @Override
    public void editSession(final String sessionName, final String sessionID) {
        if (mGroupRef == null) return;
        if (sessionID == null || sessionID.isEmpty()) {
            mView.showOnErrorMessage("Session doesn't have an ID ");
            return;
        }
        if (sessionName == null || sessionName.isEmpty()) {
            mView.showOnErrorMessage("Session can't have an empty name");
            return;

        }
        mDataSource.editSession(sessionID, sessionName, mView.getOfflineState(), new AppDataSource.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.OnEditSuccess(sessionID, sessionName);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });

    }

    @Override
    public void deleteSession(final String sessionID) {
        if (sessionID == null || sessionID.isEmpty()) {
            mView.showOnErrorMessage("This Session doesn't have an ID");
            return;
        }
        mDataSource.deleteSession(sessionID, mView.getOfflineState(), new AppDataSource.Delete() {
            @Override
            public void onDeleted() {
                mView.OnDeleteSuccess(sessionID);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });
    }

    private void addSessions() {
        if (mGroupRef == null) {
            return;
        }

        mDataSource.getGroupSessions(GROUP_ID, new AppDataSource.Get<ArrayList<cmp.sem.team8.smarlecture.common.data.model.SessionModel>>() {
            @Override
            public void onDataFetched(ArrayList<cmp.sem.team8.smarlecture.common.data.model.SessionModel> data) {
                mView.showSessionsList(data);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });

    }

    @Override
    public void end() {

    }
}
