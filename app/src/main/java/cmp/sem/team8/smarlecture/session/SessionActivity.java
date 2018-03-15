package cmp.sem.team8.smarlecture.session;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cmp.sem.team8.fireapp.R;
import cmp.sem.team8.fireapp.common.util.ActivityUtils;
import cmp.sem.team8.fireapp.session.attendance.AttendanceFragment;
import cmp.sem.team8.fireapp.session.attendance.AttendancePresenter;


public class SessionActivity extends AppCompatActivity {

/*    private Button mQuestionButton;
    private Button mAttendanceButton;
    private Button mObjectivesButton;*/

    private AttendancePresenter mAttendancePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

/*        mQuestionButton = findViewById(R.id.sessionActivity_question);
        mAttendanceButton = findViewById(R.id.sessionActivity_attendance);
        mObjectivesButton = findViewById(R.id.sessionActivity_objectives);*/
        mAttendancePresenter = null;

    }


    public void onToFragmentClick(View view) {
        int viewId = view.getId();

        int fragmentId = R.id.contentFrame;
        switch (viewId) {
            case R.id.sessionActivity_question:
                Toast.makeText(this, "not supported yet", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sessionActivity_attendance:
                AttendanceFragment fragment = (AttendanceFragment)
                        getSupportFragmentManager().findFragmentById(R.id.contentFrame);
                fragment = fragment == null ?
                        AttendanceFragment.newInstance()
                        : fragment;
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, fragmentId);

                if (mAttendancePresenter == null)
                    mAttendancePresenter = new AttendancePresenter(fragment, false, 1);
                else
                    mAttendancePresenter.refresh();
                break;

            case R.id.sessionActivity_objectives:
                Toast.makeText(this, "not supported yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
