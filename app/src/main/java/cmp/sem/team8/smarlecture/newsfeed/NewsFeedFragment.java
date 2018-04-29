package cmp.sem.team8.smarlecture.newsfeed;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.joinsession.JoinedSession;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

/**
 * The impl of news feed view, show the user the opened sessions sorted by date
 */
public class NewsFeedFragment extends Fragment implements NewsFeedContract.Views, SessionsForUserRecyclerAdapter.OnItemClickListener {


    private NewsFeedContract.Actions mAction;
    private RecyclerView sessionsRecyclerView;
    private SessionsForUserRecyclerAdapter sessionsForUserRecyclerAdapter;
    private ArrayList<SessionForUserModel> sessions;
    private Animator spruceAnimator;

    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    @Override
    public void setPresenter(NewsFeedContract.Actions presenter) {
        mAction = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_news_feed, container, false);
        setHasOptionsMenu(true);

        sessionsRecyclerView = root.findViewById(R.id.sessionsFrag_list);
        sessionsRecyclerView.setHasFixedSize(true);


        sessions = new ArrayList<>();

        sessionsForUserRecyclerAdapter = new SessionsForUserRecyclerAdapter(getContext(),
                sessions, this);

        sessionsRecyclerView.setAdapter(sessionsForUserRecyclerAdapter);
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                if (state.didStructureChange())
                    spruceAnimator = new Spruce.SpruceBuilder(sessionsRecyclerView)
                            .sortWith(new CorneredSort(150, false, CorneredSort.Corner.TOP_LEFT))
                            .animateWith(DefaultAnimations.fadeInAnimator(sessionsRecyclerView, 800)
                                    , ObjectAnimator.ofFloat(sessionsRecyclerView, "translationX", -sessionsRecyclerView.getWidth(), 0f).setDuration(800)
                            )
                            .start();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAction.start();
        if (spruceAnimator != null) spruceAnimator.start();
    }

    @Override
    public void showSessions(ArrayList<SessionForUserModel> sessions) {
        sessionsForUserRecyclerAdapter.swapList(sessions);
    }

    @Override
    public void addSession(SessionForUserModel session) {
        sessions.add(session);
        sessionsForUserRecyclerAdapter.notifyItemInserted(sessions.size());
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override
    public void onSessionClicked(View view, int position, String sessionId) {
        Intent sessionActivityIntent = new Intent(getContext(), JoinedSession.class);
        sessionActivityIntent.putExtra(getString(R.string.IKey_sessionId), sessionId);
        startActivity(sessionActivityIntent);
    }

    @Override
    public void onGroupClicked(View view, int position, String groupId) {

    }

    @Override
    public void onUserClicked(View view, int position, String userId) {

    }
}