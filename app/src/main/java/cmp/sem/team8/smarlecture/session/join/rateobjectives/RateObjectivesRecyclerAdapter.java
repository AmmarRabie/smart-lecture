package cmp.sem.team8.smarlecture.session.join.rateobjectives;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;

import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by Loai Ali on 4/20/2018.
 */

public class RateObjectivesRecyclerAdapter extends RecyclerView.Adapter<RateObjectivesRecyclerAdapter.RateObjectiveHolder> {
    private OnItemClickListener mItemClickListener = null;
    private ArrayList<ObjectiveModel> mObjectiveList;
    private ArrayList<Float> mUserRatings;
    private Context mContext;

    public RateObjectivesRecyclerAdapter(Context mContext, ArrayList<ObjectiveModel> mObjectiveList, OnItemClickListener mItemClickListener, ArrayList<Float> userRating) {
        this(mObjectiveList, mContext);
        this.mItemClickListener = mItemClickListener;
        this.mObjectiveList = mObjectiveList;
        this.mContext = mContext;
        mUserRatings = userRating;
    }

    public RateObjectivesRecyclerAdapter(ArrayList<ObjectiveModel> mObjectiveList, Context mContext) {
        this.mObjectiveList = mObjectiveList;
        this.mContext = mContext;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public RateObjectiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rate_objective, parent, false);

        return new RateObjectivesRecyclerAdapter.RateObjectiveHolder(view);
    }

    @Override
    public void onBindViewHolder(RateObjectivesRecyclerAdapter.RateObjectiveHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mObjectiveList.size();
    }

    public void swapList(ArrayList<ObjectiveModel> objectiveList) {
        if (mObjectiveList != null) {
            mObjectiveList.clear();
            mObjectiveList.addAll(objectiveList);
            return;
        }
        mObjectiveList = new ArrayList<>(objectiveList);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {


        void onRateItemClick(View view, int position, float Rating);


    }

    class RateObjectiveHolder extends RecyclerView.ViewHolder

    {
//        private RatingBar mRateView;
        private ScaleRatingBar ratingBar = new ScaleRatingBar(mContext);
        private TextView mObjectiveDescription;

        RateObjectiveHolder(View itemView) {
            super(itemView);
//            mRateView = itemView.findViewById(R.id.objectiverate);
            mObjectiveDescription = itemView.findViewById(R.id.objectivedescription);
            ratingBar = itemView.findViewById(R.id.objectiverate);

            ratingBar.setNumStars(3);
            ratingBar.setMinimumStars(0);
            ratingBar.setRating(0);
            ratingBar.setStarPadding(10);
            ratingBar.setStepSize(0.5f);
            ratingBar.setStarWidth(60);
            ratingBar.setStarHeight(60);
            ratingBar.setIsIndicator(false);
            ratingBar.setClickable(true);
            ratingBar.setScrollable(true);
            ratingBar.setClearRatingEnabled(true);

        }

        void bind(final int position) {
            ObjectiveModel mCurrentObjective = mObjectiveList.get(position);
            mObjectiveDescription.setText(mCurrentObjective.getmObjectiveDescription());

            if (mItemClickListener == null)
                return;
//            mRateView.setNumStars(3);
            ratingBar.setRating(mUserRatings.get(position));
            ratingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
                @Override
                public void onRatingChange(BaseRatingBar baseRatingBar, float rating) {
                    mItemClickListener.onRateItemClick(ratingBar, position, rating);
                    mUserRatings.set(position, rating);
                }
            });

        }

    }


}
