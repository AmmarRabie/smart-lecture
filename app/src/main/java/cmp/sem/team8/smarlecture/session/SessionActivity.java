package cmp.sem.team8.smarlecture.session;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoPresenter;

public class SessionActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem SessionTab;
    TabItem ObjectivesTab;
    TabItem AttendanceTab;
    PagerAdapter pageAdapter;
    String mGroupID;
    String mSessionID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroupID=getIntent().getStringExtra("group_id");
        mSessionID=getIntent().getStringExtra("session_id");
        setContentView(R.layout.activity_session);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),mGroupID
                , mSessionID);

        viewPager.setAdapter(pageAdapter);


      viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      int currposition=0;
          @Override
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

          }

          @Override
          public void onPageSelected(int position) {

              pageAdapter.getItem(position).onResume();
              pageAdapter.getItem(currposition).onResume();
              currposition=position;

          }

          @Override
          public void onPageScrollStateChanged(int state) {

          }
      });

     /*   private ViewPager.OnPageChangeListener pageChangeListner=new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                JoinSessionPagerAdapter.FragmentLifeCycle fragmentToShow=(JoinSessionPagerAdapter.FragmentLifeCycle)pageAdapter.getItem(position);
                fragmentToShow.onResumeFragment();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };*/

        tabLayout.setupWithViewPager(viewPager);


    }

    //********** onBackPressed  ****************
    // to close session session activity and return back to the previous activity.
    // steps
    // 1- warning message before close
    // 2- end session before closing the activity
    // 3-
    @Override
    public void onBackPressed() {
        SessionInfoPresenter sessionInfoPresenter = pageAdapter.getmSessionInfoPresenter();
        if (sessionInfoPresenter != null) {
            if(sessionInfoPresenter.getSessionStatus().equals(AppDataSource.SessionStatus.OPEN.toString())) {

                AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(this);
                mAlertBuilder.setTitle("Confirmation");
                mAlertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                mAlertBuilder.setMessage("The session will be ended. are you sure to continue");
                mAlertBuilder.setPositiveButton("End Session", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pageAdapter.getSessionPresenter().endSession();
                        SessionActivity.super.onBackPressed();
                    }
                });
                mAlertBuilder.show();
            } else SessionActivity.super.onBackPressed();

        }
    }
}
