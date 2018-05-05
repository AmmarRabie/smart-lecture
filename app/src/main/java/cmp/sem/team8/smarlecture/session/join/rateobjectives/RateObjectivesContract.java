package cmp.sem.team8.smarlecture.session.join.rateobjectives;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public interface RateObjectivesContract {
    interface Views extends IBaseView<Actions> {

        /**
         * called from the presenter when there's an error
         * @param cause the cause of the error
         */
        void showOnErrorMessage(String cause);

        /**
         * called from the presenter at the beginning ,it  shows the user the objectives
         * @param objectiveList the list of the objectives that the belong to one session
         */
        void showObjectivesList(ArrayList<ObjectiveModel> objectiveList);

        /**
         * called when the user's rate of objectives is successfully done
         */
        void updateSuccess();

        void handleOfflineStates();

        boolean getOfflineState();
    }

    interface Actions extends IBasePresenter {
        /**
         * called from the fragment at the beginning
         */
        void getObjectives();

        /**
         * called from the fragment when the user submits his rating to the objectives and
         * clicks on rate objectives
         * @param mUserRatings a list of user ratings to the objectives
         */
        void RateObjectives(ArrayList<Float> mUserRatings);
    }
}
