package cmp.sem.team8.smarlecture.session.attendance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cmp.sem.team8.fireapp.R;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class AttendanceFragment extends Fragment implements AttendanceContract.Views {


    private AttendanceContract.Actions mActions;

    private AtendeeAdapter mAtendeeAdapter;

    private EditText mSecret;
    private Button mGetAttendances;

    public static AttendanceFragment newInstance() {
        return new AttendanceFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.attendance_frag, container, false);

        mSecret = root.findViewById(R.id.attendanceFrag_secret);
        mGetAttendances = root.findViewById(R.id.attendanceFrag_takeAttendance);
        mGetAttendances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActions.findAttendanceList(mSecret.getText().toString());
            }
        });

        ListView attendeeList = root.findViewById(R.id.attendanceFrag_list);

        mAtendeeAdapter = null;
        attendeeList.setAdapter(mAtendeeAdapter);

        attendeeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mActions.setAttendance(i,true);
            }
        });

        return root;
    }

    @Override
    public void setPresenter(AttendanceContract.Actions presenter) {
        mActions = presenter;
    }

    @Override
    public void showOnCompleteFetchDataMessage() {
        Toast.makeText(getContext(), "nice, take your attendance", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(String cause) {

    }

    @Override
    public void showProgressIndicator() {

    }


    @Override
    public void hideProgressIndicator() {

    }

    @Override
    public void showAttendanceList(List<Map> attendanceList) {
        if (mAtendeeAdapter == null) {
            mAtendeeAdapter = new AtendeeAdapter(getActivity(), attendanceList);
            return;
        }

        mAtendeeAdapter.clear();
        mAtendeeAdapter.addAll(attendanceList);
        mAtendeeAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean getConnectionState(boolean includeInternet, boolean includeSim) {
        return false;
    }


}
