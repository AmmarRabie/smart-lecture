package cmp.sem.team8.smarlecture.session;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendanceFragment;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendancePresenter;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoFragment;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoPresenter;

/**
 * Created by ramym on 3/25/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private String mGroupId = null;
    SessionInfoFragment sessionInfoFragment;
    SessionInfoPresenter mSessionInfoPresenter;
    private  String mSessionID=null;

    BeginAttendanceFragment beginAttendanceFragment;
    BeginAttendancePresenter mBeginAttendancePresenter;

    public PagerAdapter(FragmentManager fm,int numOfTabs, String groupId,String sessionID) {
        super(fm);
        this.numOfTabs = numOfTabs;

        mBeginAttendancePresenter=null;
        mSessionInfoPresenter =null;
        sessionInfoFragment =null;
        beginAttendanceFragment=null;
        mGroupId = groupId;
        mSessionID=sessionID;
    }

    public SessionInfoPresenter getSessionPresenter(){
        return mSessionInfoPresenter;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                sessionInfoFragment = sessionInfoFragment == null ?
                        SessionInfoFragment.newInstance()
                        : sessionInfoFragment;
                mSessionInfoPresenter = new SessionInfoPresenter(sessionInfoFragment,mGroupId,mSessionID);
                return sessionInfoFragment;

            case 1:
                beginAttendanceFragment = beginAttendanceFragment == null ?
                        BeginAttendanceFragment.newInstance()
                        : beginAttendanceFragment;
                mBeginAttendancePresenter = new BeginAttendancePresenter(beginAttendanceFragment,mGroupId);

                return beginAttendanceFragment;
            case 2:
               // return new CallFragment();
            default:
                return null;
        }
    }

    public SessionInfoPresenter getmSessionInfoPresenter() {
        return mSessionInfoPresenter;
    }
    @Override
    public CharSequence getPageTitle(int position) {

        if(position==0)
            return "Info";
        else {
            return "Attendance";
        }


    }



    @Override
    public int getCount() {
        return numOfTabs;
    }
}