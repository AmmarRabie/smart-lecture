package cmp.sem.team8.smarlecture.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.util.SharedPrefUtil;
import cmp.sem.team8.smarlecture.home.groups.GroupsPresenter;
import cmp.sem.team8.smarlecture.home.newsfeed.NewsFeedPresenter;
import cmp.sem.team8.smarlecture.intro.IntroActivity;
import cmp.sem.team8.smarlecture.invitations.InvitationsActivity;
import cmp.sem.team8.smarlecture.profile.ProfileActivity;

/**
 * Created by Loai Ali on 5/5/2018.
 */

public class HomeActivity extends AppCompatActivity {
    private static final int INTENT_REQUEST_LOGIN = 1;
    private static final int INTENT_REQUEST_PROFILE = 2;
    private static final int INTENT_REQUEST_INTRO = 3;
    ViewPager viewPager;
    TabLayout tabLayout;
    HomePagerAdapter homePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);

        if (SharedPrefUtil.isFirstTimeOpenApp(this)) {
            Intent introIntent = new Intent(this, IntroActivity.class);
            startActivityForResult(introIntent, INTENT_REQUEST_INTRO);
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivityForResult(loginIntent, INTENT_REQUEST_LOGIN);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_grouplist, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionHome_profile:
                Intent profileActivity = new Intent(this, ProfileActivity.class);
                startActivityForResult(profileActivity, INTENT_REQUEST_PROFILE);
                return true;
            case R.id.optionHome_invitations:
                Intent invitationsIntent = new Intent(this, InvitationsActivity.class);
                startActivity(invitationsIntent);
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
                } else {
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
            case INTENT_REQUEST_INTRO:
                if (resultCode == RESULT_OK) {
                    // Finished the intro
                    SharedPrefUtil.openAppOneTime(this);
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivityForResult(loginIntent, INTENT_REQUEST_LOGIN);
                } else {
                    // Cancelled the intro. You can then e.g. finish this activity too.
                    finish();
                }
                break;
        }
    }

}
