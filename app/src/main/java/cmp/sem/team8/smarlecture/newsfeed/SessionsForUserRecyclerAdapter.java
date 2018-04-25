package cmp.sem.team8.smarlecture.newsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;

/**
 * Created by Loai Ali on 3/19/2018.
 */


class SessionsForUserRecyclerAdapter extends RecyclerView.Adapter<SessionsForUserRecyclerAdapter.SessionViewHolder> {

    private OnItemClickListener mItemClickListener = null;
    private ArrayList<SessionForUserModel> mSessionsList;
    private Context mContext;

    public SessionsForUserRecyclerAdapter(Context context, ArrayList<SessionForUserModel> sessions,
                                          OnItemClickListener onEditClickListener) {
        this(context, sessions);
        mItemClickListener = onEditClickListener;
    }


    public SessionsForUserRecyclerAdapter(Context context, ArrayList<SessionForUserModel> groupList) {
        this.mContext = context;
        this.mSessionsList = groupList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sessions_for_user, parent, false);

        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSessionsList.size();
    }

    public void swapList(ArrayList<SessionForUserModel> sessions) {
        if (mSessionsList != null) {
            mSessionsList.clear();
            mSessionsList.addAll(sessions);
            notifyDataSetChanged();
            return;
        }
        mSessionsList = new ArrayList<>(sessions);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {
        /**
         * called when the whole item is clicked
         *
         * @param view     the view clicked
         * @param position its position in the list
         */
        void onItemClick(View view, int position);

        void onSessionClicked(View view, int position, String sessionId);

        void onGroupClicked(View view, int position, String groupId);

        void onUserClicked(View view, int position, String userId);

    }

    class SessionViewHolder extends RecyclerView.ViewHolder {

        private TextView sessionNameView;
        private TextView groupNameView;
        private TextView userNameView;
        private TextView sessionStatusView;
        private TextView attendanceStatusView;

        SessionViewHolder(View itemView) {
            super(itemView);
            sessionNameView = itemView.findViewById(R.id.sessionsForUserItem_SName);
            groupNameView = itemView.findViewById(R.id.sessionsForUserItem_GName);
            userNameView = itemView.findViewById(R.id.sessionsForUserItem_UName);
            sessionStatusView = itemView.findViewById(R.id.sessionsForUserItem_SStatus);
            attendanceStatusView = itemView.findViewById(R.id.sessionsForUserItem_AStatus);
        }

        void bind(final int position) {

            SessionForUserModel currSession = mSessionsList.get(position);

            sessionNameView.setText(currSession.getSessionName());
            groupNameView.setText(currSession.getForGroupName());
            userNameView.setText(currSession.getOwnerName());
            sessionStatusView.setText(currSession.getStatus().name());
            attendanceStatusView.setText(currSession.getAttendanceStatus().name());

            if (mItemClickListener == null)
                return;

            if (!sessionNameView.hasOnClickListeners())
                sessionNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onSessionClicked(view, position, mSessionsList.get(position).getId());
                    }
                });

            if (!groupNameView.hasOnClickListeners())
                groupNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onGroupClicked(view, position, mSessionsList.get(position).getForGroupId());
                    }
                });

            if (!userNameView.hasOnClickListeners())
                userNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onUserClicked(view, position, mSessionsList.get(position).getOwnerId());
                    }
                });
        }
    }
}

