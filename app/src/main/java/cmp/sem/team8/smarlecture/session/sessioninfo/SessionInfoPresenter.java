package cmp.sem.team8.smarlecture.session.sessioninfo;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by ramym on 3/15/2018.
 */

public class SessionInfoPresenter implements SessionInfoContract.Actions {

    private static final int minId = 0;
    private static final int maxId = 10000000;
    private final String GROUP_ID;
    SessionInfoContract.Views mView;
    private DatabaseReference mDatabase;
    private String SessionId;
    private SessionModel mSession;

    public SessionInfoPresenter(SessionInfoContract.Views view, String groupId, String sessionID) {
        GROUP_ID = groupId;
        mView = view;
        mView.setPresenter(this);
        SessionId = sessionID;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSession = new SessionModel();
    }


    @Override
    public void start() {
        startSession();

        //mDatabase = FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(SessionId);

    }

    private Integer generateUniqueId() {
        Random rand = new Random(System.currentTimeMillis());
        //get the range, casting to long to avoid overflow problems
        long range = (long) maxId - (long) minId + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * rand.nextDouble());
        Integer randomNumber = (int) (fraction + minId);

        return randomNumber;
    }

    @Override
    public void startSession() {


        mDatabase = FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(SessionId);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mSession.setmName(dataSnapshot.child(SessionEntry.KEY_FOR_SESSION_NAME_).getValue(String.class));
                mSession.setmGroupID(dataSnapshot.child(SessionEntry.KEY_FOR_GROUP_ID).getValue(String.class));
                mSession.setmAttendanceStatus(dataSnapshot.child(SessionEntry.KEY_ATTENDANCE_STATUS).getValue(String.class));
                mSession.setmSessionID(SessionId);

                HashMap<String, String> hashedList = (HashMap<String, String>) dataSnapshot.child(SessionEntry.KEY_NAMES_LIST.toString()).getValue();
                if (hashedList != null) {
                    ArrayList<String> students = new ArrayList<>(hashedList.values());
                    mSession.setmStudentsList(students);
                } else {
                    mSession.setmStudentsList(new ArrayList<String>());
                }
                // mSession.setmStudentsList(dataSnapshot.child(SessionEntry.KEY_NAMES_LIST).getValue(ArrayList.class));
                mSession.setmSessionStatus(dataSnapshot.child(SessionEntry.KEY_SESSION_STATUS).getValue(String.class));
                mView.showSessionId(SessionId);
                mView.sendSessioIdToActivity(SessionId);


                //session closed
                if (mSession.getmSessionStatus().equals(AppDataSource.SessionStatus.CLOSED.toString())) {
                    mView.closedSessionView();

                }

                //session open
                else if (mSession.getmSessionStatus().equals(AppDataSource.SessionStatus.OPEN.toString())) {
                    mView.openSessionView();

                }

                //session not activated
                else {
                    mView.notActiveSessionView();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void openSession() {
        mDatabase.child(SessionEntry.KEY_SESSION_STATUS).setValue(AppDataSource.SessionStatus.OPEN.toString());
        mSession.setmSessionStatus(AppDataSource.SessionStatus.OPEN.toString());
        mView.openSessionView();
    }

    @Override
    public String getSessionStatus() {
        return mSession.getmSessionStatus();
    }


    @Override
    public void endSession() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(SessionEntry.KEY_THIS).child(SessionId).child(SessionEntry.KEY_SESSION_STATUS);
        ref.setValue(AppDataSource.SessionStatus.CLOSED.toString());
        ref = FirebaseDatabase.getInstance().getReference().child(SessionEntry.KEY_THIS).child(SessionEntry.KEY_ATTENDANCE_STATUS);
        ref.setValue(AppDataSource.SessionStatus.CLOSED.toString());
        mSession.setmSessionStatus(AppDataSource.SessionStatus.CLOSED.toString());
        mView.closedSessionView();
    }


}
