package cmp.sem.team8.smarlecture.group;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.group.sessionslist.SessionListFragment;
import cmp.sem.team8.smarlecture.group.sessionslist.SessionListPresenter;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListFragment;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListPresenter;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public class GroupPagerAdapter extends FragmentPagerAdapter {
    private String mGroupID;
    private String mGroupName;
    StudentListFragment studentListFragment ;
    StudentListPresenter studentListPresenter;
    SessionListFragment sessionListFragment;
    SessionListPresenter sessionListPresenter;

    public GroupPagerAdapter(FragmentManager fm,String groupID,String groupName) {
        super(fm);

        mGroupID=groupID;
        mGroupName=groupName;
        studentListFragment=null;
        sessionListFragment=null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(studentListFragment==null)
                    studentListFragment=StudentListFragment.newInstance();
                studentListPresenter=new StudentListPresenter(studentListFragment,mGroupID,mGroupName);
                return studentListFragment;

            case 1:
                if(sessionListFragment==null){
                    sessionListFragment=SessionListFragment.newInstance();
                    sessionListPresenter=new SessionListPresenter(sessionListFragment,mGroupID);
                    return sessionListFragment;
                }


            default:
              return null;

        }
    }
    @Override
    public CharSequence getPageTitle(int position) {

        if(position==0)
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
