package cmp.sem.team8.smarlecture.session.startsession;

import android.support.v4.app.Fragment;
import android.view.View;

import cmp.sem.team8.smarlecture.session.startsession.StartSessionContract;

/**
 * Created by ramym on 3/15/2018.
 */

public class StartSessionFragment  extends Fragment implements StartSessionContract.Views{

    private StartSessionContract.Actions mPresenter;

    @Override
    public void setPresenter(StartSessionContract.Actions presenter) {

        mPresenter = presenter;
    }

    @Override
    public void showSessionId() {

    }
}
