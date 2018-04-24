package cmp.sem.team8.smarlecture.newsfeed;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public class NewsFeedPresenter implements NewsFeedContract.Actions {

    private final String USER_ID;
    private AppDataSource mDataSource;
    private NewsFeedContract.Views mView;

    public NewsFeedPresenter(AuthService authService, AppDataSource dataSource, NewsFeedContract.Views newsFeedFragment) {
        USER_ID = authService.getCurrentUser().getUserId();
//        USER_ID = "CN8UDtwYkmSoiBGVU8clydIW9Ri1";
        mDataSource = dataSource;
        mView = newsFeedFragment;
        mView.setPresenter(this);
    }


    @Override
    public void start() {
        // start fetching use active sessions
        mDataSource.getSessionsForUser(USER_ID,new GetSessionsCallback());
    }

    private class GetSessionsCallback extends AppDataSource.Get<ArrayList<SessionForUserModel>> {
        @Override
        public void onDataFetched(ArrayList<SessionForUserModel> data) {
            mView.showSessions(data);
        }


    }
}
