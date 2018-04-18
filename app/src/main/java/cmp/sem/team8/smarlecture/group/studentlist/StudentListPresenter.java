package cmp.sem.team8.smarlecture.group.studentlist;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;

/**
 * Created by Loai Ali on 3/16/2018.
 */

public class StudentListPresenter implements StudentListContract.Actions {
    private static final String TAG = "StudentListPresenter";

    private final String GROUP_ID;
    private final String GROUP_NAME;

    private StudentListContract.Views mView;

    private DatabaseReference mGroupRef;

    public StudentListPresenter(StudentListContract.Views view, final String groupId, final String groupName) {
        mView = view;
        GROUP_ID = groupId;
        GROUP_NAME = groupName;
        mGroupRef = null;
        if (groupId == null) {
            Log.e(TAG, "StudentListPresenter: group passed as null");
            return;
        }
        mView.setPresenter(this);
    }

    public void start() {

        mView.handleOfflineStates();
        mView.showGroupName(GROUP_NAME);

        FirebaseDatabase.getInstance().getReference(GroupEntry.KEY_THIS)
                .child(GROUP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mGroupRef = FirebaseDatabase.getInstance().
                            getReference(GroupEntry.KEY_THIS).child(GROUP_ID);
                    passStudents();
                } else {
                    Log.e(TAG, "onDataChange: the Gruop presenter is called with invalid group id");
                    mView.showOnErrorMessage("group doesn't exist");
                    mGroupRef = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mGroupRef = null;
                mView.showOnErrorMessage(databaseError.getMessage());
            }
        });
    }

    @Override
    public void addStudent(final String name) {
        if (mGroupRef == null) {
            Log.e(TAG, "addStudent: called without finding the group");
            return;
        }
        if (name == null || name.isEmpty()) {
            mView.showOnErrorMessage("Student must have a name");
            return;
        }
        DatabaseReference newStudentRef = mGroupRef.child(GroupEntry.KEY_NAMES_LIST).push();
        final String key = newStudentRef.getKey();

        final boolean isOffline = mView.getOfflineState();
        if (isOffline)
            mView.onAddSuccess(key, name);
        newStudentRef.setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!isOffline)
                        mView.onAddSuccess(key, name);
                } else {
                    mView.showOnErrorMessage(task.getException().getMessage());
                }
            }
        });

        //mGroupRef.child("namesList").push().setValue(name);

    }

    @Override
    public void editStudent(final String studentKey, final String newName) {
        if (mGroupRef == null) {
            return;
        }
        if (newName == null || newName.isEmpty()) {
            mView.showOnErrorMessage("Name can't be empty");
            return;
        }
        final boolean isOffline = mView.getOfflineState();
        if (isOffline)
            mView.onEditSuccess(studentKey, newName);
        mGroupRef.child(GroupEntry.KEY_NAMES_LIST).child(studentKey).setValue(newName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!isOffline)
                        mView.onEditSuccess(studentKey, newName);
                } else {
                    mView.showOnErrorMessage(task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void deleteStudent(final String studentKey) {
        if (mGroupRef == null) {
            return;
        }
        final boolean isOffline = mView.getOfflineState();
        if (isOffline)
            mView.onDeleteSuccess(studentKey);
        mGroupRef.child(GroupEntry.KEY_NAMES_LIST).child(studentKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (!isOffline)
                        mView.onDeleteSuccess(studentKey);
                } else {
                    mView.showOnErrorMessage(task.getException().getMessage());
                }
            }
        });
    }


    private void passStudents() {
        if (mGroupRef == null) {
            return;
        }
        mGroupRef.child(GroupEntry.KEY_NAMES_LIST).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists())
                        continue;
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


       /* ValueEventListener valueEventListener = mGroupRef.child("namesList").
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
        valueEventListeners.add(valueEventListener);*/
    }

    @Override
    public void end() {
/*        for (int i = 0; i < valueEventListeners.size(); i++)
            mGroupRef.child("namesList").removeEventListener(valueEventListeners.get(i));*/
    }

}