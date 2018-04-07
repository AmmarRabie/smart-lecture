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
        void showSessionId(String id);
        void sendSessioIdToActivity(int Id);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        void startSession( );
        void endSession();

    }

}
