package cmp.sem.team8.smarlecture.newsfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.data.mock.MockRepo;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

public class NewsFeedActivity extends AppCompatActivity {


    private NewsFeedPresenter newsFeedPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        NewsFeedFragment newsFeedFragment = (NewsFeedFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (newsFeedFragment == null) {
            newsFeedFragment = NewsFeedFragment.newInstance();


            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    newsFeedFragment, R.id.contentFrame);
        }

        // Create the presenter
        newsFeedPresenter = new NewsFeedPresenter(FirebaseAuthService.getInstance(), FirebaseRepository.getInstance(), newsFeedFragment);
    }
}
