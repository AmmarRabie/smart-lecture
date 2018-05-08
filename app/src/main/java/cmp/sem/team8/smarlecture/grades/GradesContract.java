package cmp.sem.team8.smarlecture.grades;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;

/**
 * Created by ramym on 5/1/2018.
 */


public interface GradesContract {

    interface Views extends IBaseView<GradesContract.Actions> {

        /**
         * show error message
         *
         * @param cause to show an error message ex: higest grade < lowest grade
         */
        void showErrorMessage(String cause);

        /**
         * show Grades of Group
         *
         * @param list to show list of users of the group with their grade
         */
        void showGradesOfGroup(ArrayList<UserGradeModel> list);

        /**
         * getHighestGrade
         *
         * @return Highest grade of  a student
         */
        int getHighestGrade();


        /**
         * getLowestGrade
         *
         * @return Lowest grade of  a student
         */
        int getLowestGrade();


        /**
         * showProgressIndicator
         * to show any Ui to tell the user there is some tasks still running
         */
        void showProgressIndicator();

        /**
         * hideProgressIndicator
         * to hide  Ui to tell the user there is some tasks are finished
         */
        void hideProgressIndicator();

        /**
         * getGroupId
         * to send to presenter id of the current group
         *
         * @return Group id
         */
        String getGroupId();


        void hideActivityLoading();

        void showEmptyView();

        void hideEmptyView();


    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        /**
         * get Group grade
         * to get Group grade for the first time
         */
        void getGroupGrade();


        /**
         * set Group Grade
         * to set new Grade to students
         */
        void setGroupGrade();
    }
}
