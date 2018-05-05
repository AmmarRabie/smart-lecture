package cmp.sem.team8.smarlecture.session.join.rateobjectives;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public class RateObjectivesActivity extends AppCompatActivity {

    private RateObjectivesPresenter mRateObjectivesPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rateobjectives);

        setTitle("Rate Objectives");

        String sessionID = getIntent().getStringExtra("session_id");

        RateObjectivesFragment rateObjectivesFragment = (RateObjectivesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (rateObjectivesFragment == null) {
            rateObjectivesFragment = RateObjectivesFragment.newInstace();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    rateObjectivesFragment, R.id.contentFrame);
        }
        mRateObjectivesPresenter = new RateObjectivesPresenter(rateObjectivesFragment, sessionID, FirebaseRepository.getInstance());


    }
}
