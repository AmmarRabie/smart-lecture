package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
        , AdapterView.OnItemClickListener, ConnectionDetector.OnConnectionChangeListener {

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
//        takeAttendanceButton = (Button) root.findViewById(R.id.attendanceFrag_takeAttendance);
        mTimerCountView = root.findViewById(R.id.attendanceFrag_timerCount);

        students = new ArrayList<>();
        mAdapter = new StudentAttendanceAdapter(getActivity(), students);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//        listView.setOnItemClickListener(this);

//        takeAttendanceButton.setOnClickListener(this);

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
    public void endConnection() {
        /*setAirplaneMode(false);*/
    }

    @Override
    public void closeDialog() {
        /*setAirplaneMode(true);*/
        dialog.dismiss();
    }

    private void setAirplaneMode(boolean Enable) {
        // read the airplane mode setting
        boolean isEnabled = Settings.System.getInt(
                getContext().getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;

        if (isEnabled)
            return;

        // toggle airplane mode on
        Settings.System.putInt(
                getContext().getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, Enable ? 1 : 0);

        // Post an intent to reload
        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intent.putExtra("state", Enable);
        getContext().sendBroadcast(intent);
    }


    @Override
    public void showErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateEndConnectionRemainingTime(int seconds) {
/*        if (dialog == null) // first-time calling
        {
            dialog = new TimerAlertDialog(getContext());
        }*/
        dialog.setTime(seconds);
        if (seconds == 0)
            dialog.dismiss();
    }

    @Override
    public void requestDisableConnection() {
        dialog = new TimerAlertDialog(getContext());
        dialog.show();
        mConnectionDetector.start();
    }

    @Override
    public void updateAttendanceRemainingTime(int seconds) {
        mTimerCountView.setText("" + seconds);
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
        mPresenter.onConnectionLost();
    }

    @Override
    public void onConnectionBack() {
        mPresenter.onConnectionBack();
    }


    // [TODO] refactor the class to extend the AlertDialog and has a reference to the builder, not vice versa
    private class TimerAlertDialog extends AlertDialog.Builder {
        private TextView mTimeView;
        private AlertDialog dialog;

        TimerAlertDialog(@NonNull Context context) {
            super(context);
            setView(R.layout.dialog_timer);
            setIcon(android.R.drawable.ic_dialog_alert);
            setTitle("Quickly");
//            setCancelable(false);
            mTimeView = getView().findViewById(R.id.dialogTimer_time);
        }

        @Override
        public AlertDialog show() {
            dialog = super.show();
            return dialog;
        }

        public void setTime(int seconds) {
            if (seconds <= 8)
                mTimeView.setTextColor(ContextCompat.getColor(this.getContext(),
                        R.color.timeBelow8));
            else
                mTimeView.setTextColor(ContextCompat.getColor(this.getContext(),
                        R.color.timeUp8));
            mTimeView.setText(seconds);
        }

        public void dismiss() {
            dialog.dismiss();
        }

    }
}
