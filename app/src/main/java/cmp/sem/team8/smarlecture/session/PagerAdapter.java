package cmp.sem.team8.smarlecture.session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendanceFragment;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendancePresenter;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionFragment;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionPresenter;

/**
 * Created by ramym on 3/25/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    StartSessionFragment startSessionFragment;
    StartSessionPresenter mStartSessionPresenter;

    BeginAttendanceFragment beginAttendanceFragment;
    BeginAttendancePresenter mBeginAttendancePresenter;
    public PagerAdapter(FragmentManager fm,int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

        mBeginAttendancePresenter=null;
        mStartSessionPresenter=null;
        startSessionFragment=null;
        beginAttendanceFragment=null;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                startSessionFragment = startSessionFragment == null ?
                        StartSessionFragment.newInstance()
                        : startSessionFragment;
                mStartSessionPresenter = new StartSessionPresenter(startSessionFragment);
                return startSessionFragment;

            case 1:
                beginAttendanceFragment = beginAttendanceFragment == null ?
                        BeginAttendanceFragment.newInstance()
                        : beginAttendanceFragment;
                mBeginAttendancePresenter = new BeginAttendancePresenter(beginAttendanceFragment);

                return beginAttendanceFragment;
            case 2:
               // return new CallFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}