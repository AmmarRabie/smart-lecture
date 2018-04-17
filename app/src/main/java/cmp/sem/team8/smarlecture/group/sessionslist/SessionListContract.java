package cmp.sem.team8.smarlecture.group.sessionslist;

import android.service.textservice.SpellCheckerService;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListContract;
import cmp.sem.team8.smarlecture.model.SessionModel;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public interface SessionListContract {
    interface Views extends IBaseView<SessionListContract.Actions>
    {
        void showOnErrorMessage(String cause);

        void showSessionsList(ArrayList<SessionModel> sessionslist);

        void OnDeleteSuccess(String deletedSession);

        void OnAddSuccess(SessionModel addedSession);

        void OnEditSuccess(String sessionID,String sessionName);

        void handleOfflineStates();

        boolean getOfflineState();

    }
    interface Actions extends IBasePresenter {

        void addSession(String sessionName);

        void editSession(String sessionName,String sessionID);

        void deleteSession(String sessionID);

        void end();
    }
}
