package cmp.sem.team8.smarlecture.session.sessioninfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by ramym on 3/15/2018.
 */

public class SessionInfoFragment extends Fragment implements SessionInfoContract.Views{

    private SessionInfoContract.Actions mPresenter;
    private TextView id;
    private View endSessionButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.start_session_fragment, container, false);

        id=root.findViewById(R.id.session_id);

        mPresenter.startSession();

        //int Se=Session.getId();

        //Intent intent=((SessionActivity)getActivity()).getIntent();//.putExtra("SessionId",Session.getId());

        endSessionButton=root.findViewById(R.id.start_sessio_fragment_end_session);
        endSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.endSession();
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        //startActivity(intent);
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
    public void sendSessioIdToActivity(int Id) {
       getActivity().getIntent().putExtra("SessionId",Id);
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
