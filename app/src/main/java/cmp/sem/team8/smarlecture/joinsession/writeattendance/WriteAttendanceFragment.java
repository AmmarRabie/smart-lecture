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
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;
import cmp.sem.team8.smarlecture.session.startsession.StartSessionContract;

public class WriteAttendanceFragment  extends Fragment implements WriteAttendanceContract.Views {

    private ListView listView;
    private DatabaseReference reference;
    private List<UserAttendanceModel> students;

    private int  PreSelectedIndex=-1;
    private WriteAttendanceContract.Actions mPresenter;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_write_attendance, container, false);

        listView=(ListView)root.findViewById(R.id.write_attendance_studentlist);
        reference= FirebaseDatabase.getInstance().getReference();

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        students=new ArrayList<>();

        final String SessionId=getActivity().getIntent().getStringExtra("sessionid");
        final String GroupnId=getActivity().getIntent().getStringExtra("groupid");


        reference=reference.child("sessions").child(SessionId).child("attendance");


        mPresenter=new WriteAttendancePresenter(this,GroupnId,SessionId);

        mPresenter.getStudentsList();

/*
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue().toString().equals("open"))
                {
                    DatabaseReference nref=FirebaseDatabase.getInstance().getReference();
                    nref=nref.child("groups").child(GroupnId).child("namesList");
                    nref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot child :dataSnapshot.getChildren()) {
                                String name = child.getValue().toString();
                                students.add(new UserAttendanceModel(name,false));
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(getActivity(),"Attendance is closed ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/


        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             UserAttendanceModel model=students.get(position);
             model.setChecked(true);
             students.set(position,model);
             if (PreSelectedIndex>-1)
             {
                 UserAttendanceModel pModel=students.get(PreSelectedIndex);
                 pModel.setChecked(false);
                 students.set(PreSelectedIndex,pModel);
             }
             PreSelectedIndex=position;
             adapter.updateRecords(students);


            }
        });*/

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
