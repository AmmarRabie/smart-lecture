package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.auth.firebase.FirebaseAuthService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseRepository;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;

public class WriteAttendanceActivity extends AppCompatActivity {

    private WriteAttendancePresenter mPresenter;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_attendance);

        sessionId = getIntent().getStringExtra(getString(R.string.IKey_sessionId));

        WriteAttendanceFragment writeAttendanceFragment = (WriteAttendanceFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (writeAttendanceFragment == null) {
            writeAttendanceFragment = WriteAttendanceFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    writeAttendanceFragment, R.id.contentFrame);
        }
        mPresenter = new WriteAttendancePresenter(writeAttendanceFragment, FirebaseRepository.getInstance(), FirebaseAuthService.getInstance(), sessionId);
    }

    @Override
    public void onBackPressed() {
    }
}
