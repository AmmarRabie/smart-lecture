package cmp.sem.team8.smarlecture.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListFragment;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListPresenter;
import cmp.sem.team8.smarlecture.profile.ProfileActivity;
import cmp.sem.team8.smarlecture.session.PagerAdapter;
import cmp.sem.team8.smarlecture.statistics.GroupStatisticsActivity;

public class GroupActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    GroupPagerAdapter groupPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        tabLayout = (TabLayout) findViewById(R.id.group_pager_adapter_tabs);
        viewPager = (ViewPager) findViewById(R.id.group_view_pager);

        String groupId = getIntent().getStringExtra("group_id");
        String groupName = getIntent().getStringExtra("group_name");

        groupPagerAdapter = new GroupPagerAdapter(getSupportFragmentManager(), groupId, groupName);
        viewPager.setAdapter(groupPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_group, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
