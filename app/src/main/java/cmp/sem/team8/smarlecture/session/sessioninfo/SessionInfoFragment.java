package cmp.sem.team8.smarlecture.session.sessioninfo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;

/**
 * Created by ramym on 3/15/2018.
 */

public class SessionInfoFragment extends Fragment implements SessionInfoContract.Views {

    private SessionInfoContract.Actions mPresenter;
    private TextView id;
    private ImageView endStartSessionButton;

    private TextView status;
    private TextView startEndSession;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.start_session_fragment, container, false);
        id = root.findViewById(R.id.session_id);
        mPresenter.startSession();

        endStartSessionButton = root.findViewById(R.id.frag_sessioninfo_startendsession);
        startEndSession = root.findViewById(R.id.sessionAction);
        status = root.findViewById(R.id.session_stauts);
        endStartSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mPresenter.getSessionStatus().equals(AppDataSource.SessionStatus.OPEN.toString()))
                getActivity().onBackPressed();
                else{
                    mPresenter.openSession();

                }
            }
        });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void setPresenter(SessionInfoContract.Actions presenter) {

        mPresenter = presenter;
    }

    @Override
    public void showSessionId(String id) {
        this.id.setText(id);

    }

    @Override
    public void closedSessionView() {

        endStartSessionButton.setVisibility(View.GONE);
        startEndSession.setVisibility(View.GONE);
        startEndSession.setText("");
        status.setText("CLOSED");


    }

    @Override
    public void openSessionView() {

        endStartSessionButton.setImageResource(R.drawable.ic_error_outline_white_48dp);
        status.setText("OPEN");
        startEndSession.setText("End Session");


    }

    @Override
    public void notActiveSessionView() {

        endStartSessionButton.setImageResource(R.drawable.exit_blue);
        status.setText("NOT ACTIVE");
        startEndSession.setText("Start Session");


    }

    @Override
    public void sendSessioIdToActivity(String Id) {
        getActivity().getIntent().putExtra("SessionId", Id);
    }


    public static SessionInfoFragment newInstance() {
        return new SessionInfoFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }
}
