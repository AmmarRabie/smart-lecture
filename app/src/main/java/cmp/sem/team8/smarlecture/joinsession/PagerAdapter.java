package cmp.sem.team8.smarlecture.joinsession;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.joinsession.writeattendance.WriteAttendanceFragment;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.WriteAttendancePresenter;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendanceFragment;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendancePresenter;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionFragment;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionPresenter;

/**
 * Created by ramym on 3/25/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    WriteAttendanceFragment writeAttendanceFragment;
    WriteAttendancePresenter mwriteAttendancePresenter;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

         writeAttendanceFragment=null;
         mwriteAttendancePresenter=null;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                writeAttendanceFragment = writeAttendanceFragment == null ?
                        WriteAttendanceFragment.newInstance()
                        : writeAttendanceFragment;
                mwriteAttendancePresenter = new WriteAttendancePresenter(writeAttendanceFragment);
                return writeAttendanceFragment;

            case 1:
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