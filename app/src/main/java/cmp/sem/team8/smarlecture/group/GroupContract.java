package cmp.sem.team8.smarlecture.group;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;


/**
 * Created by Loai Ali on 3/16/2018.
 */

public interface GroupContract {

    interface Views extends IBaseView<Actions> {
        void showOnSuccess();

        void showErrorMessage(String cause);


        void showNamesList(ArrayList<HashMap<String, Object>> namesList);


    }

    interface Actions extends IBasePresenter {
        void addStudent(String name);

        void editStudent(String studentKey, String newName);

        void deleteStudent(String name);

        void end();
    }

}
