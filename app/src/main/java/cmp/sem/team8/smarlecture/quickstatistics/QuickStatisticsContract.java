package cmp.sem.team8.smarlecture.quickstatistics;

import cmp.sem.team8.fireapp.IBasePresenter;
import cmp.sem.team8.fireapp.IBaseView;

/**
 * Created by AmmarRabie on 08/03/2018.
 */


/**
 * when the presenter start he should look for the current user -unless it is passes earlier-
 * (try to get if from the model), if he can't find the current user
 * he should tell the view to show error and sign in the user so that the view (fragment) get
 * the activity and start the authentication activity
 * which will first find if he can log in and if not, he make a new account
 * and this data returned back to the quickstartstatistics activity in the intent, so this
 * activity in creation should look for this data and if it find it, it should tell the
 * presenter at its creation that he should't look for the user as the user is passes to
 * him at creation
 */
public interface QuickStatisticsContract {


    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        /**
         * called only when the presenter is constructed with no user
         * and he could find the current user in the model.
         * <p>
         *     The viewer should update the user info to let the user know that he is being
         *     signed in.
         *
         * </p>
         * @param name the name of the user
         * @param email the email of the user
         */
        void displayUserInfo(String name, String email);

    }
}
