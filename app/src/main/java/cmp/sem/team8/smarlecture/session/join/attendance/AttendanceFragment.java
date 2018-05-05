package cmp.sem.team8.smarlecture.session.join.attendance;


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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.view.SecretWheels;
import es.dmoral.toasty.Toasty;

public class AttendanceFragment extends Fragment implements AttendanceContract.Views
        , ConnectionDetector.OnConnectionChangeListener, View.OnClickListener {
    private static final String TAG = "AttendanceFragment";

    private CountDownTimer connectionTimer;
    private CountDownTimer attendanceTimer;
    private AttendanceContract.Actions mAction;
    private ConnectionDetector mConnectionDetector;

    // views
    private TextView secretView;
    private View mChangeSecretView;
    private ProgressBar mAttendanceTimerProgressView;
    private TextView mTimerCountView;
    private TimerAlertDialog mConnectionDialogView;
    private ViewGroup instructionsContainerView;


    public AttendanceFragment() {
    }

    public static AttendanceFragment newInstance() {
        return new AttendanceFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_attendance, container, false);

        secretView = root.findViewById(R.id.attendanceFrag_secret);
        mChangeSecretView = root.findViewById(R.id.attendanceFrag_changeSecret);
        mChangeSecretView.setOnClickListener(this);
        mTimerCountView = root.findViewById(R.id.attendanceFrag_timerCount);
        mAttendanceTimerProgressView = root.findViewById(R.id.attendanceFrag_timerProgress);
        instructionsContainerView = root.findViewById(R.id.attendanceFrag_instructionsContainer);

        final View verifySecretView = root.findViewById(R.id.attendanceFrag_verifySecret);
        verifySecretView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAction.verifySecret(getProvidedSecret()))
                {
                    verifySecretView.setVisibility(View.GONE);
                    mChangeSecretView.setVisibility(View.GONE);
                }
            }
        });

        mConnectionDetector = new ConnectionDetector(getContext(), this);
        return root;
    }

    @Override
    public void setInstructionSuccess(int instruction) {
        View view = instructionsContainerView.getChildAt(instruction - 1);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(AttendanceContract.Actions presenter) {
        mAction = presenter;
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
        mAttendanceTimerProgressView.setMax(seconds);
        mAttendanceTimerProgressView.setProgress(mAttendanceTimerProgressView.getMax());
        mAttendanceTimerProgressView.setVisibility(View.VISIBLE);
        mTimerCountView.setVisibility(View.VISIBLE);
        attendanceTimer = new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                mTimerCountView.setText("" + seconds); // seconds remaining
                mAttendanceTimerProgressView.setProgress(((int) seconds));
            }

            public void onFinish() {
                mAction.onAttendanceTimeEnd();
            }
        }.start();
    }

    @Override
    public void startConnectionTimer(int seconds) {
        mConnectionDialogView = new TimerAlertDialog(getContext());
        mConnectionDialogView.show();
        connectionTimer = new CountDownTimer(seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                mConnectionDialogView.setTime(millisUntilFinished / 1000);
            }

            public void onFinish() {
                mAction.onConnectionTimeEnd();
            }
        }.start();
    }

    @Override
    public void stopAttendanceTimer() {
        attendanceTimer.cancel();
        mTimerCountView.setVisibility(View.GONE);
        mAttendanceTimerProgressView.setVisibility(View.GONE);
    }

    @Override
    public void stopConnectionTimer() {
        connectionTimer.cancel();
        mConnectionDialogView.dismiss();
    }

    @Override
    public void requestDisableConnection() {
        mConnectionDetector.start();
    }

    @Override
    public String getProvidedSecret() {
        return secretView.getText().toString();
    }

    @Override
    public void endView() {
        if (getActivity() != null)
            getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAction.start();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // insure that mConnectionDetector ends, so that receivers unregistered successfully
        mConnectionDetector.end();
    }

    @Override
    public void onConnectionLost() {
        Log.i(TAG, "onConnectionLost: connection lost");
        mAction.onConnectionLost();
    }

    @Override
    public void onConnectionBack() {
        Log.i(TAG, "onConnectionBack: ");
        mAction.onConnectionBack();
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
                secretView.setText(secretWheels.getSecret());
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
