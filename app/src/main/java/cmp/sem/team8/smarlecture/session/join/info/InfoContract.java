package cmp.sem.team8.smarlecture.session.join.info;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by Loai Ali on 4/23/2018.
 */

public interface InfoContract {
    interface Views extends IBaseView<Actions> {
        /**
         * called from the presenter when the user joins a session successfully
         * @param sesisonID the id of the session user entered
         * @param groupOwner the owner of this session
         * @param groupName the group name
         * @param sessionName the session name
         */
        void showSessionInfo(String sesisonID, String groupOwner, String groupName, String sessionName);

        /**
         * called from the presenter when there's an error
         * @param Cause
         */
        void showErrorMessage(String Cause);

        /**
         * called from the presenter when the session status is set to closed
         * @param sessionID
         */
        void closeSession(String sessionID);
    }

    interface Actions extends IBasePresenter {


    }


}
