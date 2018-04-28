package cmp.sem.team8.smarlecture.session.beginattendance;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.AttendeeModel;
import cmp.sem.team8.smarlecture.common.view.SecretWheels;
import es.dmoral.toasty.Toasty;

/**
 * Created by ramym on 3/17/2018.
 */

public class BeginAttendanceFragment extends Fragment implements BeginAttendanceContract.Views, MembersRecyclerAdapter.OnItemClickListener {

    private BeginAttendanceContract.Actions mPresenter;

    private boolean isStarted = false;

    private TextView AttendanceTimerView;
    private TextView secretView;
    private Button startAttendanceView;
    private LinearLayout secretParentView;
    private LinearLayout attendanceTimerParentView;
    private View changeSecretView;
    private RecyclerView membersRecyclerView;

    private ArrayList<AttendeeModel> membersList;
    private MembersRecyclerAdapter membersAdapter;

    public static BeginAttendanceFragment newInstance() {
        return new BeginAttendanceFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isStarted)
            return;
        mPresenter.start();
        isStarted = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_begin_attendance, container, false);

        secretParentView = root.findViewById(R.id.membersFrag_secretParent);
        attendanceTimerParentView = root.findViewById(R.id.membersFrag_attendanceTimerParent);
        AttendanceTimerView = root.findViewById(R.id.membersFrag_attendanceTimer);
        secretView = root.findViewById(R.id.membersFrag_secret);
        changeSecretView = root.findViewById(R.id.membersFrag_changeSecret);
        membersRecyclerView = root.findViewById(R.id.membersFrag_list);
        startAttendanceView = root.findViewById(R.id.membersFrag_startAttendance);


        startAttendanceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.BeginAttendance();
                changeSecretView.setVisibility(View.GONE);
                startAttendanceView.setVisibility(View.GONE);
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


        membersList = new ArrayList<>();
        membersAdapter = new MembersRecyclerAdapter(membersList, this);
        membersRecyclerView.setAdapter(membersAdapter);
        membersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        membersRecyclerView.setHasFixedSize(true);

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
        startAttendanceView.setVisibility(View.VISIBLE);
        secretParentView.setVisibility(View.VISIBLE);
        attendanceTimerParentView.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideBeginAttendaceButton() {
        startAttendanceView.setVisibility(View.GONE);
        secretParentView.setVisibility(View.GONE);
        attendanceTimerParentView.setVisibility(View.GONE);

    }

    @Override
    public void startAttendanceTimer(int minutes) {

        new CountDownTimer(minutes * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                AttendanceTimerView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                AttendanceTimerView.setText(getString(R.string.mes_done));
                mPresenter.onAttendanceTimerEnd();
            }
        }.start();
    }

    @Override
    public void showSecret(String secret) {
        secretView.setText(secret);
    }


    @Override
    public String getSecret() {
        return secretView.getText().toString();
    }

    @Override
    public void addNewMember(AttendeeModel newAttendee) {
        membersList.add(newAttendee);
        membersAdapter.notifyItemInserted(membersList.size());
    }

    @Override
    public void updateMemberAttendance(String id, boolean attend) {
        membersAdapter.updateChecked(id, attend);
    }


    @Override
    public void onAddNoteClicked(View v, int pos, String memberId) {

    }
}
