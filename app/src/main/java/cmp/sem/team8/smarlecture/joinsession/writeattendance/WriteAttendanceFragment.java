package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionContract;

public class WriteAttendanceFragment  extends Fragment implements WriteAttendanceContract.Views {

    private ListView listView;
    private DatabaseReference reference;
    private List<UserAttendanceModel> students;

    private int  PreSelectedIndex=-1;
    private WriteAttendanceContract.Actions mPresenter;

    private TextView secrect;
    private Button takeAttendanceButton;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.frag_attendance, container, false);

        listView=(ListView)root.findViewById(R.id.attendanceFrag_list);
        secrect=(TextView)root.findViewById(R.id.attendanceFrag_secret);
        takeAttendanceButton=(Button)root.findViewById(R.id.attendanceFrag_takeAttendance);

        mPresenter=new WriteAttendancePresenter(this);

        takeAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String SessionId=getActivity().getIntent().getStringExtra("sessionid");
                final String GroupnId=getActivity().getIntent().getStringExtra("groupid");

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
                ref = ref.child("sessions").child(SessionId);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        String Secrect="";
                        String AttendanceFlag="";
                        for (DataSnapshot children : dataSnapshot.getChildren())
                        {
                            if (children.getKey().toString().equals("attendance"))
                                AttendanceFlag = children.getValue().toString();
                            else if (children.getKey().toString().equals("attendancesecrect"))
                                Secrect = children.getValue().toString();
                        }
                        if (AttendanceFlag.equals("closed"))
                        {
                            Toast.makeText(getActivity(),"Attendance is closed ", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!Secrect.equals(secrect.getText().toString()))
                        {
                            Toast.makeText(getActivity(),"Secrect is Incorrect ", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        reference= FirebaseDatabase.getInstance().getReference();

                        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        students=new ArrayList<>();

                        reference=reference.child("sessions").child(SessionId).child("attendance");


                        mPresenter.getStudentsList(GroupnId,SessionId);


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });


        return root;
    }


    public static WriteAttendanceFragment newInstance() {
        return new WriteAttendanceFragment();
    }

    @Override
    public void setPresenter(WriteAttendanceContract.Actions presenter) {

    }

    @Override
    public void showStudents(String id) {

    }

    @Override
    public void ListViewSetAdapter(StudentAttendanceAdapter adapter)
    {
        listView.setAdapter(mPresenter.getAdapter());
    }

    @Override
    public void ListViewSetOnItemClickListener(AdapterView.OnItemClickListener listener) {

        listView.setOnItemClickListener(listener);
    }
}
