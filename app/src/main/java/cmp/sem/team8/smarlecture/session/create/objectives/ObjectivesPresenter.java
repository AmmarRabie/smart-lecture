package cmp.sem.team8.smarlecture.session.create.objectives;

import android.util.Log;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class ObjectivesPresenter implements ObjectivesContract.Actions {

    private static final String TAG = "ObjectiveListPresenter";
    private final String SESSION_ID;
    private ObjectivesContract.Views mView;
    private DataService mDataSource;

    public ObjectivesPresenter(ObjectivesContract.Views mView, String SESSION_ID, DataService dataSource) {

        this.mView = mView;

        mDataSource = dataSource;

        this.SESSION_ID = SESSION_ID;

        if (SESSION_ID == null) {

            Log.e(TAG, "ObjectivesPresenter: Session passed as null");

            return;
        }

        mView.setPresenter(this);

    }

    public void start() {
        mView.handleOfflineStates();
        getObjectives();
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
        if (objectiveDescription == null || objectiveDescription.isEmpty()) {
            mView.showOnErrorMessage("Objective description can't be empty");
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
