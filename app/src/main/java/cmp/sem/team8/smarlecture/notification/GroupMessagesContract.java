package cmp.sem.team8.smarlecture.notification;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.GroupMessageModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public interface GroupMessagesContract {


    interface Views extends IBaseView<Actions> {
        void showGroupMessages(ArrayList<GroupMessageModel> groupMessages);

        void showErrorMessage(String message);
    }

    interface Actions extends IBasePresenter {
    }
}



