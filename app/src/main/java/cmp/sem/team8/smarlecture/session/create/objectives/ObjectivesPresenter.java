package cmp.sem.team8.smarlecture.session.create.objectives;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class ObjectivesPresenter implements ObjectivesContract.Actions {

    private static final String TAG = "ObjectiveListPresenter";

    private ObjectivesContract.Views mView;

    private DataService mDataSource;

    private final String SESSION_ID;

    private DatabaseReference mObjectiveRef;

    public ObjectivesPresenter(ObjectivesContract.Views mView, String SESSION_ID, DataService dataSource) {

        this.mView = mView;

        mDataSource = dataSource;

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

        mDataSource.getObjectives(SESSION_ID, new DataService.Get<ArrayList<ObjectiveModel>>() {
            @Override
            public void onDataFetched(ArrayList<ObjectiveModel> data) {
                mView.showObjectivesList(data);

            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showOnErrorMessage("Wrong Session ID");
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

        mDataSource.insertObjective(SESSION_ID, objectiveDescription, mView.getOfflineState(), new DataService.Insert<ObjectiveModel>() {
            @Override
            public void onDataInserted(ObjectiveModel feedback) {
                mView.onAddSuccess(feedback);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });


    }

    @Override
    public void editObjective(final String objectiveID, final String objectiveDescription) {

        mDataSource.editObjective(objectiveID, SESSION_ID, objectiveDescription, mView.getOfflineState(), new DataService.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.onEditSuccess(objectiveID, objectiveDescription);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });
        if (mObjectiveRef == null)

            return;


    }

    @Override
    public void deleteObjective(final String objectiveID) {
        mDataSource.deleteObjective(objectiveID, SESSION_ID, mView.getOfflineState(), new DataService.Delete() {
            @Override
            public void onDeleted() {
                mView.onDeleteSuccess(objectiveID);
            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }
        });


    }
}
