package cmp.sem.team8.smarlecture.auth;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

public class SignUpActivity extends AppCompatActivity {

    private SignUpContract.Actions mSignUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SignUpFragment signUpFragment = (SignUpFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (signUpFragment == null) {
            signUpFragment = SignUpFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    signUpFragment, R.id.contentFrame);
        }

        // Create the presenter
        mSignUpPresenter = new SignUpPresenter(signUpFragment);

    }
}
