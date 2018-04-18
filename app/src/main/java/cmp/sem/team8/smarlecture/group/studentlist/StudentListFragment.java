package cmp.sem.team8.smarlecture.group.studentlist;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.InternetConnectivityReceiver;
import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentListFragment extends android.support.v4.app.Fragment implements
        StudentListContract.Views,
        StudentListRecyclerAdapter.onItemClickListener, InternetConnectivityReceiver.OnInternetConnectionChangeListener {

    private StudentListContract.Actions mPresenter;

    private RecyclerView mStudentListRecyclerView;
    private View mOfflineView;

    private StudentListRecyclerAdapter mStudentListRecyclerAdapter;

    private ArrayList<HashMap<String, Object>> mNamesList;
    private InternetConnectivityReceiver internetConnectivityReceiver;
    private boolean mInternetState;

    public StudentListFragment() {
    }

    public static StudentListFragment newInstance() {
        return new StudentListFragment();
    }

    @Override
    public void setPresenter(StudentListContract.Actions presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_studentlist, container, false);

        mStudentListRecyclerView = root.findViewById(R.id.groupFrag_list);
        mOfflineView = root.findViewById(R.id.offlineView);
        mNamesList = new ArrayList<>();

        mStudentListRecyclerAdapter = new StudentListRecyclerAdapter(getContext(),
                mNamesList, this);

        mStudentListRecyclerView.setAdapter(mStudentListRecyclerAdapter);
        mStudentListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    @Override
    public void showNamesList(ArrayList<HashMap<String, Object>> namesList) {
        if (namesList.equals(mNamesList))
            return;
        mNamesList.clear();
        mNamesList.addAll(namesList);
        mStudentListRecyclerAdapter.notifyDataSetChanged(/*0,mNamesList.size()*/);
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
    public void onDeleteSuccess(String UID) {
        int position = 0;
        while (!(UID.equals(mNamesList.get(position).get("key").toString()))) {
            position++;
        }
        mNamesList.remove(position);
        mStudentListRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEditSuccess(String UID, String newName) {
        int position = 0;
        while (!(UID.equals(mNamesList.get(position).get("key").toString()))) {
            position++;
        }
        mNamesList.get(position).put("name", newName);
        mStudentListRecyclerAdapter.notifyItemChanged(position, null);
    }

    @Override
    public void onAddSuccess(String UID, String newName) {
        HashMap<String, Object> newStudent = new HashMap<>();
        newStudent.put("key", UID);
        newStudent.put("name", newName);
        mNamesList.add(newStudent);
        mStudentListRecyclerAdapter.notifyItemInserted(mNamesList.size());
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
        if (internetConnectivityReceiver != null)
            internetConnectivityReceiver.end(getContext());
    }

    @Override
    public void onEditItemClick(View v, int position) {
        HashMap<String, Object> studentClicked = mNamesList.get(position);
        final String studentID = studentClicked.get("key").toString();
        final String studentName = studentClicked.get("name").toString();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        final EditText newNameView = buildEditTextDialogView(studentName);
        mBuilder.setView(newNameView);
        mBuilder.setTitle(getString(R.string.dTitle_editName));
        mBuilder.setPositiveButton(getString(R.string.dAction_change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String studentName = newNameView.getText().toString();
                mPresenter.editStudent(studentID, studentName);
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

    @Override
    public void onDeleteItemClick(View v, int position) {
        String key = mNamesList.get(position).get("key").toString();
        mPresenter.deleteStudent(key);
    }

    @Override
    public void onSaveItemClick(View v, String name, int position) {
        mPresenter.addStudent(name);
    }

    private EditText buildEditTextDialogView(String name) {
        EditText input = new EditText(getContext());
        input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        input.setText(name);
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
