package cmp.sem.team8.smarlecture.notification;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.GroupMessageModel;

/**
 * Created by Loai Ali on 3/19/2018.
 */


class GroupMessagesRecyclerAdapter extends RecyclerView.Adapter<GroupMessagesRecyclerAdapter.GroupMessageViewHolder> {

    private ArrayList<GroupMessageModel> mGroupMessagesList;


    public GroupMessagesRecyclerAdapter(ArrayList<GroupMessageModel> groupsList) {
        this.mGroupMessagesList = groupsList;
    }

    @Override
    public GroupMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_group_message, parent, false);

        return new GroupMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupMessageViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mGroupMessagesList.size();
    }

    public void swapList(ArrayList<GroupMessageModel> sessions) {
        if (mGroupMessagesList != null) {
            mGroupMessagesList.clear();
            mGroupMessagesList.addAll(sessions);
            notifyDataSetChanged();
            return;
        }
        mGroupMessagesList = new ArrayList<>(sessions);
        notifyDataSetChanged();
    }

    class GroupMessageViewHolder extends RecyclerView.ViewHolder {

        private TextView titleView;
        private TextView bodyView;

        GroupMessageViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.groupMessageItem_title);
            bodyView = itemView.findViewById(R.id.groupMessageItem_body);
        }

        void bind(final int position) {
            final GroupMessageModel currGroupMessage = mGroupMessagesList.get(position);
            titleView.setText(currGroupMessage.getTitle());
            bodyView.setText(currGroupMessage.getBody());
        }
    }
}

