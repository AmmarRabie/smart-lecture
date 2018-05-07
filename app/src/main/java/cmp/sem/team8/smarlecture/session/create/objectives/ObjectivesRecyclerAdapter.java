package cmp.sem.team8.smarlecture.session.create.objectives;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;

/**
 * Created by Loai Ali on 4/18/2018.
 */

public class ObjectivesRecyclerAdapter extends RecyclerView.Adapter<ObjectivesRecyclerAdapter.ObjectiveViewHolder> {
    private OnItemClickListener mItemClickListener = null;
    private ArrayList<ObjectiveModel> mObjectivesList;
    private Context mContext;


    public ObjectivesRecyclerAdapter(Context context, ArrayList<ObjectiveModel> objectivesList,
                                     OnItemClickListener onEditClickListener) {
        this(context, objectivesList);
        mItemClickListener = onEditClickListener;
    }


    public ObjectivesRecyclerAdapter(Context context, ArrayList<ObjectiveModel> objectivesList) {
        this.mContext = context;
        this.mObjectivesList = objectivesList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }


    @Override
    public ObjectivesRecyclerAdapter.ObjectiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_objectiveslist, parent, false);

        return new ObjectivesRecyclerAdapter.ObjectiveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectivesRecyclerAdapter.ObjectiveViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mObjectivesList.size();
    }

    public void swapList(ArrayList<ObjectiveModel> objectiveModels) {
        if (mObjectivesList != null) {
            mObjectivesList.clear();
            mObjectivesList.addAll(objectiveModels);
            return;
        }
        mObjectivesList = new ArrayList<>(objectiveModels);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {


        void onDeleteObjectiveClick(View view, int position);

        void onEditObjectiveClick(View view, int position);

        /**
         * called when the whole item is clicked
         *
         * @param view     the view clicked
         * @param position its position in the list
         */


    }

    class ObjectiveViewHolder extends RecyclerView.ViewHolder {
        private TextView descrptionView;
        private View deleteImageView;
        private View editImageView;
        private View containerView;

        ObjectiveViewHolder(View itemView) {
            super(itemView);
            descrptionView = itemView.findViewById(R.id.objectives_description);

            deleteImageView = itemView.findViewById(R.id.objectives_deleteobjective);
            editImageView = itemView.findViewById(R.id.objectives_editobjective);
            containerView = itemView;
        }

        void bind(final int position) {

            ObjectiveModel currObjective = mObjectivesList.get(position);

            descrptionView.setText(currObjective.getmObjectiveDescription());

            if (mItemClickListener == null)
                return;


            deleteImageView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onDeleteObjectiveClick(view, position);
                        }
                    });
            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onEditObjectiveClick(view, position);
                }
            });


        }


    }
}
