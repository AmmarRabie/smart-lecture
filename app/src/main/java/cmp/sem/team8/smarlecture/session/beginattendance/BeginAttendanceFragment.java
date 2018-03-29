package cmp.sem.team8.smarlecture.session.beginattendance;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendanceFragment extends Fragment implements BeginAttendanceContract.Views{

    private BeginAttendanceContract.Actions mPresenter;
    private TextView AttendanceTimer;
    private TextView SecrectText;
    private ListView listView;
    private Button startAttendance;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.begin_attendace_fragment, container, false);

        AttendanceTimer=root.findViewById(R.id.attendance_timer);
        SecrectText=root.findViewById(R.id.begin_attendance_Secrect);
        listView=root.findViewById(R.id.begin_attandence_list);
        startAttendance=root.findViewById(R.id.begin_attendance_start_attendance);
        startAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPresenter.isTaskIsRunning())
                    mPresenter.BeginAttendance();
            }
        });



        return root;
    }

    @Override
    public void setPresenter(BeginAttendanceContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showOnCompleteFetchDataMessage() {

    }

    @Override
    public void showErrorMessage(String cause) {

    }

    @Override
    public void showProgressIndicator(int minutes) {

        new CountDownTimer(minutes*60*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                AttendanceTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                AttendanceTimer.setText("done!");
                mPresenter.endAttendance();
            }
        }.start();
    }

    @Override
    public void showSecrect(int Secrect) {

        SecrectText.setText("Secrect :"+Integer.toString(Secrect));
    }

    @Override
    public void hideProgressIndicator() {

    }

    @Override
    public void showAttendanceList(List<Map> attendanceList) {

    }

    @Override
    public boolean getConnectionState(boolean includeInternet, boolean includeSim) {
        return false;
    }

    @Override
    public void listViewSetAdapter(StudentsNamesAdapter adapter) {

        listView.setAdapter(adapter);
    }

    public static BeginAttendanceFragment newInstance() {
        return new BeginAttendanceFragment();
    }

}
