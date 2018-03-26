package cmp.sem.team8.smarlecture.group;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.group.groupFragment;
import cmp.sem.team8.smarlecture.group.groupPresenter;

public class groupActivity extends AppCompatActivity {



    private groupPresenter mGroupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupFragment groupfragment = (groupFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (groupfragment == null) {
            groupfragment = groupfragment.newInstance();


            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    groupfragment, R.id.contentFrame);
        }

        // Create the presenter
        mGroupPresenter = new groupPresenter(groupfragment);
    }
}
