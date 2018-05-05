package cmp.sem.team8.smarlecture.notification;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupMessageModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public class GroupMessagesPresenter implements GroupMessagesContract.Actions {

    private AppDataSource mDataSource;
    private GroupMessagesContract.Views mView;

    private final String GROUP_ID;

    public GroupMessagesPresenter(AppDataSource dataSource, GroupMessagesContract.Views groupMessagesView, String groupId) {
        mDataSource = dataSource;
        mView = groupMessagesView;
        GROUP_ID = groupId;
        mView.setPresenter(this);
    }


    @Override
    public void start() {
        // start fetching use active sessions
        mDataSource.getGroupMessages(GROUP_ID, new AppDataSource.Get<ArrayList<GroupMessageModel>>() {
            @Override
            public void onDataFetched(ArrayList<GroupMessageModel> groupMessages) {
                mView.showGroupMessages(groupMessages);
            }
        });
    }


}
