package cmp.sem.team8.smarlecture.group;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class groupFragment extends android.support.v4.app.Fragment implements groupContract.Views {

    private groupContract.Actions mPresenter;

    private Button mAddStudent;

    private ListView mGroupList;
    private groupAdapter mGroupAdapter;
    private String groupId;

    public static groupFragment newInstance() {
        return new groupFragment();
    }

    public void setPresenter(groupContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_group, container, false);

        mAddStudent = root.findViewById(R.id.groupFrag_addStudent);

//        groupId = savedInstanceState.getString("groupID");

        groupId = getActivity().getIntent().getStringExtra("group_key");


        mGroupList = root.findViewById(R.id.groupFrag_list);

       /*
       click listner for add student done with alert dialog
       * */
        mAddStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = inflater.inflate(R.layout.addstudentdialog, null);
                mBuilder.setView(mView);
                final EditText mName = (EditText) mView.findViewById(R.id.studentDialogName);
                Button mAdd = (Button) mView.findViewById(R.id.addStudentDialog);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.addStudent(mName.getText().toString(), groupId);
                        dialog.dismiss();
                    }
                });
            }
        });


        mGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (view.getId() == R.id.group_deletename) {
                    //delete student at position
                    mPresenter.deleteStudent(mGroupAdapter.getItem(position).toString(), groupId);
                }
                if (view.getId() == R.id.group_editname) {
                    final String oldName = mGroupAdapter.getItem(position).get
                            (String.valueOf(position)).toString();
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                    View mView = inflater.inflate(R.layout.addstudentdialog, null);
                    final EditText mName = (EditText) mView.findViewById(R.id.studentDialogName);
                    Button mAdd = (Button) mView.findViewById(R.id.addStudentDialog);
                    mAdd.setText("Save");
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    mAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPresenter.editStudent(oldName, mName.toString(), groupId);
                            dialog.dismiss();
                        }
                    });
                    mBuilder.setView(mView);
                }
            }


        });
        mPresenter.getStudents(groupId);

        return root;
    }

    @Override
    public void showOnSuccess() {


    }


    @Override
    public void showNamesList(ArrayList<HashMap<String, Object>> namesList) {
        mGroupAdapter = new groupAdapter(getActivity(), namesList, new groupAdapter.onItemClickListenerInterface() {

            @Override
            public void onEditItemClick(View v, int position) {

            }

            @Override
            public void onDeleteItemClick(View v, int position) {
                String name = mGroupAdapter.getItem(position).get("name").toString();
                String key = mGroupAdapter.getItem(position).get("key").toString();
                mPresenter.deleteStudent(key, groupId);
            }

        });
        mGroupList.setAdapter(mGroupAdapter);
        mGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage(String cause) {
        Toast.makeText(getContext(),cause,Toast.LENGTH_SHORT);

    }

}
