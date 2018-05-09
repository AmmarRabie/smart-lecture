package cmp.sem.team8.smarlecture.group.members;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.util.ProfileImageUtil;

/**
 * Created by Loai Ali on 3/17/2018.
 */

public class MembersRecyclerAdapter extends RecyclerView.Adapter<MembersRecyclerAdapter.GroupViewHolder> {

    private final static int VIEW_TYPE_CELL = 0;
    private final static int VIEW_TYPE_FOOTER = 1;
    private onItemClickListener mItemClickListener = null;
    private ArrayList<InvitedUserModel> mMembersList;
    private Context mContext;

    public MembersRecyclerAdapter(Context context, ArrayList<InvitedUserModel> nameList,
                                  onItemClickListener onEditClickListener) {

        this(context, nameList);
        mItemClickListener = onEditClickListener;
    }

    public MembersRecyclerAdapter(Context context, ArrayList<InvitedUserModel> namesList) {
        this.mContext = context;
        this.mMembersList = namesList;
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CELL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group_member, parent, false);
            return new GroupViewHolder(view, VIEW_TYPE_CELL);

        } else if (viewType == VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group_newname, parent, false);
            return new GroupViewHolder(view, VIEW_TYPE_FOOTER);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        if (position == mMembersList.size()) {
            holder.bindLastView(position);
            return;
        }
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMembersList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mMembersList.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    interface onItemClickListener {
        void onSaveItemClick(View v, String name, int position);

        void onCancelInvitationClick(View view, String userId, int position);

        void onRemoveMemberClick(View view, String userId, int position);
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView emailView;
        private ImageView profileImageView;
        private View actionParentView;
        private TextView actionTextView;
        private ImageView actionImageView;

        private EditText newNameView;
        private ImageView saveImageView;

        GroupViewHolder(View itemView, int type) {
            super(itemView);
            if (type == VIEW_TYPE_CELL) {
                nameTextView = itemView.findViewById(R.id.groupMemberItem_name);
                emailView = itemView.findViewById(R.id.groupMemberItem_email);
                profileImageView = itemView.findViewById(R.id.groupMemberItem_profileImage);
                actionParentView = itemView.findViewById(R.id.groupMemberItem_actionParent);
                actionTextView = itemView.findViewById(R.id.groupMemberItem_actionText);
                actionImageView = itemView.findViewById(R.id.groupMemberItem_actionImage);
                return;
            }
            newNameView = itemView.findViewById(R.id.groupNewName_name);
            saveImageView = itemView.findViewById(R.id.groupNewName_save);
        }

        void bind(final int position) {
            final InvitedUserModel currMember = mMembersList.get(position);

            nameTextView.setText(currMember.getName());
            emailView.setText(currMember.getEmail());
            ProfileImageUtil.setProfileImage(mContext, currMember.getProfileImage(), profileImageView, 70);

            if (currMember.isAccept()) {
                // put the view of accepted member
                actionImageView.setImageResource(R.drawable.ic_done);
                actionImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
                actionTextView.setText(R.string.member);
                actionTextView.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
                if (mItemClickListener == null)
                    return;
                actionParentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onRemoveMemberClick(view, currMember.getId(), position);
                    }
                });
            } else {
                // set the view of invited member use
                actionImageView.setImageResource(R.drawable.ic_cross);
                actionImageView.setColorFilter(ContextCompat.getColor(mContext, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.SRC_IN);
                actionTextView.setText(R.string.invited);
                actionTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.holo_red_dark));
                if (mItemClickListener == null)
                    return;
                actionParentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onCancelInvitationClick(view, currMember.getId(), position);
                    }
                });
            }
        }

        void bindLastView(final int position) {
            saveImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onSaveItemClick(view, newNameView.getText().toString(), position);
                }
            });
        }
    }
}
