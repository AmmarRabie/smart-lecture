package cmp.sem.team8.smarlecture.joinsession.sessioninfo;

/**
 * Created by Loai Ali on 4/23/2018.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.rateobjectives.RateObjectivesActivity;

public class SessionInfoFragment extends Fragment implements cmp.sem.team8.smarlecture.joinsession.sessioninfo.SessionInfoContract.Views {
    private SessionInfoContract.Actions mPresenter;
    private TextView mSessionID;
    private  TextView mSessionOwner;
    private TextView mGroupName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.session_info_student,container,false);
        mSessionID=root.findViewById(R.id.session_id);
        mSessionOwner=root.findViewById(R.id.session_owner);
        mGroupName=root.findViewById(R.id.group_name);
        return root;
    }

    public void setPresenter(SessionInfoContract.Actions presenter) {
        mPresenter=presenter;

    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }


    @Override
    public void showSessionInfo(String sesisonID,String sessionOwner,String groupName,String sessionName) {

        getActivity().setTitle(sessionName);
        mSessionID.setText(sesisonID);
        mGroupName.setText(groupName);
        mSessionOwner.setText(sessionOwner);


    }

    @Override
    public void showErrorMessage(String Cause) {
        Toast.makeText(getContext(),Cause,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeSession(String sessionID) {
        Intent i=new Intent(getContext(), RateObjectivesActivity.class);
        i.putExtra("session_id",sessionID);
        startActivity(i);
    }

    public  static SessionInfoFragment newInstance(){return new SessionInfoFragment();}
}
