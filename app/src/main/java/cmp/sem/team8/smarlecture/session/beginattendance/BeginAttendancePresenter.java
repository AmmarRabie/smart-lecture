package cmp.sem.team8.smarlecture.session.beginattendance;


import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.AttendeeModel;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendancePresenter implements BeginAttendanceContract.Actions {
    private final String SESSION_ID;
    private ArrayList<AttendeeModel> attendees;
    private AppDataSource.Listen AttendeesListener;

    private BeginAttendanceContract.Views mView;
    private AppDataSource mDataSource;

    public BeginAttendancePresenter(AppDataSource dataSource, BeginAttendanceContract.Views view, String sessionId) {
        mDataSource = dataSource;
        mView = view;
        SESSION_ID = sessionId;
        attendees = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mDataSource.getSessionStatus(SESSION_ID, new AppDataSource.Get<AppDataSource.SessionStatus>() {
            @Override
            public void onDataFetched(AppDataSource.SessionStatus status) {
                if (status.equals(AppDataSource.SessionStatus.OPEN)) {
                    mView.showBeginAttendaceButton();
                    mView.showSecret(generateRandomSecret());
                } else {
                    mView.hideBeginAttendaceButton();
                }
            }
        });
        AttendeesListener = mDataSource.ListenAttendanceList(SESSION_ID, new AppDataSource.Listen<AttendeeModel>() {
            @Override
            public void onDataReceived(AttendeeModel changedAttendee) {
                String id = changedAttendee.getId();
                for (AttendeeModel attendee : attendees)
                    if (attendee.getId().equals(id)) {
                        mView.updateMemberAttendance(id, changedAttendee.isAttend());
                        return;
                    }
                attendees.add(changedAttendee);
                mView.addNewMember(changedAttendee);
            }
        });
    }

    @Override
    public void BeginAttendance() {
        mView.startAttendanceTimer(3);

        mDataSource.setAttendanceStatus(SESSION_ID, AppDataSource.AttendanceStatus.OPEN, null);
        mDataSource.setSessionSecret(SESSION_ID, mView.getSecret(), null);
    }

    @Override
    public void onAttendanceTimerEnd() {
        mDataSource.setAttendanceStatus(SESSION_ID, AppDataSource.AttendanceStatus.CLOSED, null);
    }

    private String generateRandomSecret() {
        Integer r1 = (int) (Math.random() * 10); // r1 if from 0->9
        Integer r2 = (int) (Math.random() * 10); // r2 if from 0->9
        Integer r3 = (int) (Math.random() * 10); // r3 if from 0->9
        Integer r4 = (int) (Math.random() * 10); // r4 if from 0->9
        return r1.toString() + r2.toString() + r3.toString() + r4.toString();
    }


    @Override
    public void onDestroy() {
        mDataSource.forget(AttendeesListener);
    }
}
