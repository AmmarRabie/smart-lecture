package cmp.sem.team8.smarlecture.session.join.rateobjectives;

import android.util.Log;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public class RateObjectivesPresenter implements RateObjectivesContract.Actions {
    private static final String TAG = "RateObjectivesPresenter";
    private final String SESSION_ID;
    private RateObjectivesContract.Views mView;
    private DataService mDataSource;

    public RateObjectivesPresenter(RateObjectivesContract.Views mView, String SesionID, DataService dataSource) {
        this.mView = mView;
        SESSION_ID = SesionID;
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
        getObjectives();
    }

    @Override
    public void getObjectives() {

        mDataSource.getObjectives(SESSION_ID, new DataService.Get<ArrayList<ObjectiveModel>>() {
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
        mDataSource.getObjectives(SESSION_ID, new DataService.Get<ArrayList<ObjectiveModel>>() {
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


                    mDataSource.updateObjectivesRating(SESSION_ID, currentObjective.getmObjectiveID(), newRating, numUsersRated, new DataService.Update() {
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
