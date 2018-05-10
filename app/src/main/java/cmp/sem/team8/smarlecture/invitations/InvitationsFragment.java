package cmp.sem.team8.smarlecture.invitations;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import es.dmoral.toasty.Toasty;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

/**
 * The impl of news feed view, show the user the opened groupInvitations sorted by date
 */
public class InvitationsFragment extends Fragment implements InvitationsContract.Views, InvitationsRecyclerAdapter.OnItemClickListener {


    private InvitationsContract.Actions mAction;
    private RecyclerView invitationsRecyclerView;
    private View emptyView;
    private InvitationsRecyclerAdapter invitationsRecyclerAdapter;
    private ArrayList<GroupInvitationModel> groupInvitations;
    private Animator spruceAnimator;

    public static InvitationsFragment newInstance() {
        return new InvitationsFragment();
    }

    @Override
    public void setPresenter(InvitationsContract.Actions presenter) {
        mAction = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_invitations, container, false);
        setHasOptionsMenu(true);

        invitationsRecyclerView = root.findViewById(R.id.invitationsFrag_list);
        emptyView = root.findViewById(R.id.invitationsFrag_emptyView);
        invitationsRecyclerView.setHasFixedSize(true);


        groupInvitations = new ArrayList<>();

        invitationsRecyclerAdapter = new InvitationsRecyclerAdapter(groupInvitations, this);

        invitationsRecyclerView.setAdapter(invitationsRecyclerAdapter);
        invitationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                if (state.didStructureChange())
                    spruceAnimator = new Spruce.SpruceBuilder(invitationsRecyclerView)
                            .sortWith(new CorneredSort(150, false, CorneredSort.Corner.TOP_LEFT))
                            .animateWith(DefaultAnimations.fadeInAnimator(invitationsRecyclerView, 800)
                                    , ObjectAnimator.ofFloat(invitationsRecyclerView, "translationX", -invitationsRecyclerView.getWidth(), 0f).setDuration(800)
                            )
                            .start();
            }
        });

        // refresh empty view after one second so that get the database time to fetch the data
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshEmptyView();
                handler.removeCallbacks(this);
            }
        }, 1000);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAction.start();
        if (spruceAnimator != null) spruceAnimator.start();
    }


    @Override
    public void addGroupInvitation(GroupInvitationModel group) {
        groupInvitations.add(group);
        invitationsRecyclerAdapter.notifyItemInserted(groupInvitations.size());
        refreshEmptyView();
    }

    @Override
    public void removeGroup(String groupId) {
        for (int i = 0; i < groupInvitations.size(); i++)
            if (groupInvitations.get(i).getGroupId().equals(groupId)) {
                groupInvitations.remove(i);
                invitationsRecyclerAdapter.notifyItemRemoved(i);
                refreshEmptyView();
                return;
            }
    }

    @Override
    public void showErrorMessage(String message) {
        Toasty.error(getContext(), message, Toast.LENGTH_LONG, true).show();
    }

    @Override
    public void onAcceptClicked(View view, int position, String groupId) {
        mAction.acceptGroup(groupId);
    }

    @Override
    public void oRefuseClicked(View view, int position, String groupId) {
        mAction.refuseGroup(groupId);
    }

    private void refreshEmptyView() {
        if (groupInvitations.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            invitationsRecyclerView.setVisibility(View.GONE);
            return;
        }
        emptyView.setVisibility(View.GONE);
        invitationsRecyclerView.setVisibility(View.VISIBLE);

    }
}
