package cmp.sem.team8.smarlecture.invitations;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public interface InvitationsContract {


    interface Views extends IBaseView<Actions> {
        void addGroupInvitation(GroupInvitationModel session);
    }

    interface Actions extends IBasePresenter {
    }
}



