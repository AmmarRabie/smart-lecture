package cmp.sem.team8.smarlecture.session.create;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.group.members.MembersFragment;
import cmp.sem.team8.smarlecture.session.create.info.InfoPresenter;
import cmp.sem.team8.smarlecture.session.create.objectives.ObjectivesFragment;

public class CreateSessionActivity extends AppCompatActivity {

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
                if (position == 1) // members
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

    //********** onBackPressed  ****************
    // to close session session activity and return back to the previous activity.
    // steps
    // 1- warning message before close
    // 2- end session before closing the activity
    // 3-
    @Override
    public void onBackPressed() {
        InfoPresenter infoPresenter = pageAdapter.infoPresenter;
        if (infoPresenter == null || infoPresenter.getSessionStatus() == null) {
            super.onBackPressed();
            return;
        }
        if (infoPresenter.getSessionStatus().equals(DataService.SessionStatus.OPEN)) {
            AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(this);
            mAlertBuilder.setTitle("Confirmation");
            mAlertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            mAlertBuilder.setMessage("The session will be ended. are you sure to continue");
            mAlertBuilder.setPositiveButton("End Session", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pageAdapter.infoPresenter.endSession();
                    CreateSessionActivity.super.onBackPressed();
                }
            });
            mAlertBuilder.show();
        } else super.onBackPressed();
    }
}
