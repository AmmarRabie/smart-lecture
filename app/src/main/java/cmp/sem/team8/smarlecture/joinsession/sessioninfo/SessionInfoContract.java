package cmp.sem.team8.smarlecture.joinsession.sessioninfo;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by Loai Ali on 4/23/2018.
 */

public interface SessionInfoContract {
    interface Views extends IBaseView<Actions> {
        //********** showSessionID****************
        // show session id as text in the view
        void showSessionId(String id);

        void closedSessionView();

        void openSessionView();

        void notActiveSessionView();
    }

    interface Actions extends IBasePresenter {



        SessionModel getSession();




    }


}
