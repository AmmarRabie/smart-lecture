package cmp.sem.team8.smarlecture.session.beginattendance;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.AttendeeModel;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendanceContract {

    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        /**
         * to show message error message
         */
        void showErrorMessage(String cause);

        void showBeginAttendaceButton();

        void hideBeginAttendaceButton();

        /**
         * to show the timer of the attendance in the fragment;
         */
        void startAttendanceTimer(int minutes);


        /**
         * to show attendance secret code
         */
        void showSecret(String secret);

        String getSecret();

        void addNewMember(AttendeeModel newAttendee);

        void updateMemberAttendance(String id, boolean attend);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        /**
         * steps
         * 1- make attendance secrect
         * 2- put listener on session object in the database
         * if any new student mark himself as attendant
         * thi listView in the fragment will show his name
         */
        void BeginAttendance();

        /**
         * mark attendance flage in the database as closed;
         */
        void onAttendanceTimerEnd();

        void onDestroy();
    }
}
