package cmp.sem.team8.smarlecture.quickstatistics;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.auth.LoginActivity;
import cmp.sem.team8.smarlecture.session.SessionActivity;

public class QuickStatisticsActivity extends AppCompatActivity {

private Button BeginSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_statistics);


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        BeginSession=(Button)findViewById(R.id.button3);
        BeginSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),SessionActivity.class);
                startActivity(intent);
            }
        });
    }







}
