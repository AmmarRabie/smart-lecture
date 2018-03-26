package cmp.sem.team8.smarlecture.group;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Loai Ali on 3/16/2018.
 */

public class groupPresenter implements groupContract.Actions {
    private groupContract.Views mView;

    public void start() {


    }

    @Override
    public void addStudent(String name, String groupID) {
        if (name == null || name.isEmpty()) {
            mView.showErrorMessage("Student must have a name");
            return;

        }
        if(groupID==null||groupID.isEmpty()){
            mView.showErrorMessage("group ID error");
            return;
        }

        DatabaseReference thisGroup =
                FirebaseDatabase.getInstance().getReference("groups").child(groupID);

        thisGroup.child("namesList").push().setValue(name);

    }

    @Override
    public void editStudent(String oldName, String newName, String groupID) {
        if(newName==null||newName.isEmpty()){
            mView.showErrorMessage("Name can't be empty");
            return;
        }
        if(groupID==null||groupID.isEmpty()){
            mView.showErrorMessage("group ID error");
            return;
        }
        deleteStudent(oldName, groupID);
        addStudent(newName, groupID);
        return;
    }

    @Override
    public void deleteStudent(String name, String groupID) {
        FirebaseDatabase.getInstance().getReference("groups").child(groupID)
                .child("namesList").child(name).removeValue();
    }

    @Override
    public void getStudents(String groupId) {
        DatabaseReference thisGroupReference = FirebaseDatabase.getInstance().
                getReference("groups").child(groupId);
        ValueEventListener valueEventListener = thisGroupReference.child("namesList").
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    String name = child.getValue(String.class);
                    HashMap<String, Object> thisStudent = new HashMap<>();
                    thisStudent.put("key", key);
                    thisStudent.put("name", name);
                    list.add(thisStudent);
                }
                mView.showNamesList(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public groupPresenter(groupContract.Views view) {
        mView = view;
        mView.setPresenter(this);
    }
}
