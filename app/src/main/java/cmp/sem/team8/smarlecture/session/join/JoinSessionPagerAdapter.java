package cmp.sem.team8.smarlecture.session.join;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.session.join.info.InfoPresenter;
import cmp.sem.team8.smarlecture.session.questions.QuestionsFragment;
import cmp.sem.team8.smarlecture.session.questions.QuestionsPresenter;
import cmp.sem.team8.smarlecture.session.join.info.InfoFragment;
import cmp.sem.team8.smarlecture.session.join.attendance.AttendanceFragment;
import cmp.sem.team8.smarlecture.session.join.attendance.AttendancePresenter;

/**
 * Created by ramym on 3/25/2018.
 */

class JoinSessionPagerAdapter extends FragmentPagerAdapter {

    private final String SESSION_ID;
    private final String GROUP_ID;
    private final int NUM_TABS;
    AttendanceFragment attendanceFragment;
    AttendancePresenter attendancePresenter;
    InfoFragment infoFragment;
    InfoPresenter infoPresenter;
    QuestionsFragment questionsFragment;
    QuestionsPresenter questionsPresenter;

    public JoinSessionPagerAdapter(FragmentManager fm, int numOfTabs, String sessionID, String groupID) {
        super(fm);
        this.NUM_TABS = numOfTabs;

        attendanceFragment = null;
        attendancePresenter = null;
        SESSION_ID = sessionID;
        GROUP_ID = groupID;
        infoFragment = null;
        infoPresenter = null;


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (infoFragment == null)
                    infoFragment = InfoFragment.newInstance();
                if (infoPresenter == null)
                    infoPresenter = new InfoPresenter(infoFragment, GROUP_ID, SESSION_ID, FirebaseRepository.getInstance());
                return infoFragment;
            case 1:
                if (questionsFragment == null)
                    questionsFragment = QuestionsFragment.newInstance();
                if (questionsPresenter == null)
                    questionsPresenter = new QuestionsPresenter(FirebaseRepository.getInstance(), FirebaseAuthService.getInstance()
                            , questionsFragment, SESSION_ID,false);
                return questionsFragment;
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