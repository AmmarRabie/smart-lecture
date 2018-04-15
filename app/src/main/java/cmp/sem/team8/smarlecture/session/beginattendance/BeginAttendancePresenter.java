package cmp.sem.team8.smarlecture.session.beginattendance;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.common.data.FirebaseContract.*;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendancePresenter implements BeginAttendanceContract.Actions {
    private final String GROUP_ID;
    BeginAttendanceContract.Views mView;
    DatabaseReference ref;
    boolean taskIsRunning;
    int SessionId;
    List<String> students;
    int maxId = 9999;
    int minId = 0;
    private StudentsNamesAdapter adapter;

    public BeginAttendancePresenter(BeginAttendanceContract.Views view, String groupId) {
        GROUP_ID = groupId;
        taskIsRunning = false;
        mView = view;
        mView.setPresenter(this);

        ref = FirebaseDatabase.getInstance().getReference();

        students = new ArrayList<>();


    }

    @Override
    public void start() {
        mView.showSecrect(generateRandomSecret());
    }

    @Override
    public void BeginAttendance() {

        taskIsRunning = true;
        SessionId = getSessionIDFromActivity();
        mView.showProgressIndicator(3);


        DatabaseReference nref = ref.child(SessionEntry.KEY_THIS).child(Integer.toString(SessionId)).child(SessionEntry.KEY_ATTENDANCE_STATUS);

        nref.setValue(SessionEntry.AttendanceStatus.OPEN);

        nref = ref.child(SessionEntry.KEY_THIS).child(Integer.toString(SessionId)).child(SessionEntry.KEY_SECRET);

        nref.setValue(mView.getSecret());

        nref = FirebaseDatabase.getInstance().getReference();
        nref = nref.child(SessionEntry.KEY_THIS).child(Integer.toString(SessionId)).child(SessionEntry.KEY_NAMES_LIST);

        adapter = mView.getStudnetNameAdapter(students);

        mView.listViewSetAdapter(adapter);

        nref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
                mref = mref.child(GroupEntry.KEY_THIS).child(GROUP_ID).child(GroupEntry.KEY_NAMES_LIST).child(dataSnapshot.getKey());

                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            students.add(dataSnapshot.getValue().toString());
                            adapter.updateRecords(students);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mView.showErrorMessage(databaseError.getMessage());
                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mView.showErrorMessage(databaseError.getMessage());
            }
        });

    }

    @Override
    public void endAttendance() {

        DatabaseReference nref = ref.child(SessionEntry.KEY_THIS).child(Integer.toString(SessionId)).child(SessionEntry.KEY_ATTENDANCE_STATUS);
        nref.setValue(SessionEntry.AttendanceStatus.CLOSED);

    }



    private int getSessionIDFromActivity() {
        return ((BeginAttendanceFragment) mView).getActivity().getIntent().getIntExtra("SessionId", 0);
    }

    public boolean isTaskIsRunning() {
        return taskIsRunning;
    }


    private String generateRandomSecret() {
        Integer r1 = (int) (Math.random() * 10); // r1 if from 0->9
        Integer r2 = (int) (Math.random() * 10); // r2 if from 0->9
        Integer r3 = (int) (Math.random() * 10); // r3 if from 0->9
        Integer r4 = (int) (Math.random() * 10); // r4 if from 0->9
        return r1.toString() + r2.toString() + r3.toString() + r4.toString();
    }
}
