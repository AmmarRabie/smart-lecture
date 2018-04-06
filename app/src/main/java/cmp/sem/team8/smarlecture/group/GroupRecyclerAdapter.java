package cmp.sem.team8.smarlecture.group;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by Loai Ali on 3/17/2018.
 */

public class GroupRecyclerAdapter extends RecyclerView.Adapter<GroupRecyclerAdapter.GroupViewHolder> {

    private final static int VIEW_TYPE_CELL = 0;
    private final static int VIEW_TYPE_FOOTER = 1;
    private onItemClickListener mItemClickListener = null;
    private ArrayList<HashMap<String, Object>> mNamesList;
    private Context mContext;

    public GroupRecyclerAdapter(Context context, ArrayList<HashMap<String, Object>> nameList,
                                onItemClickListener onEditClickListener) {

        this(context, nameList);
        mItemClickListener = onEditClickListener;
    }

    public GroupRecyclerAdapter(Context context, ArrayList<HashMap<String, Object>> namesList) {
        this.mContext = context;
        this.mNamesList = namesList;
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CELL) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_group, parent, false);
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
        if (position == mNamesList.size()) {
            holder.bindLastView(position);
            return;
        }
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNamesList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mNamesList.size()) ? VIEW_TYPE_FOOTER : VIEW_TYPE_CELL;
    }

    interface onItemClickListener {
        void onEditItemClick(View v, int position);

        void onDeleteItemClick(View v, int position);

        void onSaveItemClick(View v, String name, int position);
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private View deleteImageView;
        private View editImageView;

        private EditText newNameView;
        private ImageView saveImageView;

        GroupViewHolder(View itemView, int type) {
            super(itemView);
            if (type == VIEW_TYPE_CELL) {
                nameTextView = itemView.findViewById(R.id.group_studentName);
                deleteImageView = itemView.findViewById(R.id.group_deletename);
                editImageView = itemView.findViewById(R.id.group_editname);
                return;
            }
            newNameView = itemView.findViewById(R.id.groupNewName_name);
            saveImageView = itemView.findViewById(R.id.groupNewName_save);
        }

        void bind(final int position) {

            HashMap<String, Object> currName = mNamesList.get(position);

            nameTextView.setText(currName.get("name").toString());

            if (mItemClickListener == null)
                return;

            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onEditItemClick(v, position);
                }
            });
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onDeleteItemClick(v, position);
                }
            });

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
