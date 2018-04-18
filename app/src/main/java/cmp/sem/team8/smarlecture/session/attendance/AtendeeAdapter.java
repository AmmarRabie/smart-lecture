package cmp.sem.team8.smarlecture.session.attendance;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cmp.sem.team8.smarlecture.R;

/**
 * Created by AmmarRabie on 12/03/2018.
 */

public class AtendeeAdapter extends ArrayAdapter<Map> {

    private static final String TAG = "AtendeeAdapter";


    public AtendeeAdapter(Activity context, List<Map> androidFlavors) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, androidFlavors);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_atendee, parent, false);
        }

        Map<String, Object> currentAndroidFlavor = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.atendee_name);

        nameTextView.setText(currentAndroidFlavor.get("name").toString());

        CheckBox isAttend = listItemView.findViewById(R.id.atendee_attend_flag);

        boolean attendFlag = (boolean) currentAndroidFlavor.get("attend");
        isAttend.setChecked(attendFlag);

        View container = listItemView.findViewById(R.id.atendee_container);
        container.setBackgroundColor(100);
        if (attendFlag)
            container.setBackgroundColor(1000);

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
    }

}