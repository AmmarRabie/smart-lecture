package cmp.sem.team8.smarlecture.joinsession;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.joinsession.writeattendance.AttendanceMonitorService;

/**
 * Created by ramym on 3/17/2018.
 */

public class JoinedSession extends AppCompatActivity {

    String GroupID;
    String SessionId;
    ViewPager viewPager;
    TabLayout tabLayout;
    JoinSessionPagerAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined__session);

        tabLayout = findViewById(R.id.joined_session_tablayout);
        viewPager = findViewById(R.id.joined_session_viewPager);


        SessionId = getIntent().getStringExtra(getString(R.string.IKey_sessionId));
        GroupID = getIntent().getStringExtra(getString(R.string.IKey_groupId));
        boolean isMember = getIntent().getBooleanExtra(getString(R.string.IKey_isMember), true); // default is true

        pageAdapter = new JoinSessionPagerAdapter(getSupportFragmentManager(), 2, SessionId,GroupID);
        viewPager.setAdapter(pageAdapter);


        tabLayout.setupWithViewPager(viewPager);

        if (!isMember)
            return;
        Intent service = new Intent(this, AttendanceMonitorService.class);
        service.putExtra(getString(R.string.IKey_sessionId), SessionId);
        startService(service);
    }
}
