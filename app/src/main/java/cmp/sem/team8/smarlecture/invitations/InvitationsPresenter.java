package cmp.sem.team8.smarlecture.invitations;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public class InvitationsPresenter implements InvitationsContract.Actions {

    private final String USER_ID;
    private AppDataSource mDataSource;
    private InvitationsContract.Views mView;

    public InvitationsPresenter(AuthService authService, AppDataSource dataSource, InvitationsContract.Views newsFeedFragment) {
        USER_ID = authService.getCurrentUser().getUserId();
        mDataSource = dataSource;
        mView = newsFeedFragment;
        mView.setPresenter(this);
    }


    @Override
    public void start() {
        // start fetching use active sessions
        mDataSource.getGroupInvitationsForUser(USER_ID,new GetInvitationsCallback());
    }

    @Override
    public void acceptGroup(final String groupId) {
        mDataSource.acceptFollowingGroup(USER_ID, groupId, new AppDataSource.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.removeGroup(groupId);
            }
        });
    }

    @Override
    public void refuseGroup(final String groupId) {
        mDataSource.refuseFollowingGroup(USER_ID, groupId, new AppDataSource.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.removeGroup(groupId);
            }
        });
    }

    private class GetInvitationsCallback extends AppDataSource.Get<GroupInvitationModel> {
        @Override
        public void onDataFetched(GroupInvitationModel data) {
            mView.addGroupInvitation(data);
        }
    }
}
