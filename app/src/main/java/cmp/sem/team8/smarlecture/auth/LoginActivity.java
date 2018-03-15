package cmp.sem.team8.smarlecture.auth;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

/**
 * The container of the fragment which implement the views
 * This should init the presenter and pass the instance to the fragment to do the tasks
 */
public class LoginActivity extends AppCompatActivity {

    public static final int RC_SIGN_UP = 4646;
    private LoginPresenter mAddEditTaskPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.contentFrame);
        }

        // Create the presenter
        mAddEditTaskPresenter = new LoginPresenter(loginFragment);
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        // don't end the activity
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_UP && resultCode == RESULT_OK) {
            finish();
        }


    }
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setResult(resultCode); // to be delivered to the MainActivity
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_UP && resultCode == RESULT_OK) {
            // Successfully signed in, insert the user into database
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (cmp.sem.team8.fireapp.ui.Controller.signUp(user.getUid(), user.getDisplayName(), user.getEmail())) {
                UserModel userModel = new UserModel(user.getDisplayName(), user.getEmail(), user.getUid());
                data.getExtras().putSerializable("user", userModel);
                setIntent(data);
                finish();
            }
        } else {

            // Sign in failed

            String detailedMes = "";
            if (response != null && response.getError() != null)
                detailedMes = response.getError().getMessage();

            Toast.makeText(this, "can't sign in " + detailedMes, Toast.LENGTH_SHORT)
                    .show();
        }


    }*/
}
