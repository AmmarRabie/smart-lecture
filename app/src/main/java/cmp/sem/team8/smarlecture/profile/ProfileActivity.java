package cmp.sem.team8.smarlecture.profile;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

/**
 * The container of the fragment which implement the views
 * This should init the presenter and pass the instance to the fragment to do the tasks
 */
public class ProfileActivity extends AppCompatActivity {

    private ProfilePresenter mProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (profileFragment == null) {
            profileFragment = ProfileFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    profileFragment, R.id.contentFrame);
        }

        // Create the presenter
        mProfilePresenter = new ProfilePresenter(profileFragment);
    }
}