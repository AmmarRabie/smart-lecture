package cmp.sem.team8.smarlecture.session.startsession;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by ramym on 3/15/2018.
 */

public class StartSessionPresenter implements StartSessionContract.Actions {


    StartSessionContract.Views mView;
    private static final int minId = 0;
    private static final int maxId = 10000000;
    private DatabaseReference mDatabase;
    private int SessionId;

    public StartSessionPresenter(StartSessionContract.Views view) {
        mView = view;
        mView.setPresenter(this);
        SessionId=-1;

    }
    @Override
    public void start() {

    }

    private Integer generateUniqueId()
    {
        Random rand = new Random(System.currentTimeMillis());
        //get the range, casting to long to avoid overflow problems
        long range = (long)maxId - (long)minId + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * rand.nextDouble());
        Integer randomNumber =  (int)(fraction + minId);

        return randomNumber;
    }

    @Override
    public void startSession() {
        final Integer newID = generateUniqueId();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase = mDatabase.child("sessions").child(newID.toString());

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {

                    Map<String,String> map=new HashMap<String,String>();
                    map.put("status","open");
                    map.put("group","id1");
                    map.put("attendance","closed");
                    mDatabase.setValue(map);   // id of the  that  ownes the session


                    SessionId=newID;
                    mView.showSessionId(newID.toString());
                    mView.sendSessioIdToActivity(newID);

                    DatabaseReference groupref=FirebaseDatabase.getInstance().getReference();
                    groupref=groupref.child("groups").child("id1").child("Sessions").child(newID.toString());
                    groupref.setValue(true);


                }
                else
                {
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

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        ref=ref.child("sessions").child(Integer.toString(SessionId)).child("status");
        ref.setValue("closed");


    }

    public void refresh() {

    }

}
