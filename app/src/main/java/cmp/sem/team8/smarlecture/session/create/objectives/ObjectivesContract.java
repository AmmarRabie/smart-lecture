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
        void showOnErrorMessage(String cause);

        void onDeleteSuccess(String deletedObjectiveID);

        void onEditSuccess(String objectiveID, String newObjectiveDesription);

        void onAddSuccess(ObjectiveModel addedObjective);

        void showObjectivesList(ArrayList<ObjectiveModel> objectivesList);

        void handleOfflineStates();

        boolean getOfflineState();

        void hideAddObjectiveButton();

    }

    interface Actions extends IBasePresenter {

        void getObjectives();

        void addObjective(String objectiveDescription);

        void editObjective(String objectiveID, String objectiveDescription);

        void deleteObjective(String objectiveID);


    }
}
