package cmp.sem.team8.smarlecture.session.beginattendance;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.view.SecretWheels;
import cmp.sem.team8.smarlecture.session.PagerAdapter;
import es.dmoral.toasty.Toasty;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendanceFragment extends Fragment implements BeginAttendanceContract.Views
,PagerAdapter.FragmentLifeCycle{

    private BeginAttendanceContract.Actions mPresenter;
    private TextView AttendanceTimer;
    private TextView secretView;
    private ListView listView;
    private Button startAttendance;
    private LinearLayout begin_attendance_secretview;
    private LinearLayout begin_attendance_remainingsecondsview;

    public static BeginAttendanceFragment newInstance() {
        return new BeginAttendanceFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public StudentsNamesAdapter getStudnetNameAdapter(List<String> students) {
        return new StudentsNamesAdapter(getActivity(), students);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.begin_attendace_fragment, container, false);

        begin_attendance_secretview = root.findViewById(R.id.begin_attendance_secretview);
        begin_attendance_remainingsecondsview = root.findViewById(R.id.begin_attendance_remainingsecondsview);
        AttendanceTimer = root.findViewById(R.id.attendance_timer);
        secretView = root.findViewById(R.id.begin_attendance_Secrect);
        final View changeSecretView = root.findViewById(R.id.beginAttendanceFragment_changeSecret);
        listView = root.findViewById(R.id.begin_attandence_list);
        startAttendance = root.findViewById(R.id.begin_attendance_start_attendance);
        startAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPresenter.isTaskIsRunning()) {
                    mPresenter.BeginAttendance();
                    changeSecretView.setVisibility(View.GONE);
                    startAttendance.setVisibility(View.GONE);
                }
            }
        });


        changeSecretView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogRootView = LayoutInflater.from(getContext()).
                        inflate(R.layout.dialog_change_secret, null);
                builder.setView(dialogRootView);
                builder.setTitle(getString(R.string.mes_enterPin));
                final SecretWheels secretWheelsView = dialogRootView.findViewById(R.id.dialogChangeSecret_secretWheels);
                builder.setPositiveButton(getString(R.string.dAction_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        secretView.setText(secretWheelsView.getSecret());
                        dialogInterface.dismiss();
                    }
                });
                View mixView = dialogRootView.findViewById(R.id.dialogChangeSecret_mix);
                mixView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        secretWheelsView.mixWheels(1000);
                    }
                });
                builder.setNegativeButton(getString(R.string.dAction_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });

        return root;
    }

    @Override
    public void setPresenter(BeginAttendanceContract.Actions presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showErrorMessage(String cause) {
        Toasty.error(getContext(), cause, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showBeginAttendaceButton() {
        startAttendance.setVisibility(View.VISIBLE);
        begin_attendance_secretview.setVisibility(View.VISIBLE);
        begin_attendance_remainingsecondsview.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideBeginAttendaceButton() {
        startAttendance.setVisibility(View.GONE);
        begin_attendance_secretview.setVisibility(View.GONE);
        begin_attendance_remainingsecondsview.setVisibility(View.GONE);

    }

    @Override
    public void showProgressIndicator(int minutes) {

        new CountDownTimer(minutes * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                AttendanceTimer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                AttendanceTimer.setText(getString(R.string.mes_done));
                mPresenter.endAttendance();
            }
        }.start();
    }

    @Override
    public void showSecrect(String secret) {

        secretView.setText(secret);
    }

    @Override
    public void listViewSetAdapter(StudentsNamesAdapter adapter) {

        listView.setAdapter(adapter);
    }

    @Override
    public String getSecret() {
        return secretView.getText().toString();
    }

    @Override
    public void onPauseFragment() {
        super.onPause();


    }

    @Override
    public void onResumeFragment() {
        super.onResume();
        mPresenter.start();

    }
}
