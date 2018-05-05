package cmp.sem.team8.smarlecture.rateobjectives;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.model.ObjectiveModel;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public class RateObjectivesPresenter implements RateObjectivesContract.Actions {
    private static final String TAG = "RateObjectivesPresenter";

    private RateObjectivesContract.Views mView;

    private final String SESSION_ID;

    private AppDataSource mDataSource;

    private DatabaseReference mObjectiveRef;

    public RateObjectivesPresenter(RateObjectivesContract.Views mView, String SesionID, AppDataSource dataSource) {
        this.mView = mView;
        SESSION_ID = SesionID;
        mObjectiveRef = null;
        if (SESSION_ID == null) {
            Log.e(TAG, "RateObjectivesPresenter: Session passed as null");

            return;
        }
        mView.setPresenter(this);
        mDataSource = dataSource;
    }


    @Override
    public void start() {
        mView.handleOfflineStates();

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

        mDataSource.getObjectives(SESSION_ID, new AppDataSource.Get<ArrayList<ObjectiveModel>>() {
            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }

            @Override
            public void onDataFetched(ArrayList<ObjectiveModel> data) {
                mView.showObjectivesList(data);

            }
        });


    }

    @Override
    public void RateObjectives(final ArrayList<Float> mUserRatings) {
        mDataSource.getObjectives(SESSION_ID, new AppDataSource.Get<ArrayList<ObjectiveModel>>() {
            @Override
            public void onDataFetched(final ArrayList<ObjectiveModel> data) {
                for (int i = 0; i < data.size(); i++) {
                    ObjectiveModel currentObjective = data.get(i);
                    int numUsersRated = currentObjective.getmNumberofUsersRatedThisObjective();

                    float averageRating = currentObjective.getmObjectivesAverageRating();

                    float totalRatings = numUsersRated * averageRating;

                    totalRatings += mUserRatings.get(i);

                    numUsersRated++;

                    float newRating = totalRatings / numUsersRated;


                    mDataSource.updateObjectivesRating(SESSION_ID, currentObjective.getmObjectiveID(), newRating, numUsersRated, new AppDataSource.Update() {
                        @Override
                        public void onUpdateSuccess() {

                        }
                    });
                }
                mView.updateSuccess();
            }
        });

    }
}
