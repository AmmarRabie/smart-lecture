package cmp.sem.team8.smarlecture.session.join.attendance;


import android.util.Log;

import java.util.TimerTask;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class AttendancePresenter implements AttendanceContract.Actions {
    private static final String TAG = "WriteAttendancePresente";

    private final String SESSION_ID;
    private final String USER_ID;

    private AttendanceContract.Views mView;
    private DataService mDataSource;
    private String mSecret = null;

    private boolean isTaskRunning = false;

    // flags to determine states of the user
    private boolean mIsConnectionTimeOver = false;
    private boolean mIsAttendanceTimeOver = false;


    public AttendancePresenter(AttendanceContract.Views view, DataService dataSource, AuthService authService, String sessionId) {
        this.mDataSource = dataSource;
        this.mView = view;
        SESSION_ID = sessionId;
        USER_ID = authService.getCurrentUser().getUserId();
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (isTaskRunning)
            return;
        mView.requestDisableConnection();
        mView.startAttendanceTimer(1 * 60);
        mView.startConnectionTimer(35);

        mDataSource.getSessionById(SESSION_ID, new DataService.Get<SessionModel>() {
            @Override
            public void onDataFetched(SessionModel session) {
                mSecret = session.getSecret();
            }

            @Override
            public void onDataNotAvailable() {
                mView.showErrorMessage("Invalid Id");
            }
        });
        isTaskRunning = true;
    }

    @Override
    public void onAttendanceTimeEnd() {
        mIsAttendanceTimeOver = true;

        mView.stopAttendanceTimer();
        mView.showInfoMessage("time end");

        if (mView.getProvidedSecret().equals(mSecret)) {
            // Mean that the user wait till the attendance timer finish with correct secret
            mView.setInstructionSuccess(4);
            mView.showInfoMessage("open your internet now");
        } else doOnProvidingWrongSecret();
    }

    @Override
    public void onConnectionTimeEnd() {
        mIsConnectionTimeOver = true;
        mView.stopConnectionTimer();
        mView.stopAttendanceTimer();
        doOnNotEndingConnection();
    }


    @Override
    public void onConnectionLost() {
        if (!mIsConnectionTimeOver) {
            mView.stopConnectionTimer();
            doOnEndingConnectionProperly();
        }
    }


    @Override
    public void onConnectionBack() {
        if (!mIsAttendanceTimeOver) {
            mView.stopAttendanceTimer();
            doOnOpeningConnectionBeforeAttendanceTimeEnd();
            return;
        }
        doOnOpeningConnectionAfterAttendanceTimeEnd();
    }

    private void writeAttendanceLocally() {
        mDataSource.setMemberAttendance(SESSION_ID, USER_ID, true, null);
        mView.showSuccessMessage("Your attendance saved locally.");
        mView.setInstructionSuccess(3);
    }

    @Override
    public boolean verifySecret(String secret) {
        if (secret.equals(mSecret)) {
            writeAttendanceLocally();
            mView.setInstructionSuccess(2);
            return true;
        }
        return false;
    }


    /**
     * do tasks for case of "user don't end the connection after time is end"
     */
    private void doOnNotEndingConnection() {
        mView.showErrorMessage("You don't close the connection. Your attendance is ignored");
        mView.endView();
    }

    /**
     * do tasks for case of "user end the connection within the correct time"
     */
    private void doOnEndingConnectionProperly() {
        mView.showSuccessMessage("Connection ended successfully.");
        mView.showInfoMessage("Don't turn on the connection before attendance time over");
        mView.setInstructionSuccess(1);
    }

    /**
     * do tasks for case of "user opened the connection before attendance time is ended"
     */
    private void doOnOpeningConnectionBeforeAttendanceTimeEnd() {
        mView.showErrorMessage("Connection detected. your attendance is ignored");
        // set his attendance false again as we set it when he verify the secret
        mDataSource.setMemberAttendance(SESSION_ID, USER_ID, false, null);
        mView.endView();
    }

    /**
     * do tasks for case of "After providing correct secret, user opened the connection after attendance time is ended"
     */
    private void doOnOpeningConnectionAfterAttendanceTimeEnd() {
        mView.setInstructionSuccess(5);

        // End the view after proper time
        new java.util.Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: " + Thread.currentThread().getName());
                        mView.endView();
                        cancel();
                    }
                },
                5000
        );
    }


    /**
     * do tasks for case of "after attendance time is end, the user provide wrong secret"
     * this case mean that the user can't write his attendance. this user can be one that is out
     * of the lecture
     */
    private void doOnProvidingWrongSecret() {
        mView.showErrorMessage("This secret is not correct. You attendance is ignored");
        mView.endView();
    }
}
