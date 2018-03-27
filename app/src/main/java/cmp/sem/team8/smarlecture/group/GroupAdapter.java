package cmp.sem.team8.smarlecture.group;

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

/**
 * Created by Loai Ali on 3/17/2018.
 */

public class GroupAdapter extends ArrayAdapter<HashMap<String, Object>> {

    private onItemClickListenerInterface mOnChangeClickListener;
    private View.OnClickListener mOnDeleteClickListener;

    interface onItemClickListenerInterface {
        void onEditItemClick(View v, int position);

        void onDeleteItemClick(View v, int position);
    }



    public GroupAdapter(Activity context, ArrayList<HashMap<String, Object>> nameList,
                        onItemClickListenerInterface onEditClickListener) {
        super(context, 0, nameList);
        mOnChangeClickListener = onEditClickListener;
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

        ImageView editImage = listItemView.findViewById(R.id.group_editname);
        ImageView deleteImage = listItemView.findViewById(R.id.group_deletename);


        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChangeClickListener.onEditItemClick(v, position);
            }
        });

//        deleteImage.setOnClickListener(mOnDeleteClickListener);

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnChangeClickListener.onDeleteItemClick(v, position);
            }
        });

        return listItemView;
    }
}
