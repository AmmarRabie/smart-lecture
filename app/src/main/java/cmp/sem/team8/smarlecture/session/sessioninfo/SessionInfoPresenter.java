package cmp.sem.team8.smarlecture.session.sessioninfo;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;

/**
 * Created by ramym on 3/15/2018.
 */

public class SessionInfoPresenter implements SessionInfoContract.Actions {

    private static final int minId = 0;
    private static final int maxId = 10000000;
    private final String GROUP_ID;
    SessionInfoContract.Views mView;
    private DatabaseReference mDatabase;
    private int SessionId;

    public SessionInfoPresenter(SessionInfoContract.Views view, String groupId) {
        GROUP_ID = groupId;
        mView = view;
        mView.setPresenter(this);
        SessionId = -1;
    }


    @Override
    public void start() {

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
        final Integer newID = generateUniqueId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child(SessionEntry.KEY_THIS).child(newID.toString());

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    Map<String, String> map = new HashMap<String, String>();
                    map.put(SessionEntry.KEY_SESSION_STATUS, SessionEntry.SessionStatus.OPEN.toString());
                    map.put(SessionEntry.KEY_FOR_GROUP_ID, GROUP_ID);
                    map.put(SessionEntry.KEY_ATTENDANCE_STATUS, SessionEntry.AttendanceStatus.NOT_ACTIVATED.toString());
                    mDatabase.setValue(map);   // id of the  that  ownes the session
                    SessionId = newID;
                    mView.showSessionId(newID.toString());
                    mView.sendSessioIdToActivity(newID);
                    DatabaseReference groupref = FirebaseDatabase.getInstance().getReference();
                    groupref = groupref.child(GroupEntry.KEY_THIS).child(GROUP_ID).child(GroupEntry.KEY_SESSIONS).child(newID.toString());
                    groupref.setValue(true);


                } else {
                    startSession();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void endSession() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child(SessionEntry.KEY_THIS).child(Integer.toString(SessionId)).child(SessionEntry.KEY_SESSION_STATUS);
        ref.setValue(SessionEntry.AttendanceStatus.CLOSED);
    }


}
