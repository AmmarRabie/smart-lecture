package cmp.sem.team8.smarlecture.joinsession.writeattendance;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.view.SecretWheels;
import cmp.sem.team8.smarlecture.common.data.model.UserAttendanceModel;
import es.dmoral.toasty.Toasty;

public class WriteAttendanceFragment extends Fragment implements WriteAttendanceContract.Views
        , AdapterView.OnItemClickListener
        , ConnectionDetector.OnConnectionChangeListener, View.OnClickListener {
    private static final String TAG = "WriteAttendanceFragment";
    private CountDownTimer connectionTimer;
    private CountDownTimer attendanceTimer;
    private String mSessionId;
    private String GroupId;
    private List<UserAttendanceModel> students;
    private StudentAttendanceAdapter mAdapter;
    private int PreSelectedIndex = -1;
    private WriteAttendanceContract.Actions mPresenter;
    private ConnectionDetector mConnectionDetector;
    private ListView listView;
    private TextView secret;
    private View mChangeSecretView;
    private ProgressBar mProgressBar;
    private TextView mTimerCountView;
    private TimerAlertDialog dialog = null;


    public WriteAttendanceFragment() {
    }

    public static WriteAttendanceFragment newInstance() {
        return new WriteAttendanceFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_attendance, container, false);

        mSessionId = getActivity().getIntent().getStringExtra(getString(R.string.IKey_sessionId));
        GroupId = getActivity().getIntent().getStringExtra(getString(R.string.IKey_groupId));

        listView = root.findViewById(R.id.attendanceFrag_list);
        secret = root.findViewById(R.id.attendanceFrag_secret);
        mChangeSecretView = root.findViewById(R.id.attendanceFrag_changeSecret);
        mChangeSecretView.setOnClickListener(this);
        mTimerCountView = root.findViewById(R.id.attendanceFrag_timerCount);
        mProgressBar = root.findViewById(R.id.attendanceFrag_timerProgress);

        students = new ArrayList<>();
        mAdapter = new StudentAttendanceAdapter(getActivity(), students);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mPresenter.getStudentsList(GroupId, mSessionId);

        mConnectionDetector = new ConnectionDetector(getContext(), this);
        return root;
    }

    @Override
    public void setPresenter(WriteAttendanceContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showStudentsList(List<UserAttendanceModel> studentsList) {
        students = studentsList;
        mAdapter = new StudentAttendanceAdapter(getActivity(), students);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void showErrorMessage(String cause) {
        Toasty.error(getContext(), cause, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void showInfoMessage(String info) {
        Toasty.info(getContext(), info, Toast.LENGTH_SHORT, true).show();

    }

    @Override
    public void showSuccessMessage(String successMes) {
        Toasty.success(getContext(), successMes, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void startAttendanceTimer(int seconds) {
        mProgressBar.setMax(seconds);
        mProgressBar.setProgress(mProgressBar.getMax());
        mProgressBar.setVisibility(View.VISIBLE);
        mTimerCountView.setVisibility(View.VISIBLE);
        attendanceTimer = new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                mTimerCountView.setText("" + seconds); // seconds remaining
                mProgressBar.setProgress(((int) seconds));
            }

            public void onFinish() {
//                AttendanceTimer.setText("done!");
                mPresenter.onAttendanceTimeEnd();
            }
        }.start();
    }

    @Override
    public void startConnectionTimer(int seconds) {
        dialog = new TimerAlertDialog(getContext());
        dialog.show();
        connectionTimer = new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                dialog.setTime(millisUntilFinished / 1000);
            }

            public void onFinish() {
                mPresenter.onConnectionTimeEnd();
            }
        }.start();
    }

    @Override
    public void stopAttendanceTimer() {
        attendanceTimer.cancel();
        mTimerCountView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void stopConnectionTimer() {
        connectionTimer.cancel();
        dialog.dismiss();
    }

    @Override
    public void requestDisableConnection() {
        mConnectionDetector.start();
    }

    @Override
    public String getStudentId() {
        //return (listView.getCheckedItemPosition());
       UserAttendanceModel model= students.get(listView.getCheckedItemPosition());
       return model.getKey();
    }

    @Override
    public String getProvidedSecret() {
        return secret.getText().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // insure that mConnectionDetector ends, so that receivers unregistered successfully
        mConnectionDetector.end();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        UserAttendanceModel model = students.get(position);
        model.setChecked(true);
        students.set(position, model);
        if (PreSelectedIndex > -1) {
            UserAttendanceModel pModel = students.get(PreSelectedIndex);
            pModel.setChecked(false);
            students.set(PreSelectedIndex, pModel);
        }
        PreSelectedIndex = position;
        mAdapter.updateRecords(students);

    }

    @Override
    public void onConnectionLost() {
        Log.i(TAG, "onConnectionLost: connection lost");
        mPresenter.onConnectionLost();
    }

    @Override
    public void onConnectionBack() {
        Log.i(TAG, "onConnectionBack: ");
        mPresenter.onConnectionBack();
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogRootView = LayoutInflater.from(getContext()).
                inflate(R.layout.dialog_change_secret, null);
        builder.setView(dialogRootView);
        builder.setTitle(R.string.dTitle_enterPin);
        final SecretWheels secretWheels = dialogRootView.findViewById(R.id.dialogChangeSecret_secretWheels);
        View mixView = dialogRootView.findViewById(R.id.dialogChangeSecret_mix);
        mixView.setVisibility(View.GONE);
        builder.setPositiveButton(getString(R.string.dAction_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                secret.setText(secretWheels.getSecret());
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

    // [TODO] refactor the class to extend the AlertDialog and has a reference to the builder, not vice versa
    private class TimerAlertDialog extends AlertDialog.Builder {
        private TextView mTimeView;
        private AlertDialog dialog;

        TimerAlertDialog(@NonNull Context context) {
            super(context);
            View view = getLayoutInflater().inflate(R.layout.dialog_timer, null);
//            setView(R.layout.dialog_timer);
            setView(view);
            setIcon(android.R.drawable.ic_dialog_alert);
            setTitle(getString(R.string.dTitle_closeConnection));
            setMessage(getString(R.string.dMes_connection));
            setCancelable(false);
            mTimeView = view.findViewById(R.id.dialogTimer_time);
        }

        @Override
        public AlertDialog show() {
            dialog = super.show();
            return dialog;

        }

        void setTime(long seconds) {
            if (seconds <= 8)
                mTimeView.setTextColor(ContextCompat.getColor(this.getContext(),
                        R.color.timeBelow8));
            else
                mTimeView.setTextColor(ContextCompat.getColor(this.getContext(),
                        R.color.timeUp8));
            mTimeView.setText(seconds + "");
        }

        void dismiss() {
            dialog.dismiss();
        }
    }
}
