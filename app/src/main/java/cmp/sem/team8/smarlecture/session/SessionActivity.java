package cmp.sem.team8.smarlecture.session;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendancePresenter;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoPresenter;

public class SessionActivity extends AppCompatActivity {

    private Button mQuestionButton;
    private Button mAttendanceButton;
    private Button mObjectivesButton;
    private Button mStartSessionButton;

    int SessionId;

    private BeginAttendancePresenter mAttendancePresenter;
    private SessionInfoPresenter mSessionInfoPresenter;

    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem SessionTab;
    TabItem AttendanceTab ;
   PagerAdapter pageAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

/*        mQuestionButton = findViewById(R.id.sessionActivity_question);
        mAttendanceButton = findViewById(R.id.sessionActivity_attendance);
        mObjectivesButton = findViewById(R.id.sessionActivity_objectives);*/


         tabLayout = findViewById(R.id.tablayout);
         SessionTab= findViewById(R.id.start_end_session_tab);
         AttendanceTab = findViewById(R.id.begin_attandence_tab);
         viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                getIntent().getStringExtra("group_id"));

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

       /* mAttendancePresenter = null;
        mSessionInfoPresenter=null;
        //
        // begin the session with generating the session.
        int fragmentId = R.id.contentFrame2;
        SessionInfoFragment fragment2 = (SessionInfoFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame2);
         fragment2 = fragment2 == null ?
               SessionInfoFragment.newInstance()
                : fragment2;
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

            mSessionInfoPresenter = new SessionInfoPresenter(fragment2);


        /////*/

    }


    public void onToFragmentClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
           // case R.id.sessionActivity_startsession:
            //    SessionInfoFragment fragment2 = (SessionInfoFragment)
             //           getSupportFragmentManager().findFragmentById(R.id.contentFrame2);
            //    fragment2 = fragment2 == null ?
            //            SessionInfoFragment.newInstance()
            //            : fragment2;
            //    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

            //    if (mSessionInfoPresenter == null)
            //        mSessionInfoPresenter = new SessionInfoPresenter(fragment2);
            //    else
            //        mSessionInfoPresenter.refresh();
            //    break;

           /* case R.id.sessionActivity_endsession:
                 mSessionInfoPresenter.endSession();
                 mSessionInfoPresenter=null;
                 break;

            case R.id.sessionActivity_attendance:
                int fragmentId = R.id.contentFrame3;
                    BeginAttendanceFragment fragment2 = (BeginAttendanceFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame3);
                   fragment2 = fragment2 == null ?
                           BeginAttendanceFragment.newInstance()
                            : fragment2;
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

                   if (mAttendancePresenter == null)
                   {
                       mAttendancePresenter = new BeginAttendancePresenter(fragment2,getIntent().getIntExtra("SessionId",1));
                   }
                   else
                        mAttendancePresenter.refresh();
                    break;

                ///////////
*/
        }
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(this);
        mAlertBuilder.setTitle("Confirmation");
        mAlertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        mAlertBuilder.setMessage("The session will be ended. are you sure to continue");
        mAlertBuilder.setPositiveButton("End Session", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SessionActivity.super.onBackPressed();
            }
        });
        mAlertBuilder.show();
    }
}
