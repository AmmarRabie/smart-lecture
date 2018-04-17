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
import android.view.MenuItem;
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
import cmp.sem.team8.smarlecture.common.InternetConnectivityReceiver;
import cmp.sem.team8.smarlecture.group.GroupActivity;
import cmp.sem.team8.smarlecture.joinsession.JoinedSession;
import cmp.sem.team8.smarlecture.session.SessionActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends Fragment implements
        GroupListContract.Views,
        GroupListRecyclerAdapter.OnItemClickListener, InternetConnectivityReceiver.OnInternetConnectionChangeListener {


    Animator spruceAnimator;
    private GroupListContract.Actions mPresenter;
    private FloatingActionButton mAddGroup;
    private View mOfflineView;
    private ViewGroup mGroupsEmptyView;
    private View mAddGroupEmptyView;
    private View mJoinSessionEmptyView;
    private RecyclerView mGroupRecyclerView;
    private GroupListRecyclerAdapter mGroupListAdapter;
    private ArrayList<HashMap<String, Object>> mGroupList;
    private View.OnClickListener mAddGroupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
            final EditText groupNameView = buildEditTextDialogView("Name", null);
            mBuilder.setView(groupNameView);
            mBuilder.setTitle(getString(R.string.dTitle_addGroup));
            mBuilder.setPositiveButton(getString(R.string.dAction_add), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.addGroup(groupNameView.getText().toString());
                    dialogInterface.dismiss();
                }
            });
            mBuilder.setNegativeButton(getString(R.string.dAction_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            mBuilder.show();
        }
    };
    private boolean mInternetState;
    private InternetConnectivityReceiver internetConnectivityReceiver;
    private boolean isInEmptyView = false;

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
        setHasOptionsMenu(true);

        mAddGroup = root.findViewById(R.id.groupListFrag_addGroup);
        mAddGroupEmptyView = root.findViewById(R.id.groupListFrag_addGroupEmptyView);
        mJoinSessionEmptyView = root.findViewById(R.id.groupListFrag_joinSessionEmptyView);
        mOfflineView = root.findViewById(R.id.offlineView);
        mGroupRecyclerView = root.findViewById(R.id.groupListFrag_list);
        mGroupsEmptyView = root.findViewById(R.id.groupListFrag_emptyView);
        mGroupRecyclerView.setHasFixedSize(true);       // optimize the performance


        mAddGroup.setOnClickListener(mAddGroupClickListener);
        mAddGroupEmptyView.setOnClickListener(mAddGroupClickListener);
        mJoinSessionEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinSession();
            }
        });

        mGroupList = new ArrayList<>();

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

        refreshEmptyState();
        return root;
    }

    private void refreshEmptyState() {
        if (mGroupList.isEmpty() && !isInEmptyView) {
            showEmptyView();
            isInEmptyView = true;
        } else if (!mGroupList.isEmpty() && isInEmptyView) {
            disableEmptyView();
            isInEmptyView = false;
        }
    }

    private void showEmptyView() {
        mGroupsEmptyView.setVisibility(View.VISIBLE);
        mAddGroup.setVisibility(View.GONE);
        mGroupRecyclerView.setVisibility(View.GONE);
    }

    private void disableEmptyView() {
        mGroupsEmptyView.setVisibility(View.GONE);
        mAddGroup.setVisibility(View.VISIBLE);
        mGroupRecyclerView.setVisibility(View.VISIBLE);
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
        if (groupList.equals(mGroupList))
            return;
        mGroupList.clear();
        mGroupList.addAll(groupList);
        mGroupListAdapter.notifyDataSetChanged();
        refreshEmptyState();
    }

    @Override
    public void onDeleteSuccess(final String groupID) {
        int position = 0;
        while (!groupID.equals(mGroupList.get(position).get("id").toString())) {
            position++;
        }
        mGroupList.remove(position);
        mGroupListAdapter.notifyDataSetChanged(); // call this instead to get onBind called on all views so that onClick listeners get updated with correct position
        refreshEmptyState();
    }

    @Override
    public void onEditSuccess(String groupID, String newName) {
        int position = 0;
        while (!groupID.equals(mGroupList.get(position).get("id").toString())) {
            position++;
        }
        mGroupList.get(position).put("name", newName);
        mGroupListAdapter.notifyItemChanged(position, null);
    }

    @Override
    public void onAddSuccess(String groupID, String newName) {
        HashMap<String, Object> newGroup = new HashMap<>();
        newGroup.put("name", newName);
        newGroup.put("id", groupID);
        mGroupList.add(newGroup);
        mGroupListAdapter.notifyItemInserted(mGroupList.size());
        refreshEmptyState();
    }

    @Override
    public void startJoinSessionView(String sessionId, String groupId) {
        Intent joinSessionActivity = new Intent(getContext(), JoinedSession.class);
        joinSessionActivity.putExtra(getString(R.string.IKey_groupId),groupId);
        joinSessionActivity.putExtra(getString(R.string.IKey_sessionId),sessionId);
        startActivity(joinSessionActivity);
    }

    @Override
    public void handleOfflineStates() {

        internetConnectivityReceiver =
                new InternetConnectivityReceiver(this).start(getContext());
    }

    @Override
    public boolean getOfflineState() {
        return !mInternetState;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionGroupList_joinSession:
                joinSession();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void joinSession() {
        View view = (LayoutInflater.from(getContext())).inflate(R.layout.dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view);
        alert.setTitle(getString(R.string.dTitle_joinSession));
        final EditText input = (EditText) view.findViewById(R.id.dialog_text);
        alert.setPositiveButton(getString(R.string.dAction_join), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                mPresenter.joinSession(input.getText().toString());
                dialog.dismiss();
            }
        });
        alert.show();
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
    public void onPause() {
        super.onPause();
        internetConnectivityReceiver.end(getContext());
    }

  /*  @Override
    public void onStartSessionClick(View view, int position) {
        HashMap<String, Object> groupSessionClicked = mGroupList.get(position);
        String groupId = groupSessionClicked.get("id").toString();
        Intent sessionActivity = new Intent(getContext(), SessionActivity.class);
        sessionActivity.putExtra("group_id", groupId);
        startActivity(sessionActivity);
    }*/

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
        String groupName = groupClicked.get("name").toString();
        Intent groupActivity = new Intent(getContext(), GroupActivity.class);
        groupActivity.putExtra("group_id", groupId);
        groupActivity.putExtra("group_name", groupName);
        startActivity(groupActivity);
    }

    @Override
    public void onDeleteGroupClick(View view, int position) {
        HashMap<String, Object> groupClicked = mGroupList.get(position);
        final String groupId = groupClicked.get("id").toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dTitle_deleteGroup));
        builder.setMessage(getString(R.string.dMes_deleteGroup));
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton(getString(R.string.dAction_delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPresenter.deleteGroup(groupId);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.dAction_cancel), new DialogInterface.OnClickListener() {
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
        mBuilder.setPositiveButton(getString(R.string.dAction_change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String groupName = newNameView.getText().toString();
                mPresenter.editGroup(groupId, groupName);
                dialogInterface.dismiss();
            }
        });
        mBuilder.setNegativeButton(getString(R.string.dAction_cancel), new DialogInterface.OnClickListener() {
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

    @Override
    public void onInternetConnectionLost() {
        mInternetState = false;
        mOfflineView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onInternetConnectionBack() {
        mInternetState = true;
        mOfflineView.setVisibility(View.GONE);
    }
}
