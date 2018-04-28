package cmp.sem.team8.smarlecture.joinsession;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by ramym on 3/17/2018.
 */

public class JoinedSession extends AppCompatActivity {

    String GroupID;
    String SessionId;
    ViewPager viewPager;
    TabLayout tabLayout;
    TabItem WriteAttendanceTab;
    PagerAdapter pageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined__session);

        tabLayout = findViewById(R.id.joined_session_tablayout);
        WriteAttendanceTab = findViewById(R.id.joined_session_attendance);
        viewPager = findViewById(R.id.joined_session_viewPager);

        SessionId = getIntent().getStringExtra(getString(R.string.IKey_sessionId));
        GroupID = getIntent().getStringExtra(getString(R.string.IKey_groupId));

        pageAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), SessionId);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i =new Intent(context, RateObjectivesActivity.class);
        i.putExtra("session_id",SessionId);
        startActivity(i);
    }*/
}
