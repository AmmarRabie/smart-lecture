package cmp.sem.team8.smarlecture.group.sessions;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public interface SessionsContract {
    interface Views extends IBaseView<SessionsContract.Actions> {
        /**
         * @param cause error to be shown
         */
        void showOnErrorMessage(String cause);

        /**
         * @param sessionslist arraylist containing the sessions of this group
         */
        void showSessionsList(ArrayList<SessionModel> sessionslist);

        /**
         * @param deletedSessionId the deleted
         *                         called on successful deletion of a session
         */
        void OnDeleteSuccess(String deletedSessionId);

        /**
         * @param addedSessionName Added session name
         *                         called on successful addition of a session
         */
        void OnAddSuccess(SessionModel addedSessionName);

        /**
         * @param sessionID   the id of the session
         * @param sessionName the new name of the session
         *                    called on successful edit of a session
         */

        void OnEditSuccess(String sessionID, String sessionName);

        void handleOfflineStates();

        boolean getOfflineState();

    }

    interface Actions extends IBasePresenter {
        /**
         * @param sessionName The name of the session that will be added
         *                    on successful addition the onAddSuccess method is called
         *                    on unSuccessful addition the onErrorMessage method is called
         */
        void addSession(String sessionName);

        /**
         * @param sessionName the new name of the session
         * @param sessionID   the id of the session
         *                    on successful edit the onEditSuccess method is called
         *                    on unSuccessful addition the onErrorMessage method is called
         */
        void editSession(String sessionName, String sessionID);

        /**
         * @param sessionID the id of the session
         *                  on successful deletion the onRemoveSuccess method is called
         *                  on unSuccessful deletion the onErrorMessage method is called
         */

        void deleteSession(String sessionID);

        void end();
    }
}
