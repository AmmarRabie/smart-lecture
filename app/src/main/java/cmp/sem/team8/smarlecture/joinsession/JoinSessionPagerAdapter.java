package cmp.sem.team8.smarlecture.joinsession;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.joinsession.questions.QuestionFragment;
import cmp.sem.team8.smarlecture.joinsession.questions.QuestionPresenter;
import cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoFragment;
import cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoPresenter;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.WriteAttendanceFragment;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.WriteAttendancePresenter;

/**
 * Created by ramym on 3/25/2018.
 */

class JoinSessionPagerAdapter extends FragmentPagerAdapter {

    private final String SESSION_ID;
    private final String GROUP_ID;
    private final int NUM_TABS;
    WriteAttendanceFragment writeAttendanceFragment;
    WriteAttendancePresenter writeAttendancePresenter;
    SessionInfoFragment sessionInfoFragment;
    SessionInfoPresenter sessionInfoPresenter;
    QuestionFragment questionFragment;
    QuestionPresenter questionPresenter;

    public JoinSessionPagerAdapter(FragmentManager fm, int numOfTabs, String sessionID, String groupID) {
        super(fm);
        this.NUM_TABS = numOfTabs;

        writeAttendanceFragment = null;
        writeAttendancePresenter = null;
        SESSION_ID = sessionID;
        GROUP_ID = groupID;
        sessionInfoFragment = null;
        sessionInfoPresenter = null;


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (sessionInfoFragment == null)
                    sessionInfoFragment = SessionInfoFragment.newInstance();
                if (sessionInfoPresenter == null)
                    sessionInfoPresenter = new SessionInfoPresenter(sessionInfoFragment, GROUP_ID, SESSION_ID, FirebaseRepository.getInstance());
                return sessionInfoFragment;
            case 1:
                if (questionFragment == null)
                    questionFragment = QuestionFragment.newInstance();
                if (questionPresenter == null)
                    questionPresenter = new QuestionPresenter(FirebaseRepository.getInstance(), FirebaseAuthService.getInstance()
                            , questionFragment, SESSION_ID);
                return questionFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Info";
            case 1:
                return "Questions";
        }
        return "";
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}