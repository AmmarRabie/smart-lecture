package cmp.sem.team8.smarlecture.session;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.session.sessioninfo.SessionInfoPresenter;

public class SessionActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem SessionTab;
    TabItem AttendanceTab;
    PagerAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

/*        mQuestionButton = findViewById(R.id.sessionActivity_question);
        mAttendanceButton = findViewById(R.id.sessionActivity_attendance);
        mObjectivesButton = findViewById(R.id.sessionActivity_objectives);*/


        tabLayout = findViewById(R.id.tablayout);
        SessionTab = findViewById(R.id.start_end_session_tab);
        AttendanceTab = findViewById(R.id.begin_attandence_tab);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                getIntent().getStringExtra("group_id"), getIntent().getStringExtra("session_id"));

        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
            if(sessionInfoPresenter.getSessionStatus().equals(FirebaseContract.SessionEntry.SessionStatus.OPEN.toString())) {

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
            }
            else SessionActivity.super.onBackPressed();

        }
    }
}
