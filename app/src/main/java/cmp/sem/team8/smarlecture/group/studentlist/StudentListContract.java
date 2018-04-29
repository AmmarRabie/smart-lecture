package cmp.sem.team8.smarlecture.group.studentlist;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;


/**
 * Created by Loai Ali on 3/16/2018.
 */

public interface StudentListContract {

    interface Views extends IBaseView<Actions> {

        void showOnErrorMessage(String cause);

        void onDeleteSuccess(String UID);

        void onAddSuccess(UserModel userAdded);

        void showNamesList(ArrayList<InvitedUserModel> namesList);

        void showGroupName(String groupName);

        void handleOfflineStates();

        boolean getOfflineState();
    }

    interface Actions extends IBasePresenter {
        void addStudent(String email);

        void deleteStudent(String name);

        void end();
    }

}
