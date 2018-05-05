package cmp.sem.team8.smarlecture.home.newsfeed;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public class NewsFeedPresenter implements NewsFeedContract.Actions {

    private final String USER_ID;
    private DataService mDataSource;
    private NewsFeedContract.Views mView;

    public NewsFeedPresenter(AuthService authService, DataService dataSource, NewsFeedContract.Views newsFeedFragment) {
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

    private class GetSessionsCallback extends DataService.Get<SessionForUserModel> {
        @Override
        public void onDataFetched(SessionForUserModel data) {
            mView.addSession(data);
        }


    }
}
