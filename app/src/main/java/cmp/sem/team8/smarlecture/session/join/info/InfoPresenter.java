package cmp.sem.team8.smarlecture.session.join.info;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;

/**
 * Created by Loai Ali on 4/23/2018.
 */

/**
 * @deprecated
 */

public class InfoPresenter implements InfoContract.Actions {

    private static final String TAG = "InfoPresenter";
    InfoContract.Views mView;
    private String sessionID;
    private String groupID;
    private DatabaseReference mRef;
    private DataService mDataSource;

    public InfoPresenter(InfoContract.Views view, String groupID, String sessionID, DataService dataSource) {
        this.groupID = groupID;
        this.sessionID = sessionID;
        mView = view;
        mView.setPresenter(this);
        mRef = FirebaseDatabase.getInstance().getReference();
        mDataSource = dataSource;
    }

    @Override
    public void start() {
        mDataSource.getJoinedSessionInfo(sessionID, groupID, new DataService.Get<SessionForUserModel>() {
            @Override
            public void onDataFetched(SessionForUserModel data) {
                mView.showSessionInfo(sessionID, data.getOwner().getName(), data.getGroup().getName(), data.getName());
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
        mDataSource.listenForSessionStatus(sessionID, new DataService.Listen<String>(1, 1) {
            @Override
            public void onDataReceived(String dataSnapshot) {
                if (dataSnapshot.equals(DataService.SessionStatus.CLOSED.toString())) {
                    if (mView != null)
                        mView.closeSession(sessionID);
                }
            }
        });


    }
}
