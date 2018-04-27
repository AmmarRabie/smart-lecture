package cmp.sem.team8.smarlecture.joinsession;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.WriteAttendanceFragment;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.WriteAttendancePresenter;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoFragment;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoPresenter;

/**
 * Created by ramym on 3/25/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    WriteAttendanceFragment writeAttendanceFragment;
    WriteAttendancePresenter mwriteAttendancePresenter;
  cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoFragment  sessionInfoFragment;
   cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoPresenter sessionInfoPresenter;
    private int numOfTabs;
    String mSessionID;
    String mGroupID;

    public PagerAdapter(FragmentManager fm, int numOfTabs,String sessionID,String groupID) {
        super(fm);
        this.numOfTabs = numOfTabs;

        writeAttendanceFragment = null;
        mwriteAttendancePresenter = null;
        mSessionID=sessionID;
        mGroupID=groupID;
        sessionInfoFragment=null;
        sessionInfoPresenter=null;


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
                if(sessionInfoFragment==null){
                    sessionInfoFragment=cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoFragment.newInstance();
                }
                sessionInfoPresenter=new cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoPresenter(sessionInfoFragment,mGroupID,mSessionID,FirebaseRepository.getInstance());
                return sessionInfoFragment;


            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0)

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