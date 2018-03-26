package cmp.sem.team8.smarlecture.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;


/**
 * Created by Loai Ali on 3/16/2018.
 */

public interface groupContract {

    interface  Views extends IBaseView<Actions>{
        void showOnSuccess();

        void showErrorMessage(String cause);


        void showNamesList(ArrayList<HashMap<String,Object>> namesList);



    }

    interface Actions extends IBasePresenter {
      void addStudent(String name,String groupID);
      void editStudent(String oldName,String  newName,String groupID);
      void deleteStudent(String name,String groupID);
      void getStudents(String groupID);
    }

}
