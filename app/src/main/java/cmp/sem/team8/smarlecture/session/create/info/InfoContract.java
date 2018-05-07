package cmp.sem.team8.smarlecture.session.create.info;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.DataService;

/**
 * Created by ramym on 3/15/2018.
 */
/**
 * @deprecated
 */
class InfoContract {
    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {
        //********** showSessionID****************
        // show session id as text in the view
        void showSessionId(String id);

        void closedSessionView();

        void openSessionView();

        void notActiveSessionView();

        //********** SendSessionToActivity ****************
        // send session id generated to the session activity to be used in another fragment within the activity.
        void sendSessioIdToActivity(String Id);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        // on start
        // steps
        // 1- generate unique id
        // 2- add new session to database
        // 3- send session id to CreateSessionActivity.

        void openSession();

        DataService.SessionStatus getSessionStatus();

        /**
         * mark (session.flag=closed)in database;
         */
        void endSession();

    }

}
