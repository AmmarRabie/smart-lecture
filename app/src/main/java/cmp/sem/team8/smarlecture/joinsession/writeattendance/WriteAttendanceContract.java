package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionContract;

/**
 * Created by ramym on 3/20/2018.
 */

public class WriteAttendanceContract {
    interface Views extends IBaseView<WriteAttendanceContract.Actions> {
        void showStudents(String id);
        void ListViewSetAdapter(StudentAttendanceAdapter adapter);
        void ListViewSetOnItemClickListener(AdapterView.OnItemClickListener listener);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        void getStudentsList(String GroupID,String SessionID);
        void WriteAttendance();
        StudentAttendanceAdapter getAdapter();

    }

}
