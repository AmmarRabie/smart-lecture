package cmp.sem.team8.smarlecture.session.create.objectives;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public interface ObjectivesContract {
    interface Views extends IBaseView<Actions> {
        /**
         * show error message when something wrong happen
         * @param cause
         */
        void showOnErrorMessage(String cause);

        /**
         *  view logic to be done if objective was deleted
         * @param deletedObjectiveID
         */
        void onDeleteSuccess(String deletedObjectiveID);

        /**
         * view logic when editing
         * @param objectiveID
         * @param newObjectiveDesription
         */
        void onEditSuccess(String objectiveID, String newObjectiveDesription);

        /**
         *  to show message when objective was added successfully
         * @param
         */
        void onAddSuccess(ObjectiveModel addedObjective);

        /**
         *  to show all objective list
         * @param objectivesList
         */
        void showObjectivesList(ArrayList<ObjectiveModel> objectivesList);

        void handleOfflineStates();

        boolean getOfflineState();

        void hideAddObjectiveButton();

    }

    interface Actions extends IBasePresenter {

        /**
         * to get all objectiviews
         */
        void getObjectives();

        /**
         * to add new objective
         * @param objectiveDescription
         */
        void addObjective(String objectiveDescription);

        /**
         * to edit objective
         * @param objectiveID
         * @param objectiveDescription
         */
        void editObjective(String objectiveID, String objectiveDescription);

        /**
         * logic of delete objective functionality
         * @param objectiveID
         */
        void deleteObjective(String objectiveID);


    }
}
