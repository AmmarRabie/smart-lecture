package cmp.sem.team8.smarlecture.grouplist;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;

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
        GroupListRecyclerAdapter.OnItemClickListener {


    Animator spruceAnimator;
    private GroupListContract.Actions mPresenter;
    private FloatingActionButton mAddGroup;
    private RecyclerView mGroupRecyclerView;
    private GroupListRecyclerAdapter mGroupListAdapter;
    private ArrayList<HashMap<String, Object>> mGroupList;
    private View.OnClickListener mAddGroupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
            final EditText groupNameView = buildEditTextDialogView("Name", null);
            mBuilder.setView(groupNameView);
            mBuilder.setTitle("Add Group");
            mBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.addGroup(groupNameView.getText().toString());
                    dialogInterface.dismiss();
                }
            });
            mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            mBuilder.show();
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

        mGroupRecyclerView = root.findViewById(R.id.groupListFrag_list);
        mGroupRecyclerView.setHasFixedSize(true);

        mAddGroup.setOnClickListener(mAddGroupClickListener);

        mGroupList = new ArrayList<HashMap<String, Object>>();

        mGroupListAdapter = new GroupListRecyclerAdapter(getContext(),
                mGroupList, this);

        mGroupRecyclerView.setAdapter(mGroupListAdapter);
        mGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                if (state.didStructureChange())
                    spruceAnimator = new Spruce.SpruceBuilder(mGroupRecyclerView)
                            .sortWith(new CorneredSort(150, false, CorneredSort.Corner.TOP_LEFT))
                            .animateWith(DefaultAnimations.fadeInAnimator(mGroupRecyclerView, 800)
                                    , ObjectAnimator.ofFloat(mGroupRecyclerView, "translationX", -mGroupRecyclerView.getWidth(), 0f).setDuration(800)
                            )
                            .start();
            }
        });

        return root;
    }

    @Override
    public void setPresenter(GroupListContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showErrorMessage(String cause) {
        Toast.makeText(getContext(), "Error: " + cause, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showGroupList(ArrayList<HashMap<String, Object>> groupList) {
        if (groupList.equals(mGroupList))
            return;
        mGroupList.clear();
        mGroupList.addAll(groupList);
        mGroupListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteSuccess(final String groupID) {
        int position = 0;
        while (!groupID.equals(mGroupList.get(position).get("id").toString())) {
            position++;
        }
        mGroupList.remove(position);
        mGroupListAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onEditSuccess(String groupID, String newName) {
        int position = 0;
        while (!groupID.equals(mGroupList.get(position).get("id").toString())) {
            position++;
        }
        mGroupList.get(position).put("name", newName);
        mGroupListAdapter.notifyItemChanged(position, null);
        Toast.makeText(getContext(), "Error: " + newName, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onAddSuccess(String groupID, String newName) {
        HashMap<String, Object> newGroup = new HashMap<>();
        newGroup.put("name", newName);
        newGroup.put("id", groupID);
        mGroupList.add(newGroup);
        mGroupListAdapter.notifyItemInserted(mGroupList.size());
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
        HashMap<String, Object> groupSessionClicked = mGroupList.get(position);
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
        HashMap<String, Object> groupClicked = mGroupList.get(position);
        String groupId = groupClicked.get("id").toString();
        Intent groupActivity = new Intent(getContext(), GroupActivity.class);
        groupActivity.putExtra("group_id", groupId);
        startActivity(groupActivity);
    }

    @Override
    public void onDeleteGroupClick(View view, int position) {
        HashMap<String, Object> groupClicked = mGroupList.get(position);
        final String groupId = groupClicked.get("id").toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Group");
        builder.setMessage("This group and all its sessions will be permanently deleted");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deleteGroup(groupId);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onEditGroupClick(View view, final int position) {
        HashMap<String, Object> groupClicked = mGroupList.get(position);
        final String groupId = groupClicked.get("id").toString();
        final String groupName = groupClicked.get("name").toString();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        final EditText newNameView = buildEditTextDialogView(null, groupName);
        mBuilder.setView(newNameView);
        mBuilder.setTitle("Edit group name");
        mBuilder.setIcon(android.R.drawable.ic_menu_edit);
        mBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = newNameView.getText().toString();
                mPresenter.editGroup(groupId, groupName);
                dialogInterface.dismiss();
            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        mBuilder.show();
    }


    private EditText buildEditTextDialogView(String hint, String text) {
        EditText input = new EditText(getContext());
        input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        input.setHint(hint);
        input.setHintTextColor(getContext().getResources().getColor(android.R.color.secondary_text_dark));
        input.setText(text);
        input.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
        return input;
    }

}
