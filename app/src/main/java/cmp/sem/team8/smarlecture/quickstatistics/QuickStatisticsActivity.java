package cmp.sem.team8.smarlecture.quickstatistics;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.profile.ProfileActivity;

public class QuickStatisticsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_statistics);


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public void testProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    public void testForceLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(getResources().getString(R.string.intentKey_forceLogin),
                true);
        startActivity(intent);
    }


}
