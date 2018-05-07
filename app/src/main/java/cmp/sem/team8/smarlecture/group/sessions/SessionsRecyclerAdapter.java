package cmp.sem.team8.smarlecture.group.sessions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;


/**
 * Created by Loai Ali on 4/15/2018.
 */

public class SessionsRecyclerAdapter extends RecyclerView.Adapter<SessionsRecyclerAdapter.SessionViewHolder> {

    private OnItemClickListener mItemClickListener = null;

    private ArrayList<SessionModel> mSessionList;

    private Context mContext;

    public SessionsRecyclerAdapter(Context context, ArrayList<SessionModel> sessionsList,
                                   OnItemClickListener onItemClickListener) {
        this(context, sessionsList);
        mItemClickListener = onItemClickListener;
    }

    public SessionsRecyclerAdapter(Context context, ArrayList<SessionModel> sessionsList) {
        mContext = context;
        mSessionList = sessionsList;
    }

    public void setOnItemClickListener(SessionsRecyclerAdapter.OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public SessionsRecyclerAdapter.SessionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);

        return new SessionsRecyclerAdapter.SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SessionsRecyclerAdapter.SessionViewHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mSessionList.size();
    }

    public void swapList(ArrayList<SessionModel> sessionlist) {
        if (mSessionList != null) {
            mSessionList.clear();
            mSessionList.addAll(sessionlist);
            return;
        }
        mSessionList = new ArrayList<>(sessionlist);
        notifyDataSetChanged();
    }

    interface OnItemClickListener {


        void onDeleteSessionClick(View view, int position);

        void onEditSessionClick(View view, int position);

        void onItemClick(View view, int position);
    }

    class SessionViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;

        private TextView statusTextView;

        private ImageView deleteImageView;

        private ImageView editImageView;


        private View containerView;

        public SessionViewHolder(View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.sessionList_sessionName);

            deleteImageView = itemView.findViewById(R.id.sessionlist_deletesession);

            editImageView = itemView.findViewById(R.id.sessionList_editsession);


            statusTextView = itemView.findViewById(R.id.sessionList_status);

            containerView = itemView;
        }

        void bind(final int position) {

            SessionModel currSession = mSessionList.get(position);

            nameTextView.setText(currSession.getName());
            statusTextView.setText(currSession.getSessionStatus().toString());

            switch (currSession.getSessionStatus()) {
                case OPEN:
                    containerView.setBackgroundColor(mContext.getResources().getColor(R.color.trafficLight_green));
                    break;
                case NOT_ACTIVATED:
                    containerView.setBackgroundColor(mContext.getResources().getColor(R.color.trafficLight_yellow));
                    break;
                case CLOSED:
                    containerView.setBackgroundColor(mContext.getResources().getColor(R.color.trafficLight_red));
                    break;
            }
            if (mItemClickListener == null)
                return;

            // set listeners to the whole view, the startSession view and the deleteGroupView

            deleteImageView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mItemClickListener.onDeleteSessionClick(view, position);
                        }
                    });
            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onEditSessionClick(view, position);
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
