package cmp.sem.team8.smarlecture.rateobjectives;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.model.ObjectiveModel;
import cmp.sem.team8.smarlecture.session.objectives.ObjectivesContract;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public interface RateObjectivesContract {
    interface Views extends IBaseView<Actions>{

        void showOnErrorMessage(String cause);

        void showObjectivesList(ArrayList<ObjectiveModel> objectiveList);

        void updateSuccess();

        void handleOfflineStates();

        boolean getOfflineState();
    }
    interface Actions extends IBasePresenter {

        void getObjectives();

        void RateObjectives(ArrayList<Float> mUserRatings);
    }
}
