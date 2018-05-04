package cmp.sem.team8.smarlecture.statistics;

import android.util.SparseArray;

import java.util.List;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.GroupOfUsersModel;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;


/**
 * Created by ramym on 4/27/2018.
 */

public interface GroupStatisticsContract {
    interface Views extends IBaseView<GroupStatisticsContract.Actions> {

        /** ------- show Attendance Percentage ----------
         *   to show percentage of attendance of specific group
         *
         * @param Percentage   --------> attendace percentage
         */
        void showAttendancePercentage(double Percentage);

        /** ----- show Most Attendant Users -------
         *  show list of most 10 attendant users
         * @param users
         */
        void showMostAttendantUsers(List<UserGradeModel> users);

        /**--- show worst attendant Users -----------
         *  show list of worst 10 attendant users
         * @param users
         */
        void showWorstAttendantUsers(List<UserGradeModel> users);


        /** getGroupId
         *   to send to presenter id of the current group
         * @return  Group id
         */
         String getGroupID();


        /**   showProgressIndicator
         *   to show any Ui to tell the user there is some tasks still running
         */
         void showProgressIndicator();

        /**   hideProgressIndicator
         *   to hide  Ui to tell the user there is some tasks are finished
         */
         void  hideProgressIndicator();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {


        /** -----  Session Attendance Percentage -------
         *   to get and show attendacne Percentage of the group
         */
        void groupAttendancePercentage();

        /**  ---- Most Attendance Users ------------
         *   to get and show list of most attendance users of the group
         */
        void MostAndWorstAttendanceUsers();


        SparseArray<GroupOfUsersModel>getGroupsOfUsers();


    }

}
