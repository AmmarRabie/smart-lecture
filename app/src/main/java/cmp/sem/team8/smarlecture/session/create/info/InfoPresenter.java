package cmp.sem.team8.smarlecture.session.create.info;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by ramym on 3/15/2018.
 */
/**
 * @deprecated
 */
public class InfoPresenter implements InfoContract.Actions {

    private final String GROUP_ID;
    InfoContract.Views mView;
    private DataService dataService;
    private String SESSION_ID;
    private DataService.SessionStatus crrSessionStatus;

    public InfoPresenter(DataService dataService, InfoContract.Views view, String groupId, String sessionID) {
        GROUP_ID = groupId;
        SESSION_ID = sessionID;
        this.dataService = dataService;
        mView = view;
        mView.setPresenter(this);
    }


    @Override
    public void start() {
        startSession();
    }

    private void startSession() {
        dataService.getSessionById(SESSION_ID, new DataService.Get<cmp.sem.team8.smarlecture.common.data.model.SessionModel>() {
            @Override
            public void onDataFetched(cmp.sem.team8.smarlecture.common.data.model.SessionModel session) {
                mView.showSessionId(SESSION_ID);
                switch (session.getSessionStatus()) {
                    case CLOSED:
                        mView.closedSessionView();
                        break;
                    case NOT_ACTIVATED:
                        mView.notActiveSessionView();
                        break;
                    case OPEN:
                        mView.openSessionView();
                        break;
                }
            }
        });
    }

    @Override
    public void openSession() {
        dataService.setSessionStatus(SESSION_ID, DataService.SessionStatus.OPEN, null);
        mView.openSessionView();
    }

    @Override
    public DataService.SessionStatus getSessionStatus() {
        return crrSessionStatus;
    }


    @Override
    public void endSession() {
        dataService.setSessionStatus(SESSION_ID, DataService.SessionStatus.CLOSED, null);
        dataService.setAttendanceStatus(SESSION_ID, DataService.AttendanceStatus.CLOSED, null);
        mView.closedSessionView();
    }


}
