package cmp.sem.team8.smarlecture.group;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListFragment;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListPresenter;
import cmp.sem.team8.smarlecture.session.PagerAdapter;

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




        groupPagerAdapter = new GroupPagerAdapter(getSupportFragmentManager(),groupId,groupName);

        viewPager.setAdapter(groupPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

    }

  /*  private StudentListPresenter mStudentListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        StudentListFragment groupfragment = (StudentListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (groupfragment == null) {
            groupfragment = StudentListFragment.newInstance();


            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    groupfragment, R.id.contentFrame);
        }

        String groupId = getIntent().getStringExtra("group_id");
        String groupName = getIntent().getStringExtra("group_name");
        // Create the presenter
        mStudentListPresenter = new StudentListPresenter(groupfragment, groupId, groupName);
    }*/
}
