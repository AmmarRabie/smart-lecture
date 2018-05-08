package cmp.sem.team8.smarlecture.grades;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;

/**
 * Created by ramym on 5/1/2018.
 */

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ViewHolder> {
    private ArrayList<UserGradeModel> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView email;
        public TextView grade;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.grade_item_name);
            email = (TextView) itemView.findViewById(R.id.grade_item_email);
            grade = (TextView) itemView.findViewById(R.id.grade_item_grade);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GradesAdapter(ArrayList<UserGradeModel> mDataset) {
        this.mDataset = mDataset;
    }

    public void setList(ArrayList<UserGradeModel> mDataset) {
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listrow_user_grade, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        UserGradeModel user = mDataset.get(position);
        holder.name.setText(user.getName());
        holder.email.setText(user.getEmail());
        holder.grade.setText(user.getGrade());


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}