package cmp.sem.team8.smarlecture.joinsession.sessioninfo;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by Loai Ali on 4/23/2018.
 */

public interface SessionInfoContract {
    interface Views extends IBaseView<Actions> {
        void showSessionInfo(String sesisonID,String groupOwner,String groupName,String sessionName);
        void showErrorMessage(String Cause);
        void closeSession(String sessionID);
    }

    interface Actions extends IBasePresenter {





    }


}
