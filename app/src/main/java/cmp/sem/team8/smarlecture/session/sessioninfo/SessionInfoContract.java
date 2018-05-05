package cmp.sem.team8.smarlecture.session.sessioninfo;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by ramym on 3/15/2018.
 */

public class SessionInfoContract {
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

        //********** Start Session  ****************
        // steps
        //1- generate unique id
        //2- add new session to database
        //3- send session id to SessionActivity.
        void startSession();

        void openSession();

        String getSessionStatus();

        //********** End Session ****************
        //mark (session.flag=closed)in database;
        void endSession();

    }

}
