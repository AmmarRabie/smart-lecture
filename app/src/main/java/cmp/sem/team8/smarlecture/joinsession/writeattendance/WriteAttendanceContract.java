package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import java.util.List;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.UserAttendanceModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class WriteAttendanceContract {
    interface Views extends IBaseView<WriteAttendanceContract.Actions> {

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
