package cmp.sem.team8.smarlecture.session.create;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.session.questions.QuestionsPresenter;
import cmp.sem.team8.smarlecture.session.questions.QuestionsFragment;
import cmp.sem.team8.smarlecture.session.create.members.MembersFragment;
import cmp.sem.team8.smarlecture.session.create.members.MembersPresenter;
import cmp.sem.team8.smarlecture.session.create.objectives.ObjectivesFragment;
import cmp.sem.team8.smarlecture.session.create.objectives.ObjectivesPresenter;

import cmp.sem.team8.smarlecture.session.create.info.InfoFragment;
import cmp.sem.team8.smarlecture.session.create.info.InfoPresenter;

/**
 * Created by ramym on 3/25/2018.
 */

public class CreateSessionPagerAdapter extends FragmentPagerAdapter {

    InfoFragment infoFragment;
    InfoPresenter mInfoPresenter;
    ObjectivesFragment mObjectivesFragment;
    ObjectivesPresenter mObjectivesPresenter;
    MembersFragment membersFragment;
    MembersPresenter mMembersPresenter;
    QuestionsFragment questionsFragment;
    QuestionsPresenter questionsPresenter;
    private int numOfTabs;
    private String mGroupId = null;
    private String mSessionID = null;

    public CreateSessionPagerAdapter(FragmentManager fm, int numOfTabs, String groupId, String sessionID) {

        super(fm);

        this.numOfTabs = numOfTabs;

        mMembersPresenter = null;

        mInfoPresenter = null;

        infoFragment = null;

        membersFragment = null;

        mObjectivesFragment = null;

        mObjectivesPresenter = null;

        mGroupId = groupId;

        mSessionID = sessionID;
        questionsFragment = null;
        questionsPresenter = null;
    }

    public InfoPresenter getSessionPresenter() {
        return mInfoPresenter;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                infoFragment = infoFragment == null ?
                        InfoFragment.newInstance() : infoFragment;
                if (mInfoPresenter == null)
                    mInfoPresenter = new InfoPresenter(infoFragment, mGroupId, mSessionID);
                return infoFragment;

            case 1:

                membersFragment = membersFragment == null ?
                        MembersFragment.newInstance() : membersFragment;

                if (mMembersPresenter == null)
                    mMembersPresenter = new MembersPresenter(FirebaseRepository.getInstance(), membersFragment, mSessionID);
                else mMembersPresenter.start();

                return membersFragment;
            case 2:

                if (mObjectivesFragment == null)

                    mObjectivesFragment = ObjectivesFragment.newInstance();

                mObjectivesPresenter = new ObjectivesPresenter(mObjectivesFragment, mSessionID, FirebaseRepository.getInstance());

                return mObjectivesFragment;

            case 3:
                if (questionsFragment == null)
                    questionsFragment = QuestionsFragment.newInstance();
                questionsPresenter = new QuestionsPresenter(FirebaseRepository.getInstance(),
                        FirebaseAuthService.getInstance(), questionsFragment, mSessionID, true);
                return questionsFragment;


            default:

                return null;
        }
    }

    public InfoPresenter getmInfoPresenter() {
        return mInfoPresenter;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)

            return "Info";

        else if (position == 1) {

            return "Members";

        } else if (position == 2) {

            return "Objectives";

        } else {
            return "Questions";
        }


    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case 0:
                infoFragment = infoFragment == null ?
                        InfoFragment.newInstance() : infoFragment;
                if (mInfoPresenter == null)
                    mInfoPresenter = new InfoPresenter(infoFragment, mGroupId, mSessionID);
                return infoFragment;

            case 1:

                membersFragment = membersFragment == null ?
                        MembersFragment.newInstance() : membersFragment;

                if (mMembersPresenter == null)
                    mMembersPresenter = new MembersPresenter(FirebaseRepository.getInstance(), membersFragment, mSessionID);

                return membersFragment;
            case 2:

                if (mObjectivesFragment == null)

                    mObjectivesFragment = ObjectivesFragment.newInstance();

                mObjectivesPresenter = new ObjectivesPresenter(mObjectivesFragment, mSessionID, FirebaseRepository.getInstance());

                return mObjectivesFragment;

            case 3:
                if (questionsFragment == null)
                    questionsFragment = QuestionsFragment.newInstance();
                questionsPresenter = new QuestionsPresenter(FirebaseRepository.getInstance(),
                        FirebaseAuthService.getInstance(), questionsFragment, mSessionID, true);
                return questionsFragment;


            default:

                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    /*public interface FragmentLifeCycle {
        void onPauseFragment();

        void onResumeFragment();
    }*/
}