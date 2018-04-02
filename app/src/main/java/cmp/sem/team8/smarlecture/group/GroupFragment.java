package cmp.sem.team8.smarlecture.group;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends android.support.v4.app.Fragment implements
        GroupContract.Views,
        GroupAdapter.onItemClickListner {

    private GroupContract.Actions mPresenter;

    private Button mAddStudent;

    private ListView mGroupList;

    private GroupAdapter mGroupAdapter;

    private View.OnClickListener mAddStudtentListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());

            View mView = getLayoutInflater().inflate(R.layout.addstudentdialog, null);

            mBuilder.setView(mView);

            final EditText mName = (EditText) mView.findViewById(R.id.studentDialogName);

            Button mAdd = (Button) mView.findViewById(R.id.addStudentDialog);

            final AlertDialog dialog = mBuilder.create();

            dialog.show();

            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.addStudent(mName.getText().toString());
                    dialog.dismiss();
                }
            });

        }
    };

    public GroupFragment(){   }

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    @Override
    public void setPresenter(GroupContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_group, container, false);

        mAddStudent = root.findViewById(R.id.groupFrag_addStudent);

        mGroupList = root.findViewById(R.id.groupFrag_list);


        mAddStudent.setOnClickListener(mAddStudtentListner);

        mGroupAdapter=new GroupAdapter(getContext(),
                new ArrayList<HashMap<String, Object>>(),this );

        mGroupList.setAdapter(mGroupAdapter);


        return root;
    }

    @Override
    public void showOnSuccess() {


    }


    @Override
    public void showNamesList(ArrayList<HashMap<String, Object>> namesList) {

        mGroupAdapter.clear();

        mGroupAdapter.addAll(namesList);

        mGroupAdapter.notifyDataSetChanged();

       }

    @Override
    public void showOnErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteSuccess(String UID) {
       int position=0;

      while ( !(UID.equals( mGroupAdapter.getItem(position).get("key").toString()))){position++;}
       HashMap<String,Object> deletedStudent=mGroupAdapter.getItem(position);
       mGroupAdapter.remove(deletedStudent);
       mGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditSuccess(String UID,String newName) {
       int position=0;
        while(!(UID.equals( mGroupAdapter.getItem(position).get("key").toString()))){position++;}
        mGroupAdapter.getItem(position).put("name",newName);
        mGroupAdapter.notifyDataSetChanged();

    }

    @Override
    public void onAddSuccess(String UID,String newName) {
        HashMap<String,Object> newStudent=new HashMap<>();
        newStudent.put("key",UID);
        newStudent.put("name",newName);
        mGroupAdapter.add(newStudent);
        mGroupAdapter.notifyDataSetChanged();

    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @Override
    public void onPause() {
        super.onPause();
        mPresenter.end();
    }

    @Override
    public void onEditItemClick(View v, int position) {
        HashMap<String,Object> studentClicked=mGroupAdapter.getItem(position);
        final String studentID=studentClicked.get("key").toString();


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.addgroupdialog, null);
        mBuilder.setView(mView);
        final EditText studentNameView = (EditText) mView.findViewById(R.id.groupDialogName);
        final Button addGroupView = (Button) mView.findViewById(R.id.addGroupDialog);
        final AlertDialog dialog = mBuilder.create();
        addGroupView.setText("Change");
        addGroupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentName = studentNameView.getText().toString();
                mPresenter.editStudent(studentID, studentName);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onDeleteItemClick(View v, int position) {
        String key = mGroupAdapter.getItem(position).get("key").toString();
        mPresenter.deleteStudent(key);


    }
}
