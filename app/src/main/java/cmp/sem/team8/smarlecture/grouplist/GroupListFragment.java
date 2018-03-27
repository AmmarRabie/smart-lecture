package cmp.sem.team8.smarlecture.grouplist;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import cmp.sem.team8.smarlecture.group.GroupActivity;
import cmp.sem.team8.smarlecture.session.SessionActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends Fragment implements
        GroupListContract.Views,
        GroupListAdapter.OnItemClickListener {


    private GroupListContract.Actions mPresenter;

    private FloatingActionButton mAddGroup;
    private ListView mGroupList;

    private GroupListAdapter mGroupListAdapter;

    private View.OnClickListener mAddGroupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
            View mView = getLayoutInflater().inflate(R.layout.addgroupdialog, null);
            mBuilder.setView(mView);
            final EditText mGroupName = (EditText) mView.findViewById(R.id.groupDialogName);
            final Button mAddGroup = (Button) mView.findViewById(R.id.addGroupDialog);
            final AlertDialog dialog = mBuilder.create();
            mAddGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.addGroup(mGroupName.getText().toString());
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };

    public GroupListFragment() {
        // Required empty public constructor
    }

    public static GroupListFragment newInstance() {
        return new GroupListFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_grouplist, container, false);

        mAddGroup = root.findViewById(R.id.groupListFrag_addGroup);
        mGroupList = root.findViewById(R.id.groupListFrag_list);

        mAddGroup.setOnClickListener(mAddGroupClickListener);
        mGroupListAdapter = new GroupListAdapter(getContext(),
                new ArrayList<HashMap<String, Object>>(), this);
        mGroupList.setAdapter(mGroupListAdapter);
        return root;
    }


    @Override
    public void setPresenter(GroupListContract.Actions presenter) {
        mPresenter = presenter;

    }

    @Override
    public void showErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showGroupList(ArrayList<HashMap<String, Object>> groupList) {
        mGroupListAdapter.clear();
        mGroupListAdapter.addAll(groupList);
        mGroupListAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link android.app.Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }


    @Override
    public void onStartSessionClick(View view, int position) {
        HashMap<String, Object> groupSessionClicked = mGroupListAdapter.getItem(position);
        String groupId = groupSessionClicked.get("id").toString();
        Intent sessionActivity = new Intent(getContext(), SessionActivity.class);
        sessionActivity.putExtra("group_id", groupId);
        startActivity(sessionActivity);
    }

    /**
     * called when the whole item is clicked
     *
     * @param view     the view clicked
     * @param position its position in the list
     */
    @Override
    public void onItemClick(View view, int position) {
        HashMap<String, Object> groupClicked = mGroupListAdapter.getItem(position);
        String groupId = groupClicked.get("id").toString();
        Intent groupActivity = new Intent(getContext(), GroupActivity.class);
        groupActivity.putExtra("group_id", groupId);
        startActivity(groupActivity);
    }

    @Override
    public void onDeleteGroupClick(View view, int position) {
        HashMap<String, Object> groupClicked = mGroupListAdapter.getItem(position);
        String groupId = groupClicked.get("id").toString();
        mPresenter.deleteGroup(groupId);
        mGroupListAdapter.remove(groupClicked);
        mGroupListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditGroupClick(View view, final int position) {
        HashMap<String, Object> groupClicked = mGroupListAdapter.getItem(position);
        final String groupId = groupClicked.get("id").toString();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        View mView = getLayoutInflater().inflate(R.layout.addgroupdialog, null);
        mBuilder.setView(mView);
        final EditText groupNameView = (EditText) mView.findViewById(R.id.groupDialogName);
        final Button addGroupView = (Button) mView.findViewById(R.id.addGroupDialog);
        final AlertDialog dialog = mBuilder.create();
        addGroupView.setText("Change");
        addGroupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupNameView.getText().toString();
                mPresenter.editGroup(groupId, groupName);
                mGroupListAdapter.getItem(position).put("name",groupName);
                mGroupListAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
