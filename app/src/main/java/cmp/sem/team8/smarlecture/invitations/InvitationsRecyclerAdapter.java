package cmp.sem.team8.smarlecture.invitations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;

/**
 * Created by Loai Ali on 3/19/2018.
 */


class InvitationsRecyclerAdapter extends RecyclerView.Adapter<InvitationsRecyclerAdapter.InvitationViewHolder> {

    private OnItemClickListener mItemClickListener = null;
    private ArrayList<GroupInvitationModel> mGroupsList;

    public InvitationsRecyclerAdapter(ArrayList<GroupInvitationModel> groups,
                                      OnItemClickListener onEditClickListener) {
        this(groups);
        mItemClickListener = onEditClickListener;
    }


    public InvitationsRecyclerAdapter(ArrayList<GroupInvitationModel> groupsList) {
        this.mGroupsList = groupsList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public InvitationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invitation, parent, false);

        return new InvitationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InvitationViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mGroupsList.size();
    }

    public void swapList(ArrayList<GroupInvitationModel> sessions) {
        if (mGroupsList != null) {
            mGroupsList.clear();
            mGroupsList.addAll(sessions);
            notifyDataSetChanged();
            return;
        }
        mGroupsList = new ArrayList<>(sessions);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {
        void onAcceptClicked(View view, int position, String groupId);

        void oRefuseClicked(View view, int position, String groupId);
    }

    class InvitationViewHolder extends RecyclerView.ViewHolder {

        private TextView ownerNameView;
        private TextView groupNameView;
        private View acceptView;
        private View refuseView;

        InvitationViewHolder(View itemView) {
            super(itemView);
            ownerNameView = itemView.findViewById(R.id.invitationItem_ownerName);
            groupNameView = itemView.findViewById(R.id.invitationItem_groupName);
            acceptView = itemView.findViewById(R.id.invitationItem_accept);
            refuseView = itemView.findViewById(R.id.invitationItem_refuse);
        }

        void bind(final int position) {

            final GroupInvitationModel currGroup = mGroupsList.get(position);

            ownerNameView.setText(currGroup.getOwnerName());
            groupNameView.setText(currGroup.getGroupName());


            acceptView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onAcceptClicked(view, position, currGroup.getGroupId());
                }
            });

            refuseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.oRefuseClicked(view, position, currGroup.getGroupId());
                }
            });
        }
    }
}

