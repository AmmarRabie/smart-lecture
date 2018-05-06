package cmp.sem.team8.smarlecture.grades;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;

public class GradesActivity extends AppCompatActivity implements GradesContract.Views {

    EditText highestGrade;
    EditText lowestGrade;
    FloatingActionButton assignGrades;
    private RecyclerView mRecyclerView;
    private GradesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private GradesContract.Actions mPresenter;
    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        highestGrade=(EditText)findViewById(R.id.grades_input_highest_grade);
        lowestGrade=(EditText)findViewById(R.id.grades_input_lowest_grade);
        assignGrades=(FloatingActionButton)findViewById(R.id.grades_assign_grades);
        spinner =(ProgressBar)findViewById(R.id.grades_progress_bar);
        hideProgressIndicator();

        mPresenter=new GradesPresenter(this);


        mRecyclerView = (RecyclerView) findViewById(R.id.grades_list_view);

        mAdapter = new GradesAdapter(new ArrayList<UserGradeModel>());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mAdapter);

       // showErrorMessage("ERROR");

        mPresenter.start();


    }

    public void onClick(View button)
    {
        mPresenter.setGroupGrade();
    }

    @Override
    public void setPresenter(GradesContract.Actions presenter) {

        this.mPresenter=presenter;

    }

    @Override
    public void showErrorMessage(String cause) {

        Toast.makeText(GradesActivity.this, cause,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showGradesOfGroup(ArrayList<UserGradeModel> list) {

        mAdapter.setList(list);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public int getHighestGrade()
    {
        if (TextUtils.isEmpty(highestGrade.getText().toString()))
            throw new NullPointerException(" you must write a number");

        return Integer.parseInt(highestGrade.getText().toString());
    }

    @Override
    public int getLowestGrade()
    {
        if (TextUtils.isEmpty(lowestGrade.getText().toString()))
            throw new NullPointerException(" you must write a number");
        return Integer.parseInt(lowestGrade.getText().toString());
    }

    public void showProgressIndicator() {
        spinner.setVisibility(View.VISIBLE);
    }

    public void hideProgressIndicator() {
        spinner.setVisibility(View.GONE);
    }

    public String getGroupId(){
        return this.getIntent().getExtras().getString(getString(R.string.IKey_groupId));
    }

}
