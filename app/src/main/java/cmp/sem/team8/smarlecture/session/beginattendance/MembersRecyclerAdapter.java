package cmp.sem.team8.smarlecture.session.beginattendance;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.AttendeeModel;

/**
 * Created by AmmarRabie on 27/04/2018.
 */

public class MembersRecyclerAdapter extends RecyclerView.Adapter<MembersRecyclerAdapter.MemberViewHolder> {
    private OnItemClickListener mItemClickListener = null;
    private ArrayList<AttendeeModel> mMembers;

    public MembersRecyclerAdapter(ArrayList<AttendeeModel> members,
                                  OnItemClickListener onEditClickListener) {
        this(members);
        mItemClickListener = onEditClickListener;
    }

    public MembersRecyclerAdapter(ArrayList<AttendeeModel> members) {
        this.mMembers = members;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);

        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    void updateChecked(String id, boolean attend) {
        for (int i = 0; i < mMembers.size(); i++)
            if (mMembers.get(i).getId().equals(id)) {
                mMembers.get(i).setAttend(attend);
                notifyDataSetChanged(/*i*/);
                return;
            }
    }

    interface OnItemClickListener {
        void onAddNoteClicked(View v, int pos, String memberId);

        void onAttendanceClicked(View view, int pos, boolean attend, String memberId);
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private TextView emailView;
        private View addNoteView;
        private CheckBox attendView;

        MemberViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.memberItem_name);
            emailView = itemView.findViewById(R.id.memberItem_email);
            addNoteView = itemView.findViewById(R.id.memberItem_addNote);
            attendView = itemView.findViewById(R.id.memberItem_attendFlag);
        }

        void bind(final int position) {
            final AttendeeModel currMember = mMembers.get(position);

            nameView.setText(currMember.getName());
            emailView.setText(currMember.getEmail());
            attendView.setChecked(currMember.isAttend());

            if (mItemClickListener == null)
                return;

            addNoteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onAddNoteClicked(view, position, mMembers.get(position).getId());
                }
            });

            attendView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mItemClickListener.onAttendanceClicked
                            (compoundButton, position, b, mMembers.get(position).getId());
                }
            });
        }
    }

}