package cmp.sem.team8.smarlecture.group;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.group.members.MembersPresenter;
import cmp.sem.team8.smarlecture.group.sessions.SessionsFragment;
import cmp.sem.team8.smarlecture.group.sessions.SessionsPresenter;
import cmp.sem.team8.smarlecture.group.members.MembersFragment;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public class GroupPagerAdapter extends FragmentPagerAdapter {
    private final String GROUP_ID;
    private final String GROUP_NAME;
    MembersFragment membersFragment;
    MembersPresenter membersPresenter;
    SessionsFragment sessionsFragment;
    SessionsPresenter sessionsPresenter;

    public GroupPagerAdapter(FragmentManager fm, String groupID, String groupName) {
        super(fm);

        GROUP_ID = groupID;
        GROUP_NAME = groupName;
        membersFragment = null;
        sessionsFragment = null;
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
                if (sessionsFragment == null) {
                    sessionsFragment = SessionsFragment.newInstance();
                    sessionsPresenter = new SessionsPresenter(sessionsFragment, GROUP_ID, FirebaseRepository.getInstance());
                    return sessionsFragment;
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
