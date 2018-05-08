package cmp.sem.team8.smarlecture.group.members;


import android.util.Log;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.FileModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;
import cmp.sem.team8.smarlecture.common.io.ExportContext;
import cmp.sem.team8.smarlecture.common.io.ExportTask;
import cmp.sem.team8.smarlecture.common.io.excel.ExcelExportStrategy;

/**
 * Created by Loai Ali on 3/16/2018.
 */

public class MembersPresenter implements MembersContract.Actions {
    private static final String TAG = "MembersPresenter";

    private final String GROUP_ID;
    private final String GROUP_NAME;

    private MembersContract.Views mView;

    private DataService mDataSource;

    public MembersPresenter(DataService dataSource, MembersContract.Views view, final String groupId, final String groupName) {
        mDataSource = dataSource;
        mView = view;
        GROUP_ID = groupId;
        GROUP_NAME = groupName;
        if (groupId == null) {
            Log.e(TAG, "MembersPresenter: group passed as null");
            return;
        }
        mView.setPresenter(this);
    }

    public void start() {
        mView.handleOfflineStates();
        mView.showGroupName(GROUP_NAME);

        mDataSource.getGroupMembers(GROUP_ID, new GetUserCallback());
    }

    @Override
    public void addMember(final String email) {
        if (email == null || email.isEmpty()) {
            mView.showOnErrorMessage("Student must have an email");
            return;
        }
        mDataSource.inviteUserToGroup(email, GROUP_ID, new InviteUserCallback());
    }

    @Override
    public void cancelInvitation(final String memberId) {
        final boolean offlineStateAtRequestTime = mView.getOfflineState();
        if (offlineStateAtRequestTime)
            mView.onRemoveSuccess(memberId);
        mDataSource.refuseFollowingGroup(memberId, GROUP_ID, new DataService.Update() {
            @Override
            public void onUpdateSuccess() {
                if (!offlineStateAtRequestTime)
                    mView.onRemoveSuccess(memberId);
            }
        });
    }

    @Override
    public void removeMember(final String memberId) {
        cancelInvitation(memberId);
    }

    @Override
    public void end() {

    }

    @Override
    public void exportExcel(String fileName) {
        mDataSource.getGroupInfoForExport(GROUP_ID, new ExportExcelCallback(fileName));
    }

    final class ExportExcelCallback extends DataService.Get<FileModel> {
        private String fileName;

        ExportExcelCallback(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public void onDataFetched(FileModel fileData) {
            ExportContext exportContext = new ExportContext(new ExcelExportStrategy());
            exportContext.export(fileData, fileName).addOnSuccessListener(new ExportTask.OnSuccessListener() {
                @Override
                public void onSuccess() {
                    mView.onExportSuccess();
                }
            }).addOnFailureListener(new ExportTask.OnFailureListener() {
                @Override
                public void onFailure() {
                    mView.showOnErrorMessage("Sorry, Can't export this group");
                }
            });
        }
    }

    final class InviteUserCallback extends DataService.Insert<UserModel> {
        @Override
        public void onDataInserted(UserModel user) {
            mView.onAddSuccess(user);
        }

        @Override
        public void onError(String cause) {
            super.onError(cause);
            mView.showOnErrorMessage(cause);
        }

    }

    final class GetUserCallback extends DataService.Get<ArrayList<InvitedUserModel>> {
        @Override
        public void onDataFetched(ArrayList<InvitedUserModel> data) {
            mView.showMembers(data);
        }

        @Override
        public void onError(String cause) {
            mView.showOnErrorMessage(cause);
        }
    }
}
