package cmp.sem.team8.smarlecture.joinsession.writeattendance;

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
import cmp.sem.team8.smarlecture.common.data.model.UserAttendanceModel;

/**
 * Created by ramym on 3/20/2018.
 */

public class StudentAttendanceAdapter extends BaseAdapter {

    private Activity activity;
    private int resource;
    private List<UserAttendanceModel> list;
    private LayoutInflater inflater;
    private int selectedPosition = -1;

    public StudentAttendanceAdapter(Activity activity, List<UserAttendanceModel> list) {
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
            convertView = inflater.inflate(R.layout.student_attendance_item, parent, false);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.student_attendance_item_name);
            holder.radioButton = (RadioButton) convertView.findViewById(R.id.student_attendance_item_radio);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserAttendanceModel model = list.get(position);
        holder.radioButton.setChecked(model.getChecked());

        holder.userName.setText(model.getName());
        return convertView;
    }


    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public void updateRecords(List<UserAttendanceModel> Students) {
        this.list = Students;
        notifyDataSetChanged();
    }

    class ViewHolder {

        TextView userName;
        RadioButton radioButton;
    }

}
