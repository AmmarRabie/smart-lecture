package cmp.sem.team8.smarlecture.session.beginattendance;

import android.text.format.Time;

import java.util.List;
import java.util.Map;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendanceContract {

    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {


        void showErrorMessage(String cause);

        void showProgressIndicator(int minutes);

        void showSecrect(String secret);

        void listViewSetAdapter(StudentsNamesAdapter adapter);

        String getSecret();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        void BeginAttendance();
        void endAttendance();

        boolean isTaskIsRunning();

        void refresh();
    }
}
