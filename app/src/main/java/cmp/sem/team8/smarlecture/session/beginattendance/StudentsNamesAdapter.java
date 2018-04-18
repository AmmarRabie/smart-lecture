package cmp.sem.team8.smarlecture.session.beginattendance;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.model.UserAttendanceModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class StudentsNamesAdapter extends BaseAdapter {

    private Activity activity;
    private int resource;
    private List<String> list;
    private LayoutInflater inflater;

    public StudentsNamesAdapter(Activity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.studnet_name_item, parent, false);
            holder = new ViewHolder();
            holder.StudentName = (TextView) convertView.findViewById(R.id.student_name_item_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = list.get(position);
        holder.StudentName.setText(name);
        return convertView;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public void updateRecords(List<String> Students) {
        this.list = Students;
        notifyDataSetChanged();
    }

    class ViewHolder {

        TextView StudentName;
    }

}

