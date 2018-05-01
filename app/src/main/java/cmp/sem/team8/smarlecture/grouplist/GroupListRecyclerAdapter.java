package cmp.sem.team8.smarlecture.grouplist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;

/**
 * Created by Loai Ali on 3/19/2018.
 */


class GroupListRecyclerAdapter extends RecyclerView.Adapter<GroupListRecyclerAdapter.GroupListViewHolder> {

    private OnItemClickListener mItemClickListener = null;
    private ArrayList<GroupModel> mGroupList;
    private Context mContext;

    public GroupListRecyclerAdapter(Context context, ArrayList<GroupModel> groupList,
                                    OnItemClickListener onEditClickListener) {
        this(context, groupList);
        mItemClickListener = onEditClickListener;
    }


    public GroupListRecyclerAdapter(Context context, ArrayList<GroupModel> groupList) {
        this.mContext = context;
        this.mGroupList = groupList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public GroupListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grouplist, parent, false);

        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public void swapList(ArrayList<GroupModel> groupList) {
        if (mGroupList != null) {
            mGroupList.clear();
            mGroupList.addAll(groupList);
            return;
        }
        mGroupList = new ArrayList<>(groupList);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {

       // void onStartSessionClick(View view, int position);

        void onDeleteGroupClick(View view, int position);

        void onEditGroupClick(View view, int position);

        /**
         * called when the whole item is clicked
         *
         * @param view     the view clicked
         * @param position its position in the list
         */
        void onItemClick(View view, int position);

    }

    class GroupListViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private View startSessionImageView;
        private View deleteImageView;
        private View editImageView;
        private View containerView;

        GroupListViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.groupList_groupName);
          //  startSessionImageView = itemView.findViewById(R.id.grouplist_startSession);
            deleteImageView = itemView.findViewById(R.id.grouplist_deleteGroup);
            editImageView = itemView.findViewById(R.id.grouplist_editGroup);
            containerView = itemView;
        }

        void bind(final int position) {

            GroupModel   currGroup = mGroupList.get(position);

            nameView.setText(currGroup.getName());

            if (mItemClickListener == null)
                return;

            // set listeners to the whole view, the startSession view and the deleteGroupView
           /* startSessionImageView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onStartSessionClick(view, position);
                        }
                    });*/
            deleteImageView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onDeleteGroupClick(view, position);
                        }
                    });
            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onEditGroupClick(view, position);
                }
            });
            containerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(view, position);
                }
            });

        }
    }
}

