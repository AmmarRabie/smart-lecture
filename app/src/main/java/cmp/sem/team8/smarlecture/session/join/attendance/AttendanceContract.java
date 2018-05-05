package cmp.sem.team8.smarlecture.session.join.attendance;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by ramym on 3/20/2018.
 */

public class AttendanceContract {
    interface Views extends IBaseView<AttendanceContract.Actions> {

        void startAttendanceTimer(int seconds);

        void startConnectionTimer(int seconds);

        void stopAttendanceTimer();

        void stopConnectionTimer();

        void showErrorMessage(String cause);

        void showInfoMessage(String info);

        void showSuccessMessage(String successMes);

        void requestDisableConnection();

        String getProvidedSecret();

        void setInstructionSuccess(int instruction);

        void endView();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {
        boolean verifySecret(String secret);

        void onAttendanceTimeEnd();

        void onConnectionTimeEnd();


        void onConnectionLost();

        void onConnectionBack();
    }

}
