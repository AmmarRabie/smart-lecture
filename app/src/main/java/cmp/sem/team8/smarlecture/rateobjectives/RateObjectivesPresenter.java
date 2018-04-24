package cmp.sem.team8.smarlecture.rateobjectives;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.model.ObjectiveModel;
import cmp.sem.team8.smarlecture.session.objectives.ObjectivesContract;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public class RateObjectivesPresenter implements RateObjectivesContract.Actions {
 //   public RateObjectivesPresenter(FirebaseAuthService instance, FirebaseRepository instance1, RateObjectivesFragment rateObjectivesFragment) {

   // }


    private static final String TAG = "RateObjectivesPresenter";

    private RateObjectivesContract.Views mView;

    private final String SESSION_ID;

    private DatabaseReference mObjectiveRef;

    public RateObjectivesPresenter(RateObjectivesContract.Views mView,String SesionID){
        this.mView=mView;
        SESSION_ID=SesionID;
        mObjectiveRef=null;
        if(SESSION_ID==null)
        { Log.e(TAG, "RateObjectivesPresenter: Session passed as null");

        return;}
        mView.setPresenter(this);
    }


    @Override
    public void start() {

        FirebaseDatabase.getInstance().getReference(FirebaseContract.SessionEntry.KEY_THIS).child(SESSION_ID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mObjectiveRef = FirebaseDatabase.getInstance().getReference(FirebaseContract.SessionEntry.KEY_THIS).child(SESSION_ID).

                            child(FirebaseContract.SessionEntry.KEY_FOR_OBJECTIVES_LIST);

                    getObjectives();

                } else {

                    Log.e(TAG, "onDataChange: the RateObjective presenter is called with invalid Session id");

                    mView.showOnErrorMessage("Session doesn't exist");

                    mObjectiveRef = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                mObjectiveRef = null;

                mView.showOnErrorMessage(databaseError.getMessage());

            }
        });

    }

    @Override
    public void getObjectives() {
        if (mObjectiveRef == null)

            return;

        mObjectiveRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<ObjectiveModel> objectiveList = new ArrayList<>();

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    if (!dataSnapshot.exists())

                        continue;

                    String key = child.getKey();

                    String name = child.getValue(String.class);

                    float averageRating=child.child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).getValue(float.class);

                    int numberUsersRated=child.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).getValue(int.class);

                    ObjectiveModel thisObjective = new ObjectiveModel();

                    thisObjective.setmObjectiveDescription(name);

                    thisObjective.setmObjectiveID(key);

                    thisObjective.setmNumberofUsersRatedThisObjective(numberUsersRated);

                    thisObjective.setmObjectivesAverageRating(averageRating);

                    thisObjective.setmSessionID(SESSION_ID);

                    objectiveList.add(thisObjective);
                }

                mView.showObjectivesList(objectiveList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                mView.showOnErrorMessage(databaseError.getMessage());

            }
        });



    }

    @Override
    public void RateObjectives(HashMap<String,Object> newObjecivesInformation) {
        for(int i=0;i<newObjecivesInformation.size();i++){
            {


            }
        }
    }
}
