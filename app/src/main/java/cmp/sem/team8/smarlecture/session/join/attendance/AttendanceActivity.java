package cmp.sem.team8.smarlecture.session.join.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

public class AttendanceActivity extends AppCompatActivity {

    private AttendancePresenter mPresenter;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_attendance);

        // make sure that the service is stopped, so that this activity doesn't fire again
        stopService(new Intent(this, AttendanceMonitorService.class));

        sessionId = getIntent().getStringExtra(getString(R.string.IKey_sessionId));

        AttendanceFragment attendanceFragment = (AttendanceFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (attendanceFragment == null) {
            attendanceFragment = AttendanceFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    attendanceFragment, R.id.contentFrame);
        }
        mPresenter = new AttendancePresenter(attendanceFragment, FirebaseRepository.getInstance(), FirebaseAuthService.getInstance(), sessionId);
    }

    @Override
    public void onBackPressed() {
    }
}
