package cmp.sem.team8.smarlecture.group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.group.sessionslist.SessionListFragment;
import cmp.sem.team8.smarlecture.group.sessionslist.SessionListPresenter;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListFragment;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListPresenter;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public class GroupPagerAdapter extends FragmentPagerAdapter {
    private final String GROUP_ID;
    private final String GROUP_NAME;
    StudentListFragment studentListFragment;
    StudentListPresenter studentListPresenter;
    SessionListFragment sessionListFragment;
    SessionListPresenter sessionListPresenter;

    public GroupPagerAdapter(FragmentManager fm, String groupID, String groupName) {
        super(fm);

        GROUP_ID = groupID;
        GROUP_NAME = groupName;
        studentListFragment = null;
        sessionListFragment = null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (studentListFragment == null)
                    studentListFragment = StudentListFragment.newInstance();
                studentListPresenter = new StudentListPresenter(FirebaseRepository.getInstance(), studentListFragment, GROUP_ID, GROUP_NAME);
                return studentListFragment;

            case 1:
                if (sessionListFragment == null) {
                    sessionListFragment = SessionListFragment.newInstance();
                    sessionListPresenter = new SessionListPresenter(sessionListFragment, GROUP_ID);
                    return sessionListFragment;
                }
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)
            return "Students";
        else {
            return "Sessions";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
