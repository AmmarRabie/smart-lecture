package cmp.sem.team8.smarlecture.grouplist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.group.groupAdapter;

/**
 * Created by Loai Ali on 3/19/2018.
 */

public class GroupListAdapter extends ArrayAdapter<HashMap<String, Object>> {

    private GroupListAdapter.onItemClickListenerInterface mOnChangeClickListener;
    private View.OnClickListener mOnDeleteClickListener;

    interface onItemClickListenerInterface {
        void onEditItemClick(View v, int position);

        void onDeleteItemClick(View v, int position);
    }

    public GroupListAdapter(Activity context, ArrayList<HashMap<String, Object>> nameList,
                        GroupListAdapter.onItemClickListenerInterface onEditClickListener) {
        super(context, 0, nameList);
        mOnChangeClickListener = onEditClickListener;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
        View listItemView=convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.item_grouplist,parent,false);
        }
        HashMap<String,Object> currGroup=getItem(position);
        TextView groupNameTextView=(TextView)listItemView.findViewById(R.id.groupList_groupName);
        groupNameTextView.setTag(currGroup.get("key"));
        groupNameTextView.setText(currGroup.get("name").toString());

        ImageView editImage = listItemView.findViewById(R.id.grouplist_editgroupname);
        ImageView deleteImage = listItemView.findViewById(R.id.grouplist_deletegroupname);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChangeClickListener.onEditItemClick(v,position);

            }

        });
        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChangeClickListener.onDeleteItemClick(v,position);
            }
        });
        return listItemView;

    }
}
