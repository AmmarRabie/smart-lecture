package cmp.sem.team8.smarlecture.session.join.info;

import android.util.Log;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;

/**
 * Created by Loai Ali on 4/23/2018.
 */


public class InfoPresenter implements InfoContract.Actions {

    private static final String TAG = "InfoPresenter";
    InfoContract.Views mView;
    private String sessionID;
    private String groupID;
    private DataService mDataSource;

    public InfoPresenter(InfoContract.Views view, String groupID, String sessionID, DataService dataSource) {
        this.groupID = groupID;
        this.sessionID = sessionID;
        mView = view;
        mView.setPresenter(this);
        mDataSource = dataSource;
    }

    @Override
    public void start() {
        mDataSource.getJoinedSessionInfo(sessionID, groupID, new DataService.Get<SessionForUserModel>() {
            @Override
            public void onDataFetched(SessionForUserModel data) {
                mView.showSessionInfo(sessionID, data.getOwner().getName(), data.getGroup().getName(), data.getName());
                if(data.getSessionStatus()== DataService.SessionStatus.OPEN){
                    mDataSource.listenSessionStatus(sessionID, new DataService.Listen<DataService.SessionStatus>() {
                        @Override
                        public void onDataReceived(DataService.SessionStatus sessionStatus) {
                            if (sessionStatus.equals(DataService.SessionStatus.CLOSED)) {
                                if (mView != null) {
                                    mDataSource.getObjectivesCount(sessionID, new DataService.Get<Long>() {
                                        @Override
                                        public void onDataFetched(Long data) {
                                            if(data>0)
                                                mView.openRateObjectives(sessionID);
                                            else
                                                mView.closeSession();
                                        }

                                        @Override
                                        public void onError(String cause) {
                                            mView.showErrorMessage(cause);
                                        }
                                    });

                                }
                            }
                        }
                    });

                }
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




    }
}
