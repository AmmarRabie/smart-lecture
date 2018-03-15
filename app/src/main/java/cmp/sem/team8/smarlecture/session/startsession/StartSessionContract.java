package cmp.sem.team8.smarlecture.session.startsession;

import android.text.format.Time;

import java.util.List;
import java.util.Map;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.session.attendance.AttendanceContract;

/**
 * Created by ramym on 3/15/2018.
 */

public class StartSessionContract {
    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {
        void showSessionId();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        void startSession( );
    }

}
