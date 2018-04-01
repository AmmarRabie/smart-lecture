package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.model.UserAttendanceModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class WriteAttendancePresenter implements WriteAttendanceContract.Actions {
    private List<UserAttendanceModel> students;
    private ValueEventListener listener = null;
    private String mSessionId;
    private String mGroupId;
    private WriteAttendanceContract.Views mView;
    private boolean mIsAttendanceTimeOver = false;
    private String mSecret = null;
    private boolean mHasNamesList = false;
    private boolean mIsConnectionTimeOver = false;

    public WriteAttendancePresenter(WriteAttendanceContract.Views view) {
        this.mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void getStudentsList(final String groupId, final String sessionId) {

        mSessionId = sessionId;
        mGroupId = groupId;

        DatabaseReference thisSessionRef = FirebaseDatabase.getInstance()
                .getReference("sessions").child(sessionId);

        thisSessionRef.
                child("attendance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String attendanceState = dataSnapshot.getValue(String.class);
                    if (attendanceState.equals("open")) {
                        mView.showErrorMessage("you can't take the attendance while" +
                                " session attendance is open");
                        return;
                    }
                    if (attendanceState.equals("closed")) {
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
                child("attendancesecrect").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mSecret = dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if (listener == null) {
            listener = FirebaseDatabase.getInstance().getReference("sessions").child(mSessionId).
                    child("attendance").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String state = dataSnapshot.getValue(String.class);
                        if (state.equals("open") && mHasNamesList) {
                            // Not-Active -> open; begin of the attendance

                            mView.requestDisableConnection();
                            mView.startAttendanceTimer(1 * 60);
                            mView.startConnectionTimer(15);
                            FirebaseDatabase.getInstance().getReference("sessions")
                                    .child(mSessionId).child("attendance")
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
        mView.showErrorMessage("time end");
        writeAttendance();
    }

    @Override
    public void onConnectionTimeEnd() {
        mIsConnectionTimeOver = true;
        // [TODO] show info mesg instead
        mView.stopConnectionTimer();
        mView.showErrorMessage("attendance time over without closing the connection");
    }


    @Override
    public void onConnectionLost() {
        if (!mIsConnectionTimeOver) {
            mView.stopConnectionTimer();
            // [TODO] tell the view to end dialog
            // [TODO] change to success mesg
            mView.showErrorMessage("bravoooo, you can take your attendance 3la mahlak");
            return;
        }
        mView.showErrorMessage("ent gay t2felow delo2ty ya...");
    }


    @Override
    public void onConnectionBack() {
        // if the connection is back, and i still in the attendance time, error
        if (!mIsAttendanceTimeOver && mIsConnectionTimeOver) {
            mView.showErrorMessage("fat7th leh ya mohaza2 2bl ma alwa2t y5las");
            return;
        }
    }


    private void fetchNamesListAndSendToView() {
        students = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference = reference.child("groups").child(mGroupId).child("namesList");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String name = child.getValue(String.class);
                        students.add(new UserAttendanceModel(name, false));
                    }

                    mView.showStudentsList(students);
                    mHasNamesList = true;
//                mView.endConnection();
//                mView.setTimer(1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void writeAttendance() {
        mView.stopConnectionTimer();
        mView.stopAttendanceTimer();

        if (!verifySecret(mView.getProvidedSecret()))
            return;

        DatabaseReference Sessionreference = FirebaseDatabase.getInstance().getReference();

        Sessionreference = Sessionreference.child("sessions").child(mSessionId).child("namesList")
                .child(Integer.toString(mView.getStudentId()));

        Sessionreference.setValue(true);
    }

    private boolean verifySecret(String secret) {
        boolean isVerified = secret.equals(mSecret);
        if (!isVerified)
            mView.showErrorMessage("this secret isn't correct");
        return isVerified;
    }
}
