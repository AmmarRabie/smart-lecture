package cmp.sem.team8.smarlecture.home.groups;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.grades.GradesActivity;
import cmp.sem.team8.smarlecture.profile.ProfileActivity;
import cmp.sem.team8.smarlecture.statistics.GroupStatisticsActivity;

public class GroupsActivity extends AppCompatActivity {


    private static final int INTENT_REQUEST_LOGIN = 1;
    private GroupsPresenter mGroupsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouplist);
        setTitle("Groups");
        Intent i = new Intent(this, LoginActivity.class);
        startActivityForResult(i, INTENT_REQUEST_LOGIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_grouplist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionGroupList_joinSession:
                return false; // make the fragment handle this instead
            case R.id.optionGroupList_profile:
                Intent profileActivity = new Intent(this, ProfileActivity.class);
                startActivity(profileActivity);
                return true;
            case R.id.optionGroupList_statistics:
                Intent GroupStatis = new Intent(this, GroupStatisticsActivity.class);
                GroupStatis.putExtra("GroupID","-LBCAWGxSUtyqoeNIMYC");
                startActivity(GroupStatis);
                return true;
            case R.id.optionGroupList_grades:
                Intent Grade = new Intent(this, GradesActivity.class);
                Grade.putExtra("GroupID","-LBCAWGxSUtyqoeNIMYC");
                startActivity(Grade);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENT_REQUEST_LOGIN:
                if (resultCode != RESULT_OK) {
                    // login cancelled, end the application
                    finish();
                    return;
                }
                GroupsFragment grouplistfragment = (GroupsFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);

                if (grouplistfragment == null) {
                    grouplistfragment = GroupsFragment.newInstance();
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                            grouplistfragment, R.id.contentFrame);
                }
                mGroupsPresenter = new GroupsPresenter(FirebaseAuthService.getInstance(), grouplistfragment, FirebaseRepository.getInstance());
                return;
        }
    }
}
