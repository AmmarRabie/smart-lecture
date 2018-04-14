package cmp.sem.team8.smarlecture.session.beginattendance;

import android.support.v4.app.FragmentActivity;
import android.text.format.Time;

import java.util.List;
import java.util.Map;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.session.SessionActivity;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendanceContract {

    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        //********** show error message  ****************
        //to show message error message
        void showErrorMessage(String cause);

        //********** show  progress indicator  ****************
        //to show the timer of the attendance in the fragment;
        void showProgressIndicator(int minutes);

        //********** show secrect  ****************
        //to show attendance secrect code;
        void showSecrect(String secret);

        //********** list view set adapter   ****************
        //set adapter of the list view
        // adapter: to store the elements of the list view
        void listViewSetAdapter(StudentsNamesAdapter adapter);

        String getSecret();

        //********** get Student Name adapter   ****************
        //to return studentAdapter to the presenter from the fragment
        StudentsNamesAdapter getStudnetNameAdapter(List<String> students);
    }



    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        //********** begin attendance ****************
        //steps
        // 1- make attendance secrect
        // 2- put listener on session object in the database
        //  if any new student mark himself as attendant
        // thi listview in the fragment will show his name
        void BeginAttendance();

        //********** end attendance ****************
        // mark attendance flage in the database as closed;
        void endAttendance();

        //********** end attendance ****************
        // mark attendance flage in the database as closed;
        boolean isTaskIsRunning();


    }
}
