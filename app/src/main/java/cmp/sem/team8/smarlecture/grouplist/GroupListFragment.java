package cmp.sem.team8.smarlecture.grouplist;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.group.groupContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends android.support.v4.app.Fragment implements GroupListContract.Views {



    private GroupListContract.Actions mPresenter;
    private FloatingActionButton mAddGroup;
    private ListView mGroupList;
    private String mUserID;
    private GroupListAdapter mGroupListAdapter;

    public GroupListFragment newInstance() {
        return new GroupListFragment();
    }
    public GroupListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View root=inflater.inflate(R.layout.frag_grouplist,container,false);
       mAddGroup=root.findViewById(R.id.addGroupListButton);
       mUserID=getActivity().getIntent().getStringExtra("UID");
       mGroupList=root.findViewById(R.id.grouplistFrag_list);
       mAddGroup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
               View mView = inflater.inflate(R.layout.addgroupdialog, null);
               mBuilder.setView(mView);
               final EditText mGroupName=(EditText)mView.findViewById(R.id.groupDialogName);
               final Button mAddGroup=(Button)mView.findViewById(R.id.addGroupDialog);
               final AlertDialog dialog = mBuilder.create();
               dialog.show();
               mAddGroup.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mPresenter.addGroup(mGroupName.getText().toString(),mUserID);
                       dialog.dismiss();
                   }
               });

           }
       });
       mGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

           }
       });
        mPresenter.getGroups(mUserID);
        return root;
    }


    @Override
    public void setPresenter(GroupListContract.Actions presenter) {
        mPresenter = presenter;

    }

    @Override
    public void showErrorMessage(String cause) {
        Toast.makeText(getContext(),cause,Toast.LENGTH_SHORT);

    }

    @Override
    public void showGroupList(ArrayList<HashMap<String, Object>> groupList) {
        mGroupListAdapter=new GroupListAdapter(getActivity(),groupList,new GroupListAdapter.onItemClickListenerInterface(){

            @Override
            public void onEditItemClick(View v, final int position) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.addgroupdialog, null);
                mBuilder.setView(mView);
                final EditText mGroupName = (EditText) mView.findViewById(R.id.groupDialogName);
                Button mAdd = (Button) mView.findViewById(R.id.addGroupDialog);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String key=mGroupListAdapter.getItem(position).get("key").toString();

                        mPresenter.editGroup(key,mGroupName.getText().toString(),mUserID);
                        dialog.dismiss();
                    }
                });


            }

            @Override
            public void onDeleteItemClick(View v, int position) {

                String key = mGroupListAdapter.getItem(position).get("key").toString();
                mPresenter.deleteGroup(key, mUserID);
            }
        });
        mGroupList.setAdapter(mGroupListAdapter);
        mGroupListAdapter.notifyDataSetChanged();

    }
}
