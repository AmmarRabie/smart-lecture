package cmp.sem.team8.smarlecture.group.studentlist;


import android.util.Log;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.FileModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;
import cmp.sem.team8.smarlecture.common.io.ExportContext;
import cmp.sem.team8.smarlecture.common.io.ExportTask;
import cmp.sem.team8.smarlecture.common.io.excel.ExcelExportStrategy;

/**
 * Created by Loai Ali on 3/16/2018.
 */

public class StudentListPresenter implements StudentListContract.Actions {
    private static final String TAG = "StudentListPresenter";

    private final String GROUP_ID;
    private final String GROUP_NAME;

    private StudentListContract.Views mView;

    private AppDataSource mDataSource;

    public StudentListPresenter(AppDataSource dataSource, StudentListContract.Views view, final String groupId, final String groupName) {
        mDataSource = dataSource;
        mView = view;
        GROUP_ID = groupId;
        GROUP_NAME = groupName;
        if (groupId == null) {
            Log.e(TAG, "StudentListPresenter: group passed as null");
            return;
        }
        mView.setPresenter(this);
    }

    public void start() {
        mView.handleOfflineStates();
        mView.showGroupName(GROUP_NAME);

        mDataSource.getUsersListOfGroup(GROUP_ID, new GetUserCallback());
    }

    @Override
    public void addStudent(final String email) {
        if (email == null || email.isEmpty()) {
            mView.showOnErrorMessage("Student must have an email");
            return;
        }
        mDataSource.inviteUserToGroup(email, GROUP_ID, new InviteUserCallback());
    }

    @Override
    public void deleteStudent(final String studentKey) {
        mDataSource.refuseFollowingGroup(studentKey, GROUP_ID, new AppDataSource.Update() {
            @Override
            public void onUpdateSuccess() {
                mView.onDeleteSuccess(studentKey);
            }
        });
    }

    @Override
    public void end() {
/*        for (int i = 0; i < valueEventListeners.size(); i++)
            mGroupRef.child("namesList").removeEventListener(valueEventListeners.get(i));*/
    }

    @Override
    public void exportExcel(String fileName) {
        mDataSource.getGroupInfoForExport(GROUP_ID, new ExportExcelCallback(fileName));
    }

    final class ExportExcelCallback extends AppDataSource.Get<FileModel> {
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

    final class InviteUserCallback extends AppDataSource.Insert<UserModel> {
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

    final class GetUserCallback extends AppDataSource.Get<ArrayList<InvitedUserModel>> {
        @Override
        public void onDataFetched(ArrayList<InvitedUserModel> data) {
            mView.showNamesList(data);
        }

        @Override
        public void onError(String cause) {
            mView.showOnErrorMessage(cause);
        }
    }
}
