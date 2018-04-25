package cmp.sem.team8.smarlecture.session.beginattendance;


import com.google.firebase.database.ChildEventListener;
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

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendancePresenter implements BeginAttendanceContract.Actions {
    private final String GROUP_ID;
    BeginAttendanceContract.Views mView;
    DatabaseReference ref;
    boolean taskIsRunning;
    String SessionId;
    List<String> students;
    int maxId = 9999;
    int minId = 0;
    private StudentsNamesAdapter adapter;

    public BeginAttendancePresenter(BeginAttendanceContract.Views view, String groupId, String sessionid) {
        GROUP_ID = groupId;
        taskIsRunning = false;
        mView = view;
        SessionId = sessionid;
        mView.setPresenter(this);

        ref = FirebaseDatabase.getInstance().getReference();

        students = new ArrayList<>();


    }

    @Override
    public void start() {
        //SessionId = getSessionIDFromActivity();
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child(SessionEntry.KEY_THIS).child(SessionId).child(SessionEntry.KEY_SESSION_STATUS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sessionStatus = dataSnapshot.getValue().toString();
                if (sessionStatus.equals(AppDataSource.SessionStatus.OPEN.toString())) {
                    mView.showBeginAttendaceButton();
                    mView.showSecrect(generateRandomSecret());
                    mView.setPresenter(BeginAttendancePresenter.this);


                } else {
                    mView.hideBeginAttendaceButton();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void BeginAttendance() {

        taskIsRunning = true;


        mView.showProgressIndicator(3);
        //   SessionId = getSessionIDFromActivity();

        DatabaseReference nref = ref.child(SessionEntry.KEY_THIS).child(SessionId).child(SessionEntry.KEY_ATTENDANCE_STATUS);
      /*  ref.child(SessionEntry.KEY_THIS).child(SessionId).child(SessionEntry.KEY_SESSION_STATUS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sessionStatus = dataSnapshot.getValue().toString();
                if (sessionStatus.equals(AppDataSource.SessionStatus.OPEN.toString())) {
                    mView.showBeginAttendaceButton();


                } else {
                    mView.hideBeginAttendaceButton();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/


        nref.setValue(AppDataSource.AttendanceStatus.OPEN.toString());

        nref = ref.child(SessionEntry.KEY_THIS).child(SessionId).child(SessionEntry.KEY_SECRET);

        nref.setValue(mView.getSecret());

        nref = FirebaseDatabase.getInstance().getReference();
        nref = nref.child(SessionEntry.KEY_THIS).child(SessionId).child(SessionEntry.KEY_NAMES_LIST);

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

        DatabaseReference nref = ref.child(SessionEntry.KEY_THIS).child(SessionId).child(SessionEntry.KEY_ATTENDANCE_STATUS);
        nref.setValue(AppDataSource.AttendanceStatus.CLOSED);

    }


    private String getSessionIDFromActivity() {
        return ((BeginAttendanceFragment) mView).getActivity().getIntent().getStringExtra("SessionId");
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
