package cmp.sem.team8.smarlecture.grouplist;


/**
 * Created by AmmarRabie on 08/03/2018.
 */


import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * when the presenter start he should look for the current user -unless it is passes earlier-
 * (try to get if from the model), if he can't find the current user
 * he should tell the view to show error and sign in the user so that the view (fragment) get
 * the activity and start the authentication activity
 * which will first find if he can log in and if not, he make a new account
 * and this data returned back to the quickstartstatistics activity in the intent, so this
 * activity in creation should look for this data and if it find it, it should tell the
 * presenter at its creation that he should't look for the user as the user is passes to
 * him at creation
 */
public interface GroupListContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        void showErrorMessage(String cause);

        void showGroupList(ArrayList<HashMap<String, Object>> groupList);

        void onDeleteSuccess(String groupID);
        void onEditSuccess(String groupID,String newName);
        void onAddSuccess(String groupID,String newName);

        void handleOfflineStates();
        boolean getOfflineState();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        void deleteGroup(String groupID);

        void addGroup(String groupName);

        void editGroup(String groupID, String newGroupName);

    }
}
