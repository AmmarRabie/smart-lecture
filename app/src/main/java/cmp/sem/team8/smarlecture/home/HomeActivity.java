package cmp.sem.team8.smarlecture.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.grades.GradesActivity;
import cmp.sem.team8.smarlecture.home.groups.GroupsPresenter;
import cmp.sem.team8.smarlecture.home.newsfeed.NewsFeedFragment;
import cmp.sem.team8.smarlecture.home.newsfeed.NewsFeedPresenter;
import cmp.sem.team8.smarlecture.profile.ProfileActivity;
import cmp.sem.team8.smarlecture.statistics.GroupStatisticsActivity;

/**
 * Created by Loai Ali on 5/5/2018.
 */

public class HomeActivity extends AppCompatActivity {
    private static final int INTENT_REQUEST_LOGIN = 1;
    private static final int INTENT_REQUEST_PROFILE = 2;
    ViewPager viewPager;
    TabLayout tabLayout;
    HomePagerAdapter homePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivityForResult(loginIntent, INTENT_REQUEST_LOGIN);

/*        homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(homePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);*/
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
                startActivityForResult(profileActivity, INTENT_REQUEST_PROFILE);
                return true;
            case R.id.optionGroupList_statistics:
                Intent GroupStatis = new Intent(this, GroupStatisticsActivity.class);
                GroupStatis.putExtra("GroupID", "-LBCAWGxSUtyqoeNIMYC");
                startActivity(GroupStatis);
                return true;
            case R.id.optionGroupList_grades:
                Intent Grade = new Intent(this, GradesActivity.class);
                Grade.putExtra("GroupID", "-LBCAWGxSUtyqoeNIMYC");
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
                if (homePagerAdapter == null) {
                    homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), 2);
                    viewPager.setAdapter(homePagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                }
                else
                {
                    FirebaseAuthService auth = FirebaseAuthService.getInstance();
                    FirebaseRepository repo = FirebaseRepository.getInstance();
                    homePagerAdapter.groupsPresenter = new GroupsPresenter(auth, homePagerAdapter.groupsFragment, repo);
                    homePagerAdapter.newsFeedPresenter = new NewsFeedPresenter(auth, repo, homePagerAdapter.newsFeedFragment);
                }
                break;
            case INTENT_REQUEST_PROFILE:
                if (resultCode == ProfileActivity.RESULT_SIGN_OUT) {
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    loginIntent.putExtra(getString(R.string.IKey_forceLogin), true);
                    startActivityForResult(loginIntent, INTENT_REQUEST_LOGIN);
                }
                break;
        }
    }

}
