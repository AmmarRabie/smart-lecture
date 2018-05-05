package cmp.sem.team8.smarlecture.group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.group.members.MembersPresenter;
import cmp.sem.team8.smarlecture.group.sessions.SessionListFragment;
import cmp.sem.team8.smarlecture.group.sessions.SessionListPresenter;
import cmp.sem.team8.smarlecture.group.members.MembersFragment;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public class GroupPagerAdapter extends FragmentPagerAdapter {
    private final String GROUP_ID;
    private final String GROUP_NAME;
    MembersFragment membersFragment;
    MembersPresenter membersPresenter;
    SessionListFragment sessionListFragment;
    SessionListPresenter sessionListPresenter;

    public GroupPagerAdapter(FragmentManager fm, String groupID, String groupName) {
        super(fm);

        GROUP_ID = groupID;
        GROUP_NAME = groupName;
        membersFragment = null;
        sessionListFragment = null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (membersFragment == null)
                    membersFragment = MembersFragment.newInstance();
                membersPresenter = new MembersPresenter(FirebaseRepository.getInstance(), membersFragment, GROUP_ID, GROUP_NAME);
                return membersFragment;

            case 1:
                if (sessionListFragment == null) {
                    sessionListFragment = SessionListFragment.newInstance();
                    sessionListPresenter = new SessionListPresenter(sessionListFragment, GROUP_ID, FirebaseRepository.getInstance());
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
