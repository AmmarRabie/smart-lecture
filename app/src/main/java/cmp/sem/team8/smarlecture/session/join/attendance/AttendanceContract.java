package cmp.sem.team8.smarlecture.session.join.attendance;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by ramym on 3/20/2018.
 */

public class AttendanceContract {
    interface Views extends IBaseView<AttendanceContract.Actions> {

        /**
         * to start attendace timer
         * @param seconds
         */
        void startAttendanceTimer(int seconds);

        /**
         * to start connection timer
         * @param seconds
         */
        void startConnectionTimer(int seconds);

        /**
         * to stop attendace timer
         */
        void stopAttendanceTimer();
        /**
         * to stop connecction timer
         */
        void stopConnectionTimer();

        /**
         *  to show error message
         * @param cause
         */
        void showErrorMessage(String cause);
        /**
         *  to show info message
         * @param info
         */
        void showInfoMessage(String info);

        /**
         * to show coneection
         * @param successMes
         */
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

        /**
         * to check on the secrect
         * @param secret
         * @return
         */
        boolean verifySecret(String secret);

        /**
         * logic to be done when attendance time ends
         * @return
         */

        void onAttendanceTimeEnd();
        /**
         * logic to be done when onConnection time ends
         * @return
         */
        void onConnectionTimeEnd();


        /**
         * logic to be done when connection lost
         */
        void onConnectionLost();

        /**
         * logic to be done when connection is back
         */
        void onConnectionBack();
    }

}
