package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import java.util.List;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class WriteAttendanceContract {
    interface Views extends IBaseView<WriteAttendanceContract.Actions> {

        void showStudentsList(List<UserAttendanceModel> list);

        void endConnection();

        void startConnection();

        void setTimer(int minutes);

        void showErrorMessage(String cause);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        void getStudentsList(String GroupID, String SessionID);

//        void writeAttendance(int position, String SessionId, String providedSecret);

        void onTimerFinish(int position, String secretProvided);
    }

}
