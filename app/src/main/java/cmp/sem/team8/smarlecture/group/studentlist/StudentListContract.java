package cmp.sem.team8.smarlecture.group.studentlist;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;


/**
 * Created by Loai Ali on 3/16/2018.
 */

public interface StudentListContract {

    interface Views extends IBaseView<Actions> {

        void showOnErrorMessage(String cause);

        void onDeleteSuccess(String UID);

        void onEditSuccess(String UID, String newName);

        void onAddSuccess(String UID, String name);

        void showNamesList(ArrayList<HashMap<String, Object>> namesList);

        void showGroupName(String groupName);

        void handleOfflineStates();

        boolean getOfflineState();
    }

    interface Actions extends IBasePresenter {
        void addStudent(String name);

        void editStudent(String studentKey, String newName);

        void deleteStudent(String name);

        void end();
    }

}
