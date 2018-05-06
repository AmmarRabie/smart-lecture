package cmp.sem.team8.smarlecture.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.grades.GradesActivity;
import cmp.sem.team8.smarlecture.statistics.GroupStatisticsActivity;

public class GroupActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private GroupPagerAdapter groupPagerAdapter;
    private String groupId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        tabLayout = (TabLayout) findViewById(R.id.group_pager_adapter_tabs);
        viewPager = (ViewPager) findViewById(R.id.group_view_pager);

        groupId = getIntent().getStringExtra("group_id");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionGroup_exportToExcel:
                return false; // let the frag handle it
            case R.id.optionGroup_grades:
                Intent gradesIntent = new Intent(this, GradesActivity.class);
                gradesIntent.putExtra(getString(R.string.IKey_groupId), groupId);
                startActivity(gradesIntent);
                return true;
            case R.id.optionGroup_statistics:
                Intent statisticsIntent = new Intent(this, GroupStatisticsActivity.class);
                statisticsIntent.putExtra(getString(R.string.IKey_groupId), groupId);
                startActivity(statisticsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
