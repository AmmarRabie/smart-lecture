package cmp.sem.team8.smarlecture.auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

/**
 * The container of the fragment which implement the views
 * This should init the presenter and pass the instance to the fragment to do the tasks
 */
public class LoginActivity extends AppCompatActivity {

    public static final int RC_SIGN_UP = 4646;
    private LoginPresenter mLoginPresenter;
    private boolean mForceLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);


        mForceLogin = getIntent().getBooleanExtra(
                getResources().getString(R.string.IKey_forceLogin),
                false);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.contentFrame);
        }

        // Create the presenter
        mLoginPresenter = new LoginPresenter(FirebaseRepository.getInstance(),loginFragment, mForceLogin);
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void onCreateAccountClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, RC_SIGN_UP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_UP && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
