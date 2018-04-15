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

        void startAttendanceTimer(int seconds);
        void startConnectionTimer(int seconds);

        void stopAttendanceTimer();
        void stopConnectionTimer();

        void showErrorMessage(String cause);
        void showInfoMessage(String info);
        void showSuccessMessage(String successMes);

        void requestDisableConnection();

        String getStudentId();
        String getProvidedSecret();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        void getStudentsList(String GroupID, String SessionID);

//        void writeAttendance(int position, String SessionId, String providedSecret);

//        void onTimerFinish(int position, String secretProvided);

        void onAttendanceTimeEnd();
        void onConnectionTimeEnd();


        void onConnectionLost();
        void onConnectionBack();
    }

}
