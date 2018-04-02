package cmp.sem.team8.smarlecture.group;

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
 * Created by Loai Ali on 3/17/2018.
 */

public class GroupAdapter extends ArrayAdapter<HashMap<String, Object>> {

    private onItemClickListner mItemClickListener=null;
   // private View.OnClickListener mOnDeleteClickListener;

    interface onItemClickListner {
        void onEditItemClick(View v, int position);

        void onDeleteItemClick(View v, int position);
    }



    public GroupAdapter(Context context, ArrayList<HashMap<String, Object>> nameList,
                        onItemClickListner onEditClickListener) {

        this(context,nameList);
        mItemClickListener = onEditClickListener;
    }
    public GroupAdapter(Context context,ArrayList<HashMap<String,Object>>   namesList){
        super(context,0,namesList);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_group, parent, false);
        }
        HashMap<String, Object> currName = getItem(position);
        TextView nameTextView = listItemView.findViewById(R.id.group_studentName);

        nameTextView.setTag(currName.get("key"));

        nameTextView.setText(currName.get("name").toString());

        if (mItemClickListener == null)
            return listItemView;
        listItemView.findViewById(R.id.group_editname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onEditItemClick(v,position);
            }
        });
        listItemView.findViewById(R.id.group_deletename).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onDeleteItemClick(v,position);
            }
        });



        return listItemView;
    }
    public void setOnItemClickListener(onItemClickListner onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }
}
