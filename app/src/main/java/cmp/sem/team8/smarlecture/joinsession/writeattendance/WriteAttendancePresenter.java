package cmp.sem.team8.smarlecture.joinsession.writeattendance;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.model.UserAttendanceModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class WriteAttendancePresenter implements WriteAttendanceContract.Actions {

    private WriteAttendanceContract.Views mView;
    private AppDataSource mDataSource;

    private List<UserAttendanceModel> students;
    private ValueEventListener listener = null;

    private String mSessionId;
    private String mGroupId;


    private String mSecret = null;

    // flags to determine states of the user
    private boolean mIsConnectionTimeOver = false;
    private boolean mIsAttendanceTimeOver = false;
    private boolean mHasNamesList = false;

    public WriteAttendancePresenter(WriteAttendanceContract.Views view, AppDataSource dataSource) {
        this.mView = view;
        mView.setPresenter(this);
        mDataSource = dataSource;
    }

    @Override
    public void start() {

    }

    @Override
    public void getStudentsList(final String groupId, final String sessionId) {

        mSessionId = sessionId;
        mGroupId = groupId;

        final DatabaseReference thisSessionRef = FirebaseDatabase.getInstance()
                .getReference(SessionEntry.KEY_THIS).child(sessionId);

        thisSessionRef.
                child(SessionEntry.KEY_ATTENDANCE_STATUS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String attendanceState = dataSnapshot.getValue(String.class);
                    if (attendanceState.equals(AppDataSource.AttendanceStatus.OPEN.toString())) {
                        mView.showErrorMessage("you can't take the attendance while" +
                                " session attendance is open");
                        return;
                    }
                    if (attendanceState.equals(AppDataSource.AttendanceStatus.CLOSED.toString())) {
                        // [TODO]: this could be removed after adding objectives and questions
                        mView.showErrorMessage("The attendance is already taken");
                        return;
                    }
                    fetchNamesListAndSendToView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        thisSessionRef.
                child(SessionEntry.KEY_SECRET).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mSecret = dataSnapshot.getValue(String.class);
                    thisSessionRef.child(SessionEntry.KEY_SECRET).removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (listener == null) {
            listener = FirebaseDatabase.getInstance().getReference(SessionEntry.KEY_THIS).child(mSessionId).
                    child(SessionEntry.KEY_ATTENDANCE_STATUS).addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String state = dataSnapshot.getValue(String.class);
                        if (state.equals(AppDataSource.AttendanceStatus.OPEN.toString()) && mHasNamesList) {
                            // Not-Active -> open; begin of the attendance

                            mView.requestDisableConnection();
                            mView.startAttendanceTimer(1 * 60);
                            mView.startConnectionTimer(35);
                            FirebaseDatabase.getInstance().getReference(SessionEntry.KEY_THIS)
                                    .child(mSessionId).child(SessionEntry.KEY_ATTENDANCE_STATUS)
                                    .removeEventListener(this);
                            listener = null;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onAttendanceTimeEnd() {
        mIsAttendanceTimeOver = true;

        mView.stopAttendanceTimer();
        mView.showInfoMessage("time end");

        if (verifySecret(mView.getProvidedSecret())) doOnProvidingCorrectSecret();
        else doOnProvidingWrongSecret();
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
        if (!mIsConnectionTimeOver && mHasNamesList) {
            mView.stopConnectionTimer();
            doOnEndingConnectionProperly();
            return;
        }
    }


    @Override
    public void onConnectionBack() {
        if (!mIsAttendanceTimeOver) {
            mView.stopAttendanceTimer();
            doOnOpeningConnectionBeforeAttendanceTimeEnd();
        }
    }


    private void fetchNamesListAndSendToView() {


        mDataSource.getUsersListOfGroupTemp(mGroupId, new AppDataSource.Get<ArrayList<UserAttendanceModel>>() {
            @Override
            public void onDataFetched(ArrayList<UserAttendanceModel> data) {
                mView.showStudentsList(data);
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
            }
        });

    }

    private void writeAttendance() {
        DatabaseReference currSessionRef = FirebaseDatabase.getInstance().getReference();
        currSessionRef = currSessionRef.child(SessionEntry.KEY_THIS).child(mSessionId).child(SessionEntry.KEY_NAMES_LIST)
                .child(mView.getStudentId());
        mView.showSuccessMessage("Your attendance saved locally.");
        mView.showInfoMessage("open your internet now so that lecturer find you");
        currSessionRef.setValue(true);
    }

    private boolean verifySecret(String secret) {
        return secret.equals(mSecret);
    }


    /**
     * do tasks for case of "user don't end the connection after time is end"
     */
    private void doOnNotEndingConnection() {
        mView.showErrorMessage("You don't close the connection. Your attendance is ignored");
    }

    /**
     * do tasks for case of "user end the connection within the correct time"
     */
    private void doOnEndingConnectionProperly() {
        mView.showSuccessMessage("Connection ended successfully.");
        mView.showInfoMessage("Don't turn on the connection before attendance time over");
    }

    /**
     * do tasks for case of "user opened the connection before attendance time is ended"
     */
    private void doOnOpeningConnectionBeforeAttendanceTimeEnd() {
        mView.showErrorMessage("Connection detected. your attendance is ignored");
    }

    /**
     * do tasks for case of "after attendance time is end, the user provide correct secret"
     * this case mean that the user will successfully write his attendance
     */
    private void doOnProvidingCorrectSecret() {
        writeAttendance();
    }

    /**
     * do tasks for case of "after attendance time is end, the user provide wrong secret"
     * this case mean that the user can't write his attendance. this user can be one that is out
     * of the lecture
     */
    private void doOnProvidingWrongSecret() {
        mView.showErrorMessage("This secret is not correct. You attendance is ignored");
    }
}
