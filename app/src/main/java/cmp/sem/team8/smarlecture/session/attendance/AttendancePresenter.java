package cmp.sem.team8.smarlecture.session.attendance;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Responsible for attendance processes:-
 * <p>
 * get all student names of specific session
 * </P>
 * <p>
 * attend students into specific session
 * </P>
 */
public class AttendancePresenter implements AttendanceContract.Actions {


    private final long sessionId;
    private AttendanceContract.Views mView;
    private boolean isLecturer;


    public AttendancePresenter(AttendanceContract.Views mView, boolean isLecturer, long sessionId) {
        this.mView = mView;
        this.isLecturer = isLecturer;
        this.sessionId = sessionId;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void setAttendance(int index, boolean isAttend) {

    }

    @Override
    public void findAttendanceList(final String secret) {
        DatabaseReference session = FirebaseDatabase.getInstance().getReference("session");
        DatabaseReference thisSession = session.child(String.valueOf(sessionId));
        thisSession.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String corrSecret = dataSnapshot.getValue(String.class);
                if (corrSecret.equals(secret)) {
                    fetchAttendanceListAndPassIt();
                } else
                    mView.showErrorMessage("this secret is incorrect");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void fetchAttendanceListAndPassIt() {

        DatabaseReference thisSession = FirebaseDatabase.getInstance()
                .getReference("session").child(String.valueOf(sessionId));
        thisSession.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child("attendanceList");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void refresh() {

    }

}
