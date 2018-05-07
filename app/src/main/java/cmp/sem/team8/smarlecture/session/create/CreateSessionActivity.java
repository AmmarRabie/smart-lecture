package cmp.sem.team8.smarlecture.session.create;


import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.session.create.members.MembersFragment;

public class CreateSessionActivity extends AppCompatActivity implements MembersFragment.MembersFragmentCallbacks {

    ViewPager viewPager;
    TabLayout tabLayout;
    CreateSessionPagerAdapter pageAdapter;
    String mGroupID;
    String mSessionID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroupID = getIntent().getStringExtra(getString(R.string.IKey_groupId));
        mSessionID = getIntent().getStringExtra(getString(R.string.IKey_sessionId));
        setContentView(R.layout.activity_session);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new CreateSessionPagerAdapter(getSupportFragmentManager(), 4, mGroupID
                , mSessionID);

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) // members
                {
                    pageAdapter.membersPresenter.start();
                }
/*                if (((MembersFragment) pageAdapter.getItem(position)) MembersFragment)
                    pageAdapter.membersPresenter.start();

                if (pageAdapter.getItem(position) instanceof ObjectivesFragment)
                    pageAdapter.objectivesPresenter.start();*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_session_owner, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    //********** onBackPressed  ****************
    // to close session session activity and return back to the previous activity.
    // steps
    // 1- warning message before close
    // 2- end session before closing the activity
    // 3-
    @Override
    public void onBackPressed() {
        if (!(viewPager.getCurrentItem() == 0)) {
            viewPager.setCurrentItem(0, true);
            return;
        }
        if (pageAdapter.membersPresenter.getAttendanceStatus().equals(DataService.AttendanceStatus.OPEN)){
            AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(this);
            mAlertBuilder.setTitle("Confirmation");
            mAlertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            mAlertBuilder.setMessage("Close Attendance and exit");
            mAlertBuilder.setPositiveButton("close attendance", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pageAdapter.membersPresenter.onDestroy();
                    CreateSessionActivity.super.onBackPressed();
                }
            });
            mAlertBuilder.show();
        } else super.onBackPressed();
    }

    @Override
    public void setColor(int color) {
        tabLayout.setBackgroundColor(getResources().getColor(color));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(color)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
}
