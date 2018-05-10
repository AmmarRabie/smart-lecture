package cmp.sem.team8.smarlecture.group.members;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;


/**
 * Created by Loai Ali on 3/16/2018.
 */

public interface MembersContract {

    interface Views extends IBaseView<Actions> {
        /**
         * called when the presenter can't do any operation
         *
         * @param cause the error
         */

        void showOnErrorMessage(String cause);

        /**
         * called on successful deletion of a student from the current group
         *
         * @param UID the id of the student
         */
        void onRemoveSuccess(String UID);

        /**
         * called on successful addition of a student to the current group
         *
         * @param userAdded an usermodel of the added student
         */

        void onAddSuccess(UserModel userAdded);

        /**
         * called when the fragment is created it shows the names of the student that accepted the
         * the invitaion to the group
         *
         * @param namesList an ararylist of the students at the current group
         */

        void showMembers(ArrayList<InvitedUserModel> namesList);

        /**
         * called when the fragment is created to set the title of the activity to group name
         *
         * @param groupName the current group name
         */
        void showGroupName(String groupName);

        void handleOfflineStates();

        boolean getOfflineState();

        /**
         * called when export process of file has been succsedd to do
         * some view logic as print message to the user
         */
        void onExportSuccess();
    }

    interface Actions extends IBasePresenter {
        /**
         * called by the fragment to add a student to a group
         * on successful addition the onAddSuccess is called
         *
         * @param email the email of the student to be added to group
         */
        void addMember(String email);

        /**
         * called by fragment to cancel invitation
         *
         * @param memberId the name of the student to cancel hi invation
         */

        void cancelInvitation(String memberId);

        /**
         * called by fragment to delete a student from a group
         * on successfull deletion the onDelteSuccess is called
         * on unSuccessful deletion the onErrorMessage is valed
         *
         * @param memberId the name of the student to be deleted
         */
        void removeMember(String memberId);

        void end();

        /**
         * called when we want to export group information to excel file
         *
         * @param fileName the name of the student to be deleted
         */
        void exportExcel(String fileName);

        void notifyMembers(String message);
    }

}
