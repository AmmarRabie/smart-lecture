package cmp.sem.team8.smarlecture.session.attendance;


import java.util.List;
import java.util.Map;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class AttendanceContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        void showOnCompleteFetchDataMessage();

        void showErrorMessage(String cause);

        void showProgressIndicator();

        void hideProgressIndicator();

        void showAttendanceList(List<Map> attendanceList);

        boolean getConnectionState(boolean includeInternet, boolean includeSim);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

         void setAttendance(int index, boolean isAttend);

        void findAttendanceList(String secret);

        void refresh();
    }
}
