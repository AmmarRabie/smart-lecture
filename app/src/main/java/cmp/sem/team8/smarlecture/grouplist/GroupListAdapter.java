package cmp.sem.team8.smarlecture.grouplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by Loai Ali on 3/19/2018.
 */

public class GroupListAdapter extends ArrayAdapter<HashMap<String, Object>> {

    private OnItemClickListener mItemClickListener = null;
    private View.OnClickListener mOnDeleteClickListener;

    public GroupListAdapter(Context context, ArrayList<HashMap<String, Object>> nameList,
                            OnItemClickListener onEditClickListener) {
        this(context, nameList);
        mItemClickListener = onEditClickListener;
    }


    public GroupListAdapter(Context context, ArrayList<HashMap<String, Object>> nameList) {
        super(context, 0, nameList);
    }


    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_grouplist, parent, false);
        }
        HashMap<String, Object> currGroup = getItem(position);
        TextView groupNameView = (TextView) listItemView.findViewById(R.id.groupList_groupName);

        groupNameView.setText(currGroup.get("name").toString());

        if (mItemClickListener == null)
            return listItemView;

        // set listeners to the whole view and the startSession view
        listItemView.findViewById(R.id.grouplist_startSession)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onStartSessionClick(view, position);
                    }
                });
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(view, position);
            }
        });
        return listItemView;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }


    interface OnItemClickListener {

        void onStartSessionClick(View view, int position);

        /**
         * called when the whole item is clicked
         *
         * @param view     the view clicked
         * @param position its position in the list
         */
        void onItemClick(View view, int position);

    }
}
