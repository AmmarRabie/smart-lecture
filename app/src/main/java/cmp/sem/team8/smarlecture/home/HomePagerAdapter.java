package cmp.sem.team8.smarlecture.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.home.groups.GroupsFragment;
import cmp.sem.team8.smarlecture.home.groups.GroupsPresenter;
import cmp.sem.team8.smarlecture.home.newsfeed.NewsFeedFragment;
import cmp.sem.team8.smarlecture.home.newsfeed.NewsFeedPresenter;

/**
 * Created by Loai Ali on 5/5/2018.
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
NewsFeedFragment newsFeedFragment;
NewsFeedPresenter newsFeedPresenter;
GroupsFragment groupsFragment;
GroupsPresenter groupsPresenter;


    private int numOfTabs;

    public HomePagerAdapter(FragmentManager fm,int numOfTabs) {
        super(fm);
        this.numOfTabs= numOfTabs;
        newsFeedFragment=null;
        newsFeedPresenter=null;
        groupsFragment=null;
        groupsPresenter=null;
    }

    @Override
    public Fragment getItem(int position) {
       switch (position) {
           case 0:
               if (groupsFragment == null)
                   groupsFragment = GroupsFragment.newInstance();
               groupsPresenter = new GroupsPresenter(groupsFragment, FirebaseRepository.getInstance());
               return groupsFragment;

           case 1:
               if(newsFeedFragment==null)
                   newsFeedFragment=NewsFeedFragment.newInstance();
               newsFeedPresenter=new NewsFeedPresenter(FirebaseAuthService.getInstance(),FirebaseRepository.getInstance(),newsFeedFragment);
               return newsFeedFragment;
               default:
                   return null;
       }

    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
            return "Groups";
        else {
            return "NewsFeed";
        }
    }
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
