package cmp.sem.team8.smarlecture.session.create;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
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
                if (membersFragment == null) {
                    membersFragment = MembersFragment.newInstance();
                    if (membersPresenter == null)
                        membersPresenter = new MembersPresenter(FirebaseRepository.getInstance(), membersFragment, SESSION_ID);
                    else
                        membersPresenter.updateView(membersFragment);
                }
                return membersFragment;
            case 1:
                if (objectivesFragment == null)
                    objectivesFragment = ObjectivesFragment.newInstance();
                objectivesPresenter = new ObjectivesPresenter(objectivesFragment, SESSION_ID, FirebaseRepository.getInstance());
                return objectivesFragment;
            case 2:
                if (questionsFragment == null) {
                    questionsFragment = QuestionsFragment.newInstance();
                    questionsPresenter = new QuestionsPresenter(FirebaseRepository.getInstance(),
                            FirebaseAuthService.getInstance(), questionsFragment, SESSION_ID, true);
                }
                return questionsFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Members";
            case 1:
                return "Objectives";
            case 2:
                return "Questions";
        }
        return "";
    }

    @Override
    public int getCount() {
        return 3;
    }

}