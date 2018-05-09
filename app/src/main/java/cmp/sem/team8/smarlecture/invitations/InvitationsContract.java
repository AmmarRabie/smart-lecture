package cmp.sem.team8.smarlecture.invitations;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public interface InvitationsContract {


    interface Views extends IBaseView<Actions> {
        /**
         *  add group invitation  to the list
         * @param session
         */
        void addGroupInvitation(GroupInvitationModel session);

        /** to remove invitaion from the
         *
         * @param groupId
         */
        void removeGroup(String groupId);

        /**
         * to show error message when something went wrong
         * @param message
         */
        void showErrorMessage(String message);
    }

    interface Actions extends IBasePresenter {
        /**
         * called when the user accept group invitation
         * @param groupId group to be accepted
         */

        void acceptGroup(String groupId);
        /**
         * called when the user refuse group invitation
         * @param groupId group to be refused
         */
        void refuseGroup(String groupId);
    }
}



