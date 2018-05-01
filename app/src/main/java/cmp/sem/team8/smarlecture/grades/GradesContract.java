package cmp.sem.team8.smarlecture.grades;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;
import cmp.sem.team8.smarlecture.grouplist.GroupListContract;

/**
 * Created by ramym on 5/1/2018.
 */

public interface GradesContract {

    interface Views extends IBaseView<GradesContract.Actions> {

        /**
         *   show error message
         * @param cause
         * to show an error message ex: higest grade < lowest grade
         */
        void showErrorMessage(String cause);

        /**
         *    show Grades of Group
         * @param list
         * to show list of users of the group with their grade
         */
        void showGradesOfGroup(ArrayList<UserGradeModel> list);

        /**
         *
         * @return  Highest grade of  a student
         */
        int getHighestGrade();


        /**
         *
         * @return  Lowest grade of  a student
         */
        int getLowestGrade();

         void showProgressIndicator();

         void hideProgressIndicator();

         String getGroupId();
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        /**
         *  get Group grade
         *  to get Group grade for the first time
         */
        void getGroupGrade();


        /**
         * set Group Grade
         * to set new Grade to students
         */
        void setGroupGrade();
    }
}
