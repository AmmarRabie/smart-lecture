package cmp.sem.team8.smarlecture.session.startsession;

/**
 * Created by ramym on 3/15/2018.
 */

public class StartSessionPresenter implements StartSessionContract.Actions {


    StartSessionContract.Views mView;

    public StartSessionPresenter(StartSessionContract.Views view) {
        mView = view;
        mView.setPresenter(this);
    }
    @Override
    public void start() {

    }

    @Override
    public void startSession() {

    }
}
