package cmp.sem.team8.smarlecture.session.create;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.session.create.info.InfoFragment;
import cmp.sem.team8.smarlecture.session.create.info.InfoPresenter;
import cmp.sem.team8.smarlecture.session.create.members.MembersFragment;
import cmp.sem.team8.smarlecture.session.create.members.MembersPresenter;
import cmp.sem.team8.smarlecture.session.create.objectives.ObjectivesFragment;
import cmp.sem.team8.smarlecture.session.create.objectives.ObjectivesPresenter;
import cmp.sem.team8.smarlecture.session.questions.QuestionsFragment;
import cmp.sem.team8.smarlecture.session.questions.QuestionsPresenter;

/**
 * Created by ramym on 3/25/2018.
 */

class CreateSessionPagerAdapter extends FragmentPagerAdapter {

    private final int NUM_TABS;
    private final String GROUP_ID;
    private final String SESSION_ID;
    InfoFragment infoFragment;
    InfoPresenter infoPresenter;
    ObjectivesFragment objectivesFragment;
    ObjectivesPresenter objectivesPresenter;
    MembersFragment membersFragment;
    MembersPresenter membersPresenter;
    QuestionsFragment questionsFragment;
    QuestionsPresenter questionsPresenter;

    public CreateSessionPagerAdapter(FragmentManager fm, int numOfTabs, String groupId, String sessionID) {
        super(fm);
        this.NUM_TABS = numOfTabs;
        GROUP_ID = groupId;
        SESSION_ID = sessionID;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                infoFragment = infoFragment == null ?
                        InfoFragment.newInstance() : infoFragment;
                if (infoPresenter == null)
                    infoPresenter = new InfoPresenter(infoFragment, GROUP_ID, SESSION_ID);
                return infoFragment;

            case 1:

                membersFragment = membersFragment == null ?
                        MembersFragment.newInstance() : membersFragment;

                if (membersPresenter == null)
                    membersPresenter = new MembersPresenter(FirebaseRepository.getInstance(), membersFragment, SESSION_ID);
                else membersPresenter.start();

                return membersFragment;
            case 2:

                if (objectivesFragment == null)

                    objectivesFragment = ObjectivesFragment.newInstance();

                objectivesPresenter = new ObjectivesPresenter(objectivesFragment, SESSION_ID, FirebaseRepository.getInstance());

                return objectivesFragment;

            case 3:
                if (questionsFragment == null)
                    questionsFragment = QuestionsFragment.newInstance();
                questionsPresenter = new QuestionsPresenter(FirebaseRepository.getInstance(),
                        FirebaseAuthService.getInstance(), questionsFragment, SESSION_ID, true);
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
                return "Members";
            case 2:
                return "Objectives";
            case 3:
                return "Questions";
        }
        return "";
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

}