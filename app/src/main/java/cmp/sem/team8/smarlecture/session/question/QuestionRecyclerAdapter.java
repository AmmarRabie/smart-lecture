package cmp.sem.team8.smarlecture.session.question;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.model.QuestionModel;

/**
 * Created by mahmoud on 4/18/2018.
 */

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.QuestionViewHolder> {
    private OnItemClickListener mItemClickListener = null;
    private ArrayList<QuestionModel> mQuestionList;
    private Context mContext;

    public QuestionRecyclerAdapter(Context context, ArrayList<QuestionModel> QuestionList,
                                     OnItemClickListener onEditClickListener) {
        this(context, QuestionList);
        mItemClickListener = onEditClickListener;
    }


    public QuestionRecyclerAdapter(Context context, ArrayList<QuestionModel> QuestionList) {
        this.mContext = context;
        this.mQuestionList = QuestionList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }


    @Override
    public QuestionRecyclerAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_questionslist, parent, false);

        return new QuestionRecyclerAdapter.QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionRecyclerAdapter.QuestionViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    public void swapList(ArrayList<QuestionModel> QuestionModels) {
        if (mQuestionList != null) {
            mQuestionList.clear();
            mQuestionList.addAll(QuestionModels);
            return;
        }
        mQuestionList = new ArrayList<>(QuestionModels);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {


        void onDeleteQuestionClick(View view, int position);

        void onEditQuestionClick(View view, int position);

        /**
         * called when the whole item is clicked
         *
         * @param view     the view clicked
         * @param position its position in the list
         */


    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView descrptionView;
        private View deleteImageView;
        private View editImageView;
        private View containerView;

        QuestionViewHolder(View itemView) {
            super(itemView);
            descrptionView = itemView.findViewById(R.id.Question_description);

            deleteImageView = itemView.findViewById(R.id.Question_deleteobjective);
            editImageView = itemView.findViewById(R.id.Question_editobjective);
            containerView = itemView;
        }

        void bind(final int position) {

            QuestionModel currObjective = mQuestionList.get(position);

            descrptionView.setText(currObjective.getmQuestionDescription());

            if (mItemClickListener == null)
                return;


            deleteImageView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onDeleteQuestionClick(view, position);
                        }
                    });
            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onEditQuestionClick(view, position);
                }
            });


        }


    }
}
