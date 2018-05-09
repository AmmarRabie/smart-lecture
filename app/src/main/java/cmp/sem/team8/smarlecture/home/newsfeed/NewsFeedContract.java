package cmp.sem.team8.smarlecture.home.newsfeed;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public interface NewsFeedContract {


    interface Views extends IBaseView<Actions> {
        /**
         * called when  we want to show all session list in news feed
         * @param sessions
         */
        void showSessions(ArrayList<SessionForUserModel> sessions);

        /**
         *  called to add sessions one by one while news feed fragment open
         * @param session
         */
        void addSession(SessionForUserModel session);
    }

    interface Actions extends IBasePresenter {

    }
}



