package cmp.sem.team8.smarlecture.joinsession;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendanceFragment;
import cmp.sem.team8.smarlecture.session.beginattendance.BeginAttendancePresenter;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionFragment;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionPresenter;

/**
 * Created by ramym on 3/17/2018.
 */

public class JoinedSession extends AppCompatActivity {

    private Button mQuestionButton;
    private Button mAttendanceButton;
    private Button mObjectivesButton;
    private Button mStartSessionButton;

    int SessionId;

    private BeginAttendancePresenter mAttendancePresenter;
    private StartSessionPresenter    mStartSessionPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined__session);

/*        mQuestionButton = findViewById(R.id.sessionActivity_question);
        mAttendanceButton = findViewById(R.id.sessionActivity_attendance);
        mObjectivesButton = findViewById(R.id.sessionActivity_objectives);*/

        mAttendancePresenter = null;
        mStartSessionPresenter=null;
        //
        // begin the session with generating the session.
        int fragmentId = R.id.contentFrame2;
        StartSessionFragment fragment2 = (StartSessionFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame2);
        fragment2 = fragment2 == null ?
                StartSessionFragment.newInstance()
                : fragment2;
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

        mStartSessionPresenter = new StartSessionPresenter(fragment2);


        /////

    }


    public void onToFragmentClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            // case R.id.sessionActivity_startsession:
            //    StartSessionFragment fragment2 = (StartSessionFragment)
            //           getSupportFragmentManager().findFragmentById(R.id.contentFrame2);
            //    fragment2 = fragment2 == null ?
            //            StartSessionFragment.newInstance()
            //            : fragment2;
            //    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

            //    if (mStartSessionPresenter == null)
            //        mStartSessionPresenter = new StartSessionPresenter(fragment2);
            //    else
            //        mStartSessionPresenter.refresh();
            //    break;

            case R.id.sessionActivity_endsession:
                mStartSessionPresenter.endSession();
                mStartSessionPresenter=null;
                break;

            case R.id.sessionActivity_attendance:
                int fragmentId = R.id.contentFrame3;
                BeginAttendanceFragment fragment2 = (BeginAttendanceFragment)
                        getSupportFragmentManager().findFragmentById(R.id.contentFrame3);
                fragment2 = fragment2 == null ?
                        BeginAttendanceFragment.newInstance()
                        : fragment2;
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment2, fragmentId);

                if (mAttendancePresenter == null)
                    mAttendancePresenter = new BeginAttendancePresenter(fragment2,2790306);
                else
                    mAttendancePresenter.refresh();
                break;

            ///////////

        }
    }
}
