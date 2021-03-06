package cmp.sem.team8.smarlecture.group.sessions;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.InternetConnectivityReceiver;

import cmp.sem.team8.smarlecture.common.data.model.*;
import cmp.sem.team8.smarlecture.session.create.CreateSessionActivity;

/**
 * Created by Loai Ali on 4/15/2018.
 */

public class SessionsFragment extends android.support.v4.app.Fragment implements
        SessionsContract.Views,
        InternetConnectivityReceiver.OnInternetConnectionChangeListener,
        SessionsRecyclerAdapter.OnItemClickListener {


    Animator spruceAnimator;

    private SessionsContract.Actions mPresenter;

    private FloatingActionButton mAddSession;

    private RecyclerView mSessionListRecyclerView;

    private View mOfflineView;

    private SessionsRecyclerAdapter mSessionsRecyclerAdapter;

    private ArrayList<SessionModel> mSessionslist;


    private View.OnClickListener mAddGroupClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

            final EditText sessionNameView = buildEditTextDialogView("Name", null);

            mBuilder.setView(sessionNameView);

            mBuilder.setTitle(getString(R.string.dTitle_addSession));

            mBuilder.setPositiveButton(getString(R.string.dAction_add), new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialogInterface, int i) {

                    mPresenter.addSession(sessionNameView.getText().toString());

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

    public SessionsFragment() {
    }

    public static SessionsFragment newInstance() {
        return new SessionsFragment();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_sessions, container, false);
        setHasOptionsMenu(true);

        mAddSession = root.findViewById(R.id.sessionListFrag_addSession);
        mOfflineView = root.findViewById(R.id.offlineView);
        mSessionListRecyclerView = root.findViewById(R.id.sessionListFrag_list);

        mSessionListRecyclerView.setHasFixedSize(true);       // optimize the performance


        mAddSession.setOnClickListener(mAddGroupClickListener);


        mSessionslist = new ArrayList<>();

        mSessionsRecyclerAdapter = new SessionsRecyclerAdapter(getContext(),
                mSessionslist, this);

        mSessionListRecyclerView.setAdapter(mSessionsRecyclerAdapter);
        mSessionListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                if (state.didStructureChange())
                    spruceAnimator = new Spruce.SpruceBuilder(mSessionListRecyclerView)
                            .sortWith(new CorneredSort(150, false, CorneredSort.Corner.TOP_LEFT))
                            .animateWith(DefaultAnimations.fadeInAnimator(mSessionListRecyclerView, 800)
                                    , ObjectAnimator.ofFloat(mSessionListRecyclerView, "translationX", -mSessionListRecyclerView.getWidth(), 0f).setDuration(800)
                            )
                            .start();
            }
        });


        return root;
    }


    public void setPresenter(SessionsContract.Actions presenter) {
        mPresenter = presenter;

    }

    @Override
    public void showOnErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showSessionsList(ArrayList<SessionModel> sessionslist) {

        mSessionslist.clear();
        mSessionslist.addAll(sessionslist);
        mSessionsRecyclerAdapter.notifyDataSetChanged();
        mSessionListRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void OnDeleteSuccess(String deletedSessionId) {
        int position = 0;


        while (!deletedSessionId.equals(mSessionslist.get(position).getId())) {
            position++;
        }
        mSessionslist.remove(position);
        mSessionsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnAddSuccess(SessionModel addedSessionName) {

        mSessionslist.add(addedSessionName);
        mSessionsRecyclerAdapter.notifyItemInserted(mSessionslist.size());
        mSessionListRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void OnEditSuccess(String sessionID, String sessionName) {

        int position = 0;

        while (!sessionID.equals(mSessionslist.get(position).getId())) {
            position++;
        }

        mSessionslist.get(position).setName(sessionName);

        mSessionsRecyclerAdapter.notifyItemChanged(position, null);


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
    public void onInternetConnectionLost() {
        mInternetState = false;
        mOfflineView.setVisibility(View.VISIBLE);

    }

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

    @Override
    public void onInternetConnectionBack() {
        mInternetState = true;
        mOfflineView.setVisibility(View.GONE);

    }


    @Override
    public void onDeleteSessionClick(View view, int position) {
        String sessionID = mSessionslist.get(position).getId();
        mPresenter.deleteSession(sessionID);


    }

    @Override
    public void onEditSessionClick(View view, int position) {
        final String sessionID = mSessionslist.get(position).getId();
        String sessionName = mSessionslist.get(position).getName();


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
        final EditText newNameView = buildEditTextDialogView(null, sessionName);

        mBuilder.setView(newNameView);
        mBuilder.setTitle(getString(R.string.dTitle_editSession));
        mBuilder.setPositiveButton(getString(R.string.dAction_change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String sessionName = newNameView.getText().toString();
                mPresenter.editSession(sessionName, sessionID);
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
    public void onItemClick(View view, int position) {
        SessionModel session = mSessionslist.get(position);
        String groupID = session.getForGroupId();
        String sessionID = session.getId();
        Intent sessionActivity = new Intent(getContext(), CreateSessionActivity.class);
        sessionActivity.putExtra(getString(R.string.IKey_groupId), groupID);
        sessionActivity.putExtra(getString(R.string.IKey_sessionId), sessionID);
        sessionActivity.putExtra(getString(R.string.IKey_sessionName), session.getName());
        startActivity(sessionActivity);

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
