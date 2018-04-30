package cmp.sem.team8.smarlecture.statistics;

import java.util.List;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
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
        void showMostAttendantUsers(List<UserModel> users);

        /**--- show worst attendant Users -----------
         *  show list of worst 10 attendant users
         * @param users
         */
        void showWorstAttendantUsers(List<UserModel> users);

         String getGroupID();


         void showProgressIndicator();

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


    }

}
