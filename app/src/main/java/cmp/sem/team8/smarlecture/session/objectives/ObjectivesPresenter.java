package cmp.sem.team8.smarlecture.session.objectives;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.model.ObjectiveModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class ObjectivesPresenter implements ObjectivesContract.Actions {

    private static final String TAG = "ObjectiveListPresenter";

    private ObjectivesContract.Views mView;

    private final String SESSION_ID;

    private DatabaseReference mObjectiveRef;

    public ObjectivesPresenter(ObjectivesContract.Views mView, String SESSION_ID) {

        this.mView = mView;

        this.SESSION_ID = SESSION_ID;

        mObjectiveRef = null;

        if (SESSION_ID == null) {

            Log.e(TAG, "ObjectivesPresenter: Session passed as null");

            return;
        }

        mView.setPresenter(this);

    }

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

                    Log.e(TAG, "onDataChange: the Objective presenter is called with invalid Session id");

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
    public void addObjective(final String objectiveDescription) {

        if (mObjectiveRef == null) {

            Log.e(TAG, "addSObjective: called without finding the Session");

            return;
        }

        if (objectiveDescription == null || objectiveDescription.isEmpty()) {

            mView.showOnErrorMessage("Objective desciption can't be empty");

            return;
        }

        DatabaseReference newObjective = mObjectiveRef.push();

        final String key = newObjective.getKey();

        final boolean isOffline = mView.getOfflineState();

        if (isOffline) {

            ObjectiveModel addedObjective = new ObjectiveModel();

            addedObjective.setmSessionID(SESSION_ID);

            addedObjective.setmObjectiveID(key);

            addedObjective.setmObjectiveDescription(objectiveDescription);

            addedObjective.setmObjectivesAverageRating(0);

            addedObjective.setmNumberofUsersRatedThisObjective(0);

            mView.onAddSuccess(addedObjective);
        }
        newObjective.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).setValue(0);

        newObjective.child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).setValue(0);

        newObjective.setValue(objectiveDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    if (!isOffline) {

                        ObjectiveModel addedObjective = new ObjectiveModel();

                        addedObjective.setmSessionID(SESSION_ID);

                        addedObjective.setmObjectiveID(key);

                        addedObjective.setmObjectiveDescription(objectiveDescription);

                        addedObjective.setmNumberofUsersRatedThisObjective(0);

                        addedObjective.setmObjectivesAverageRating(0);


                        mView.onAddSuccess(addedObjective);

                    }
                } else {

                    mView.showOnErrorMessage(task.getException().getMessage());
                }
            }
        });


    }

    @Override
    public void editObjective(String objectiveID, String objectiveDescription) {

        if (mObjectiveRef == null)

            return;


    }

    @Override
    public void deleteObjective(String objectiveID) {

    }
}
