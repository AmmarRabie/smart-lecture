package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;

public class WriteAttendanceFragment extends Fragment implements WriteAttendanceContract.Views
        , AdapterView.OnItemClickListener
        , ConnectionDetector.OnConnectionChangeListener {
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
    private TextView secrect;
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

        mSessionId = getActivity().getIntent().getStringExtra("sessionid");
        GroupId = getActivity().getIntent().getStringExtra("groupid");

        listView = (ListView) root.findViewById(R.id.attendanceFrag_list);
        secrect = (TextView) root.findViewById(R.id.attendanceFrag_secret);
        mTimerCountView = root.findViewById(R.id.attendanceFrag_timerCount);

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
//        mAdapter.(mPresenter.getAdapter());
        students = studentsList;
        mAdapter = new StudentAttendanceAdapter(getActivity(), students);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
/*        mAdapter.updateRecords(students);
        mAdapter.notifyDataSetChanged();*/
    }


    @Override
    public void showErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startAttendanceTimer(int seconds) {
        attendanceTimer = new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTimerCountView.setText("seconds remaining: " + millisUntilFinished / 1000);
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
//                AttendanceTimer.setText("done!");
                mPresenter.onConnectionTimeEnd();
            }
        }.start();
    }

    @Override
    public void stopAttendanceTimer() {
        attendanceTimer.cancel();
        mTimerCountView.setVisibility(View.GONE);
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
    public int getStudentId() {
        return listView.getCheckedItemPosition() + 1;
    }

    @Override
    public String getProvidedSecret() {
        return secrect.getText().toString();
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
            setTitle("Quickly");
//            setCancelable(false);
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
