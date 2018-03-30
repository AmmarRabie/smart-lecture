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
    List<UserAttendanceModel> students;
    ValueEventListener listener = null;
    private String mSessionId;
    private String mGroupId;
    private WriteAttendanceContract.Views mView;
    private String mSecret = null;
    //    private int studentPos = -1;
    private boolean mHasNamesList = false;

    public WriteAttendancePresenter(WriteAttendanceContract.Views mView) {
        this.mView = mView;
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
                            mView.setTimer(1);
                            mView.endConnection();
                            FirebaseDatabase.getInstance().getReference("sessions")
                                    .child(mSessionId).child("attendance")
                                    .removeEventListener(this);
                            listener = null;
                        } else if (state.equals("not-activated")) {

                        } else if (state.equals("closed")) {
//                            mView.startConnection();

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

/*    @Override
    public void writeAttendance(int position, String SessionId, String providedSecret) {
        if (providedSecret.equals(mSecret)) {
            mView.showErrorMessage("this secret is not correct");
            return;
        }

        studentPos = position;

    }*/

    @Override
    public void onTimerFinish(int studentPos, String providedSecret) {
        mView.startConnection();

        if (!verifySecret(providedSecret))
            return;

        DatabaseReference Sessionreference = FirebaseDatabase.getInstance().getReference();

        Sessionreference = Sessionreference.child("sessions").child(mSessionId).child("namesList")
                .child(Integer.toString(studentPos + 1));

        Sessionreference.setValue(true);
    }


    private boolean verifySecret(String secret) {
        boolean isVerified = secret.equals(mSecret);
        if (!isVerified)
            mView.showErrorMessage("this secret isn't correct");
        return isVerified;
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

}
