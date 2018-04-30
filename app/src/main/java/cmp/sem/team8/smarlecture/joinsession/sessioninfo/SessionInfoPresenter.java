package cmp.sem.team8.smarlecture.joinsession.sessioninfo;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.session.sessioninfo.*;

/**
 * Created by Loai Ali on 4/23/2018.
 */

public class SessionInfoPresenter implements cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoContract.Actions {

    private static final String TAG = "SessionInfoPresenter";
    private String sessionID;
    private String groupID;
    SessionInfoContract.Views mView;
    private DatabaseReference mRef;
    private AppDataSource mDataSource;

    public SessionInfoPresenter(SessionInfoContract.Views view, String groupID, String sessionID, AppDataSource dataSource) {
        this.groupID = groupID;
        this.sessionID = sessionID;
        mView = view;
        mView.setPresenter(this);
        mRef = FirebaseDatabase.getInstance().getReference();
        mDataSource = dataSource;
    }

    @Override
    public void start() {
        mDataSource.getJoinedSessionInfo(sessionID, groupID, new AppDataSource.Get<SessionForUserModel>() {
            @Override
            public void onDataFetched(SessionForUserModel data) {
                mView.showSessionInfo(sessionID, data.getOwnerName(), data.getForGroupName(), data.getSessionName());
            }

            @Override
            public void onDataNotAvailable() {
                Log.e(TAG,
                        "onDataNotAvailable: data of the user is not available," +
                                " can't find the id or user doesn't have name");
                mView.showErrorMessage("There is a problem with your account");
            }

            @Override
            public void onError(String cause) {
                super.onError(cause);
                mView.showErrorMessage(cause);
            }
        });
        mDataSource.listenForsessionStatus(sessionID, new AppDataSource.Listen<String>(1, 1) {
            @Override
            public void onDataReceived(String dataSnapshot) {
                if (dataSnapshot.equals(AppDataSource.SessionStatus.CLOSED.toString())) {
                    if (mView != null)
                        mView.closeSession(sessionID);
                }
            }
        });


    }
}
