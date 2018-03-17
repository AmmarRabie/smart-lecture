package cmp.sem.team8.smarlecture.session.startsession;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionContract;

/**
 * Created by ramym on 3/15/2018.
 */

public class StartSessionFragment  extends Fragment implements StartSessionContract.Views{

    private StartSessionContract.Actions mPresenter;
    private TextView id;
    private Button StartSession;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.start_session_fragment, container, false);

        StartSession = root.findViewById(R.id.start_session);
        StartSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startSession();
            }
        });

        id=root.findViewById(R.id.session_id);

        return root;
    }

    @Override
    public void setPresenter(StartSessionContract.Actions presenter) {

        mPresenter = presenter;
    }

    @Override
    public void showSessionId(String id) {
       this.id.setText(id);
    }
    public static StartSessionFragment newInstance() {
        return new StartSessionFragment();
    }

}
