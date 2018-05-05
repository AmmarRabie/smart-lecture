package cmp.sem.team8.smarlecture.session.join.rateobjectives;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.InternetConnectivityReceiver;
import cmp.sem.team8.smarlecture.home.groups.GroupsActivity;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public class RateObjectivesFragment extends android.support.v4.app.Fragment
        implements RateObjectivesContract.Views,
        RateObjectivesRecyclerAdapter.OnItemClickListener, InternetConnectivityReceiver.OnInternetConnectionChangeListener {

    Animator spruceAnimator;

    private RateObjectivesContract.Actions mPresenter;

    private Button mSumbitRating;

    private Button mCancelRating;

    private View mOfflineView;

    private RecyclerView mObjectiveRecyclerView;

    private RateObjectivesRecyclerAdapter mObjectiveAdapter;

    private ArrayList<ObjectiveModel> mObjectiveList;

    // private ArrayList<Float> mUserRatings;

    private ArrayList<Float> mObjectivesRating;//changed on every change in rating bar used in submitting ratings

    private View.OnClickListener mSubmitRatingListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.RateObjectives(mObjectivesRating);

            //  mPresenter.RateObjectives(mObjectivesRating);
        }
    };

    private View.OnClickListener mCancelRatingListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getContext(), GroupsActivity.class);
            startActivity(i);
            //remove the activity from the backstack so after starting grouplist activity
            // if clicking on back button it won't get us back to this activity
            getActivity().finish();
        }
    };

    private boolean mInternetState;

    private InternetConnectivityReceiver internetConnectivityReceiver;

    private boolean isInEmptyView = false;

    public RateObjectivesFragment() {
    }

    public static RateObjectivesFragment newInstace() {
        return new RateObjectivesFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_rate_objectives, container, false);

        mSumbitRating = root.findViewById(R.id.submitrating);

        mCancelRating = root.findViewById(R.id.cancelrating);

        mOfflineView = root.findViewById(R.id.offlineView);

        mObjectiveRecyclerView = root.findViewById(R.id.objectivesList);

        mSumbitRating.setOnClickListener(mSubmitRatingListner);

        mCancelRating.setOnClickListener(mCancelRatingListner);

        mObjectivesRating = new ArrayList<>();

        mObjectiveList = new ArrayList<>();

        // mUserRatings=new ArrayList<>();


        mObjectiveAdapter = new RateObjectivesRecyclerAdapter(getContext(), mObjectiveList, this, mObjectivesRating);

        mObjectiveRecyclerView.setAdapter(mObjectiveAdapter);

        mObjectiveRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

                super.onLayoutChildren(recycler, state);

                if (state.didStructureChange())

                    spruceAnimator = new Spruce.SpruceBuilder(mObjectiveRecyclerView)

                            .sortWith(new CorneredSort(150, false, CorneredSort.Corner.TOP_LEFT))

                            .animateWith(DefaultAnimations.fadeInAnimator(mObjectiveRecyclerView, 800)

                                    , ObjectAnimator.ofFloat(mObjectiveRecyclerView, "translationX", -mObjectiveRecyclerView.getWidth(), 0f).setDuration(800)
                            )

                            .start();
            }
        });

        return root;


    }

    @Override
    public void setPresenter(RateObjectivesContract.Actions presenter) {
        mPresenter = presenter;

    }

    @Override
    public void showOnErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showObjectivesList(ArrayList<ObjectiveModel> objectivesList) {
        if (mObjectiveList.equals(objectivesList))
            return;

        mObjectiveList.clear();

        mObjectiveList.addAll(objectivesList);

        mObjectiveAdapter.notifyDataSetChanged();

        for (int i = 0; i < mObjectiveList.size(); i++) {
            mObjectivesRating.add(0f);
        }


    }

    @Override
    public void updateSuccess() {
//        Intent i = new Intent(getContext(), GroupsActivity.class);
//        startActivity(i);

        //remove the activity from the backstack so after starting grouplist activity
        // if clicking on back button it won't get us back to this activity
        if (getActivity() != null)
            getActivity().finish();


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
    public void onRateItemClick(View view, int position, float rating) {
        mObjectivesRating.set(position, rating);

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
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        internetConnectivityReceiver.end(getContext());
    }

}
