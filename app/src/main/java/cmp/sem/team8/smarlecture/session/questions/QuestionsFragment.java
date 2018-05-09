package cmp.sem.team8.smarlecture.session.questions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.InternetConnectivityReceiver;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;
import es.dmoral.toasty.Toasty;

/**
 * Created by Mahmoud youssri on 11/03/2018.
 */

public class QuestionsFragment extends Fragment implements
        QuestionsContract.Views,
        InternetConnectivityReceiver.OnInternetConnectionChangeListener {

    private QuestionsContract.Actions mPresenter;

    private View mSendQuestionView;
    private TextView mQuestionTextView;
    private View mOfflineView;
    private RecyclerView mQuestionsRecyclerView;

    private QuestionsRecyclerAdapter mQuestionsRecyclerAdapter;

    private ArrayList<QuestionModel> mQuestionsList;

    private boolean mInternetState;

    private InternetConnectivityReceiver internetConnectivityReceiver;

    private boolean isInEmptyView = false;
    private boolean isOwner = false;

    public QuestionsFragment() {
    }

    public static QuestionsFragment newInstance() {
        return new QuestionsFragment();
    }

    public void setPresenter(QuestionsContract.Actions presenter) {

        mPresenter = presenter;

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_question, container, false);
        setHasOptionsMenu(true);

        mSendQuestionView = root.findViewById(R.id.addQuestionFrag_send);
        mQuestionTextView = root.findViewById(R.id.addQuestionFrag_text);
        mQuestionsRecyclerView = root.findViewById(R.id.addQuestionFrag_list);
        mOfflineView = root.findViewById(R.id.offlineView);

        mQuestionsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSendQuestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.submitQuestion(mQuestionTextView.getText().toString());
            }
        });

        mQuestionsList = new ArrayList<>();
        mQuestionsRecyclerAdapter = new QuestionsRecyclerAdapter(mQuestionsList);
        mQuestionsRecyclerView.setAdapter(mQuestionsRecyclerAdapter);

        return root;
    }


    @Override
    public void showOnErrorMessage(String cause) {
        Toasty.error(getContext(), cause, Toast.LENGTH_SHORT, true).show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_member_questions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionQuestion_refresh:
                mPresenter.refresh();
                return true;
        }
        return false;
    }

    @Override
    public void clearAllQuestions() {
        mQuestionsList.clear();
        mQuestionsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void hideQuestionTextBox() {
        mQuestionTextView.setVisibility(View.GONE);
        mSendQuestionView.setVisibility(View.GONE);
    }

    @Override
    public void handleOfflineStates() {
        internetConnectivityReceiver = new InternetConnectivityReceiver(this).start(getContext());
    }

    @Override
    public boolean getOfflineState() {
        return !mInternetState;
    }

    @Override
    public void addQuestion(QuestionModel questionInserted) {
        mQuestionsList.add(questionInserted);
        mQuestionsRecyclerAdapter.notifyItemInserted(mQuestionsList.size());
    }



    @Override
    public void onInternetConnectionLost() {
        mInternetState = false;
        mOfflineView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onInternetConnectionBack() {
        mInternetState = true;
        mOfflineView.setVisibility(View.GONE);

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mQuestionsList != null)
            mQuestionsList.clear();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        internetConnectivityReceiver.end(getContext());
    }


}