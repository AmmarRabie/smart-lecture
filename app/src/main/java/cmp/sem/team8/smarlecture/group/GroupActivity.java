package cmp.sem.team8.smarlecture.group;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

public class GroupActivity extends AppCompatActivity {


    private GroupPresenter mGroupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        GroupFragment groupfragment = (GroupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (groupfragment == null) {
            groupfragment = GroupFragment.newInstance();


            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    groupfragment, R.id.contentFrame);
        }

        String groupId = getIntent().getStringExtra("group_id");
        // Create the presenter
        mGroupPresenter = new GroupPresenter(groupfragment, groupId);
    }
}
