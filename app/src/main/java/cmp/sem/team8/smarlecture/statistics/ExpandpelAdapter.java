package cmp.sem.team8.smarlecture.statistics;
import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.GroupOfUsersModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by javiergonzalezcabezas on 11/5/15.
 */
public class ExpandpelAdapter extends BaseExpandableListAdapter {

    private final SparseArray<GroupOfUsersModel> mGroups;
    public LayoutInflater mInflater;
    public Activity mActivity;

    public ExpandpelAdapter(Activity act, SparseArray<GroupOfUsersModel> groups) {
        mActivity = act;
        this.mGroups = groups;
        mInflater = act.getLayoutInflater();
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final UserModel children = (UserModel) getChild(groupPosition, childPosition);
        TextView text = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listrow_details, null);
        }
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(children.getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity, children.getEmail(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listrow_group, null);
        }
        GroupOfUsersModel group = (GroupOfUsersModel) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}