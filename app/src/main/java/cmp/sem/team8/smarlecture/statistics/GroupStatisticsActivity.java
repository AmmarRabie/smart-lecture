package cmp.sem.team8.smarlecture.statistics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.GroupOfUsersModel;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class GroupStatisticsActivity extends AppCompatActivity implements  GroupStatisticsContract.Views {

        // more efficient than HashMap for mapping integers to objects
        ExpandableAdapter adapter;

    private ProgressBar spinner;

        String groupID;
        ExpandableListView mListView;
        TextView Attendance_Percentage;
        TextView Percentage_text;
        GroupStatisticsContract.Actions mPresenter;
        ScrollView fullView;
        LinearLayout emptyView;
        ProgressBar begSpinner;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_group_statistics);

            groupID=this.getIntent().getExtras().getString(getString(R.string.IKey_groupId));
            ImageView imageView = (ImageView) findViewById(R.id.group_statistics_list_view_icon);

            spinner = (ProgressBar)findViewById(R.id.group_statistics_progress_bar);


            Attendance_Percentage=(TextView)findViewById(R.id.group_statistics_attendance_percentage);
            Attendance_Percentage.setVisibility(View.GONE);
            Percentage_text=(TextView)findViewById(R.id.group_statistics_text_percentage);
            Percentage_text.setVisibility(View.GONE);
            emptyView=(LinearLayout)findViewById(R.id.statisitcs_empty_view);
            fullView=(ScrollView)findViewById(R.id.activity_expandable_scroll_view);
            begSpinner=(ProgressBar)findViewById(R.id.statisitcs_progress_bar_beg);
            begSpinner.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            fullView.setVisibility(View.GONE);
            mPresenter=new GroupStatisticsPresenter(this);

            adapter = new ExpandableAdapter(this, mPresenter.getGroupsOfUsers());
            mPresenter.start();

            mListView = (ExpandableListView) findViewById(R.id.group_statistics_list_view);

            mListView.setAdapter(adapter);
            mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    setListViewHeight(parent, groupPosition);
                    return false;
                }
            });


        }



        private void setListViewHeight(ExpandableListView listView,
                                       int group) {
            ExpandableListAdapter listAdapter = (ExpandableListAdapter) listView.getExpandableListAdapter();
            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                    View.MeasureSpec.EXACTLY);
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                View groupItem = listAdapter.getGroupView(i, false, null, listView);
                groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                totalHeight += groupItem.getMeasuredHeight();

                if (((listView.isGroupExpanded(i)) && (i != group))
                        || ((!listView.isGroupExpanded(i)) && (i == group))) {
                    for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                        View listItem = listAdapter.getChildView(i, j, false, null,
                                listView);
                        listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                        totalHeight += listItem.getMeasuredHeight();

                    }
                }
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            int height = totalHeight
                    + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
            if (height < 10)
                height = 200;
            params.height = height;
            listView.setLayoutParams(params);
            listView.requestLayout();

        }

    @Override
    public void setPresenter(GroupStatisticsContract.Actions presenter) {

            this.mPresenter=presenter;
    }

    @Override
    public void showAttendancePercentage(double Percentage) {
            Percentage_text.setVisibility(View.VISIBLE);
            Attendance_Percentage.setVisibility(View.VISIBLE);
            Attendance_Percentage.setText(Double.toString(Percentage));
    }

    @Override
    public void showMostAttendantUsers(List<UserGradeModel> users) {
        GroupOfUsersModel group=new GroupOfUsersModel("Best Users");
        for (int i=0;i< users.size();i++)
        {
            UserModel model=users.get(i);
            group.children.add(model);
        }

        mPresenter.getGroupsOfUsers().append(1,group);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showWorstAttendantUsers(List<UserGradeModel> users) {

        GroupOfUsersModel group=new GroupOfUsersModel("worst Users");
        for (int i=0;i< users.size();i++)
        {
            UserModel model=users.get(i);
            group.children.add(model);
        }

        mPresenter.getGroupsOfUsers().append(0,group);
        adapter.notifyDataSetChanged();
    }

    public String getGroupID()
    {
        return groupID;
    }

    @Override
    public void showProgressIndicator() {
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
        spinner.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyView() {

            begSpinner.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            fullView.setVisibility(View.GONE);
    }

    @Override
    public void hideEmptyView()
    {
        begSpinner.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        fullView.setVisibility(View.VISIBLE);
    }


}

