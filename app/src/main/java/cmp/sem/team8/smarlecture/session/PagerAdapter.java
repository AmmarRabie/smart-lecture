package cmp.sem.team8.smarlecture.session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.session.members.MembersFragment;
import cmp.sem.team8.smarlecture.session.members.MembersPresenter;
import cmp.sem.team8.smarlecture.session.objectives.ObjectivesFragment;
import cmp.sem.team8.smarlecture.session.objectives.ObjectivesPresenter;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoFragment;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoPresenter;

/**
 * Created by ramym on 3/25/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    SessionInfoFragment sessionInfoFragment;
    SessionInfoPresenter mSessionInfoPresenter;
    ObjectivesFragment mObjectivesFragment;
    ObjectivesPresenter mObjectivesPresenter;
    MembersFragment membersFragment;
    MembersPresenter mMembersPresenter;
    private int numOfTabs;
    private String mGroupId = null;
    private String mSessionID = null;

    public PagerAdapter(FragmentManager fm, int numOfTabs, String groupId, String sessionID) {

        super(fm);

        this.numOfTabs = numOfTabs;

        mMembersPresenter = null;

        mSessionInfoPresenter = null;

        sessionInfoFragment = null;

        membersFragment = null;

        mObjectivesFragment = null;

        mObjectivesPresenter = null;

        mGroupId = groupId;

        mSessionID = sessionID;
    }

    public SessionInfoPresenter getSessionPresenter() {
        return mSessionInfoPresenter;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                sessionInfoFragment = sessionInfoFragment == null ?
                        SessionInfoFragment.newInstance() : sessionInfoFragment;
                if (mSessionInfoPresenter == null)
                    mSessionInfoPresenter = new SessionInfoPresenter(sessionInfoFragment, mGroupId, mSessionID);
                return sessionInfoFragment;

            case 1:

                membersFragment = membersFragment == null ?
                        MembersFragment.newInstance() : membersFragment;

                if (mMembersPresenter == null)
                    mMembersPresenter = new MembersPresenter(FirebaseRepository.getInstance(), membersFragment, mSessionID);

                return membersFragment;
            case 2:

                if (mObjectivesFragment == null)

                    mObjectivesFragment = ObjectivesFragment.newInstance();

                mObjectivesPresenter = new ObjectivesPresenter(mObjectivesFragment, mSessionID);

                return mObjectivesFragment;

            default:

                return null;
        }
    }

    public SessionInfoPresenter getmSessionInfoPresenter() {
        return mSessionInfoPresenter;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)

            return "Info";

        else if (position == 1) {

            return "Members";

        } else {

            return "Objectives";

        }


    }


    @Override
    public int getCount() {
        return 3;
    }

    /*public interface FragmentLifeCycle {
        void onPauseFragment();

        void onResumeFragment();
    }*/
}