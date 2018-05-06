package cmp.sem.team8.smarlecture.group.members;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.InternetConnectivityReceiver;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class MembersFragment extends android.support.v4.app.Fragment implements
        MembersContract.Views,
        MembersRecyclerAdapter.onItemClickListener, InternetConnectivityReceiver.OnInternetConnectionChangeListener {

    private MembersContract.Actions mPresenter;

    private RecyclerView mStudentListRecyclerView;
    private View mOfflineView;

    private MembersRecyclerAdapter mMembersRecyclerAdapter;

    private ArrayList<InvitedUserModel> mNamesList;
    private InternetConnectivityReceiver internetConnectivityReceiver;
    private boolean mInternetState;

    public MembersFragment() {
    }

    public static MembersFragment newInstance() {
        return new MembersFragment();
    }

    @Override
    public void setPresenter(MembersContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_group_members, container, false);
        setHasOptionsMenu(true);

        mStudentListRecyclerView = root.findViewById(R.id.groupFrag_list);
        mOfflineView = root.findViewById(R.id.offlineView);
        mNamesList = new ArrayList<>();

        mMembersRecyclerAdapter = new MembersRecyclerAdapter(getContext(),
                mNamesList, this);

        mStudentListRecyclerView.setAdapter(mMembersRecyclerAdapter);
        mStudentListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void showNamesList(ArrayList<InvitedUserModel> namesList) {
        if (namesList.equals(mNamesList))
            return;
        mNamesList.clear();
        mNamesList.addAll(namesList);
        mMembersRecyclerAdapter.notifyDataSetChanged(/*0,mNamesList.size()*/);
    }

    @Override
    public void showGroupName(String groupName) {
        getActivity().setTitle(groupName);
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
    public void showOnErrorMessage(String cause) {
        Toasty.error(getContext(), cause, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onRemoveSuccess(String UID) {
        int position = 0;
        while (!(UID.equals(mNamesList.get(position).getId()))) {
            position++;
        }
        mNamesList.remove(position);
        mMembersRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAddSuccess(UserModel user) {
        InvitedUserModel newStudent = new InvitedUserModel(user, false);
        mNamesList.add(newStudent);
        mMembersRecyclerAdapter.notifyItemInserted(mNamesList.size());
    }

    @Override
    public void onExportSuccess() {
        Toasty.success(getContext(), "Exported successfully", Toast.LENGTH_SHORT, true).show();
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
    public void onDestroy() {
        super.onDestroy();
        if (internetConnectivityReceiver != null)
            internetConnectivityReceiver.end(getContext());
    }

    @Override
    public void onCancelInvitationClick(View view, final String userId, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Cancel Invitation")
                .setMessage("Are you sure you want to cancel the invitation. You can invite him again")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.cancelInvitation(userId);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    @Override
    public void onRemoveMemberClick(View view, final String userId, int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Remove Member")
                .setMessage("This user will not be included in upcoming sessions in this group.\nYou have to invite him again if you proceed")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.removeMember(userId);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }

    @Override
    public void onSaveItemClick(View v, String name, int position) {
        mPresenter.addStudent(name);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionGroup_exportToExcel:
                mPresenter.exportExcel(null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
