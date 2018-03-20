package cmp.sem.team8.smarlecture.joinsession.writeattendance;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cmp.sem.team8.smarlecture.common.util.ActivityUtils;
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class WriteAttendancePresenter implements WriteAttendanceContract.Actions {
    private WriteAttendanceContract.Views mView;
    private String GroupId;
    private String SessionId;
    List<UserAttendanceModel>students;
    private  StudentAttendanceAdapter adapter;
    private int PreSelectedIndex=-1;

    public WriteAttendancePresenter(WriteAttendanceContract.Views mView,String GroupId,String SessionId) {
        this.mView = mView;
        this.GroupId=GroupId;
        this.SessionId=SessionId;
    }


    @Override
    public void start() {

    }

    @Override
    public void getStudentsList() {

        students=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        reference=reference.child("groups").child(GroupId).child("namesList");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                for (DataSnapshot child :dataSnapshot.getChildren())
                {
                    String name = child.getValue().toString();
                    students.add(new UserAttendanceModel(name,false));
                }
                adapter =new StudentAttendanceAdapter(((WriteAttendanceFragment)mView).getActivity(),students);
                mView.ListViewSetAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mView.ListViewSetOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                DatabaseReference Sessionreference=FirebaseDatabase.getInstance().getReference();

                Sessionreference=Sessionreference.child("sessions").child(SessionId).child("namesList").child(Integer.toString(position+1));

                Sessionreference.setValue(true);

                Toast.makeText(((WriteAttendanceFragment)mView).getActivity(),model.getName()+" has been written as attendant ", Toast.LENGTH_LONG).show();

                ActivityUtils.removeFragmentToActivityByTag(((WriteAttendanceFragment)mView).getActivity().getSupportFragmentManager(),"writeattendance");



            }
        });

    }

    @Override
    public void WriteAttendance() {

    }

    @Override
    public StudentAttendanceAdapter getAdapter() {
        return adapter;
    }
}
