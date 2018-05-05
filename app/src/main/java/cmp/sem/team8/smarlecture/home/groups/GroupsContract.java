package cmp.sem.team8.smarlecture.home.groups;


/**
 * Created by AmmarRabie on 08/03/2018.
 */


import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;

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
public interface GroupsContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        /**
         * called when an error happens in presenter
         * @param cause cause of the error
         */
        void showErrorMessage(String cause);

        /**
         * called from the presenter when this fragment is created
         * @param groupList groups made by this user
         */
        void showGroupList(ArrayList<GroupModel> groupList);

        /**
         * called on successful deletion
         * @param groupID the id of the group that's deleted
         */
        void onDeleteSuccess(String groupID);

        /**
         * called on successful edit of a group's name
         * @param groupID the id of the group
         * @param newName the new name of the group
         */
        void onEditSuccess(String groupID, String newName);

        /**
         *called on successful addition of a group
         * @param groupID the id of the added group
         * @param newName the name of the group
         * @param OwnerId the owner of the group
         */
        void onAddSuccess(String groupID, String newName, String OwnerId);

        /**
         * called from the presenter  after  join session is pressed
         * @param sessionId the id of the session
         * @param groupId the id of the group that this session belongs to
         */
        void startJoinSessionView(String sessionId, String groupId);

        void handleOfflineStates();

        boolean getOfflineState();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        /**
         * called from fragment when user clicks on delete button
         * on successful deletion the onDeleteSuccess is called
         * otherwise the showOnErrorMessage method is called
         * @param groupID the id of the group to be deleted
         */
        void deleteGroup(String groupID);

        /**
         * called from fragment when user clicks on add gorup button
         * on successful addition the on addSuccess method is called
         * otherwise the showOnErrorMessage method is called
         * @param groupName the name of the group to be added
         */

        void addGroup(String groupName);

        /**
         * called from the fragment when user clicks on edit group
         * on successful edit the onEditSuccess is called
         * otherwise the showOnErrorMessage is called
         * @param groupID the id of the group that it's name will change
         * @param newGroupName the new name of the group
         */

        void editGroup(String groupID, String newGroupName);

        /**
         * called from fragment when the user clicks on join session
         * @param sessionId the id of the session the user wants to join
         */
        void joinSession(String sessionId);
    }
}
