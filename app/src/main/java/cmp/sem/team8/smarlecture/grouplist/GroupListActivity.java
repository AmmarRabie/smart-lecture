package cmp.sem.team8.smarlecture.grouplist;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.group.GroupActivity;

public class GroupListActivity extends AppCompatActivity {


    private GroupListPresenter mGroupListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist);
        GroupListFragment grouplistfragment = (GroupListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if(grouplistfragment==null){
            grouplistfragment=GroupListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    grouplistfragment, R.id.contentFrame);
        }

        mGroupListPresenter=new GroupListPresenter(grouplistfragment);
    }



    public void test(View view)
    {
        Intent intent = new Intent(this,GroupActivity.class);
        intent.putExtra("group_key","id1");
        startActivity(intent);
    }



}
