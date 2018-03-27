package cmp.sem.team8.smarlecture.group;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Loai Ali on 3/16/2018.
 */

public class GroupPresenter implements GroupContract.Actions {
    private static final String TAG = "GroupPresenter";
    private final String GROUP_ID;
    ArrayList<ValueEventListener> valueEventListeners = new ArrayList<>();
    private GroupContract.Views mView;
    private DatabaseReference mGroupRef;

    public GroupPresenter(GroupContract.Views view, final String groupId) {
        mView = view;
        GROUP_ID = groupId;
        mGroupRef = null;
        if (groupId == null) {
            Log.e(TAG, "GroupPresenter: group passed as null");
            return;
        }
        mView.setPresenter(this);
    }

    public void start() {
        FirebaseDatabase.getInstance().getReference("groups")
                .child(GROUP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mGroupRef = FirebaseDatabase.getInstance().
                            getReference("groups").child(GROUP_ID);
                    passStudents();
                } else {
                    Log.e(TAG, "onDataChange: the Gruop presenter is called with invalid group id");
                    mView.showErrorMessage("group doesn't exist");
                    mGroupRef = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mGroupRef = null;
                mView.showErrorMessage(databaseError.getMessage());
            }
        });
    }

    @Override
    public void addStudent(String name) {
        if (mGroupRef == null) {
            Log.e(TAG, "addStudent: called without finding the group");
            return;
        }
        if (name == null || name.isEmpty()) {
            mView.showErrorMessage("Student must have a name");
            return;
        }
        mGroupRef.child("namesList").push().setValue(name);

    }

    @Override
    public void editStudent(String studentKey, String newName) {
        if (mGroupRef == null) {
            return;
        }
        if (newName == null || newName.isEmpty()) {
            mView.showErrorMessage("Name can't be empty");
            return;
        }
        mGroupRef.child("namesList").child(studentKey).setValue(newName);
    }

    @Override
    public void deleteStudent(String name) {
        if (mGroupRef == null) {
            return;
        }
        mGroupRef.child("namesList").child(name).removeValue();
    }


    private void passStudents() {
        if (mGroupRef == null) {
            return;
        }

        ValueEventListener valueEventListener = mGroupRef.child("namesList").
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
        valueEventListeners.add(valueEventListener);
    }

    @Override
    public void end() {
        for (int i = 0; i < valueEventListeners.size(); i++)
            mGroupRef.child("namesList").removeEventListener(valueEventListeners.get(i));
    }

}
