package cmp.sem.team8.smarlecture.joinsession.questions;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;

/**
 * Created by mahmoud on 4/18/2018.
 */

public class QuestionsRecyclerAdapter extends RecyclerView.Adapter<QuestionsRecyclerAdapter.QuestionViewHolder> {
    private ArrayList<QuestionModel> mQuestionsList;

    public QuestionsRecyclerAdapter(ArrayList<QuestionModel> Questions) {
        this.mQuestionsList = Questions;
    }


    @Override
    public QuestionsRecyclerAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);

        return new QuestionsRecyclerAdapter.QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionsRecyclerAdapter.QuestionViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mQuestionsList.size();
    }

    public void swapList(ArrayList<QuestionModel> QuestionModels) {
        if (mQuestionsList != null) {
            mQuestionsList.clear();
            mQuestionsList.addAll(QuestionModels);
            return;
        }
        mQuestionsList = new ArrayList<>(QuestionModels);
        notifyDataSetChanged();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView ownerNameView;

        QuestionViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.questionItem_text);
            ownerNameView = itemView.findViewById(R.id.questionItem_ownerName);
        }

        void bind(final int position) {
            QuestionModel currQuestion = mQuestionsList.get(position);

            textView.setText(currQuestion.getText());
            ownerNameView.setText(currQuestion.getOwner().getName());
        }
    }
}
