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
        void showSessions(ArrayList<SessionForUserModel> sessions);
        void addSession(SessionForUserModel session);
    }

    interface Actions extends IBasePresenter {

    }
}



