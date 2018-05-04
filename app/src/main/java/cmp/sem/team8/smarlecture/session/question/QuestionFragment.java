package cmp.sem.team8.smarlecture.session.question;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.CorneredSort;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.R;
import cmp.sem.team8.smarlecture.common.InternetConnectivityReceiver;
import cmp.sem.team8.smarlecture.group.studentlist.StudentListContract;

import cmp.sem.team8.smarlecture.model.QuestionModel;
import cmp.sem.team8.smarlecture.session.PagerAdapter;

/**
 * Created by Mahmoud youssri on 11/03/2018.
 */

public class QuestionFragment extends android.support.v4.app.Fragment implements
        QuestionContract.Views,
        QuestionRecyclerAdapter.OnItemClickListener, InternetConnectivityReceiver.OnInternetConnectionChangeListener {

    Animator spruceAnimator;

    private QuestionContract.Actions mPresenter;

    private FloatingActionButton mAddQuestion;

    private View mOfflineView;

    private RecyclerView mQuestionRecyclerView;

    private QuestionRecyclerAdapter mQuestionAdapter;

    private ArrayList<QuestionModel> mQuestionList;

    private View.OnClickListener mAddQuestionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

            final EditText questionNameView = buildEditTextDialogView("Question", null);

            mBuilder.setView(questionNameView);

            mBuilder.setTitle(" your Question");

            mBuilder.setPositiveButton(getString(R.string.dAction_add), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    mPresenter.addQuestion(questionNameView.getText().toString());

                    dialogInterface.dismiss();

                }
            });

            mBuilder.setNegativeButton(getString(R.string.dAction_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                }
            });

            mBuilder.show();
        }
    };

    private boolean mInternetState;

    private InternetConnectivityReceiver internetConnectivityReceiver;

    private boolean isInEmptyView = false;

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance() {
        return new QuestionFragment();
    }

    public void setPresenter(QuestionContract.Actions presenter) {

        mPresenter = presenter;

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_question, container, false);

        setHasOptionsMenu(true);

        mAddQuestion = root.findViewById(R.id.questionlistfrag_addobjective);

        mOfflineView = root.findViewById(R.id.offlineView);

        mQuestionRecyclerView = root.findViewById(R.id.questionlistfrag_list);

        mAddQuestion.setOnClickListener(mAddQuestionClickListener);

        mQuestionList = new ArrayList<>();

        mQuestionAdapter = new QuestionRecyclerAdapter(getContext(), mQuestionList, this);

        mQuestionRecyclerView.setAdapter(mQuestionAdapter);

        mQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

                super.onLayoutChildren(recycler, state);

                if (state.didStructureChange())

                    spruceAnimator = new Spruce.SpruceBuilder(mQuestionRecyclerView)

                            .sortWith(new CorneredSort(150, false, CorneredSort.Corner.TOP_LEFT))

                            .animateWith(DefaultAnimations.fadeInAnimator(mQuestionRecyclerView, 800)

                                    , ObjectAnimator.ofFloat(mQuestionRecyclerView, "translationX", -mQuestionRecyclerView.getWidth(), 0f).setDuration(800)
                            )

                            .start();
            }
        });


        return root;

    }


    @Override
    public void showOnErrorMessage(String cause) {
        Toast.makeText(getContext(), cause, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteSuccess(String deletedQuestionID) {

        int position = 0;

        while (!deletedQuestionID.equals(mQuestionList.get(position).getmQuestionID())) {
            position++;
        }

        mQuestionList.remove(position);

        mQuestionAdapter.notifyDataSetChanged();

    }

    @Override
    public void onEditSuccess(String QuestionID, String newQuestionDescription) {

        int position = 0;

        while (!QuestionID.equals(mQuestionList.get(position).getmQuestionID())) {
            position++;
        }

        mQuestionList.get(position).setmQuestionDescription(newQuestionDescription);

        mQuestionAdapter.notifyDataSetChanged();

    }

    @Override

    public void onAddSuccess(QuestionModel addedQuestion) {

        mQuestionList.add(addedQuestion);

        mQuestionAdapter.notifyDataSetChanged();

    }

    @Override
    public void showQuestionList(ArrayList<QuestionModel> QuestionList) {

        if (mQuestionList.equals(QuestionList))

            return;

        mQuestionList.clear();

        mQuestionList.addAll(QuestionList);

        mQuestionAdapter.notifyDataSetChanged();

    }

    @Override
    public void handleOfflineStates() {

        internetConnectivityReceiver =

                new InternetConnectivityReceiver(this).start(getContext());

    }

    @Override
    public boolean getOfflineState() {
        return !mInternetState;
    }

    @Override
    public void onDeleteQuestionClick(View view, int position) {
        mPresenter.deleteQuestion(mQuestionList.get(position).getmQuestionID());

    }

    @Override
    public void onEditQuestionClick(View view, int position) {

        final String QuestionId = mQuestionList.get(position).getmQuestionID();

        final String QuestionDescription = mQuestionList.get(position).getmQuestionDescription();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

        final EditText newDescriptionView = buildEditTextDialogView(null, QuestionDescription);

        mBuilder.setView(newDescriptionView);

        mBuilder.setTitle("Edit Question description");

        mBuilder.setIcon(android.R.drawable.ic_menu_edit);

        mBuilder.setPositiveButton(getString(R.string.dAction_change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String QuestionDescription = newDescriptionView.getText().toString();

                mPresenter.editQuestion(QuestionId, QuestionDescription);

                dialogInterface.dismiss();
            }
        });
        mBuilder.setNegativeButton(getString(R.string.dAction_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        mBuilder.show();

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

    private EditText buildEditTextDialogView(String hint, String text) {

        EditText input = new EditText(getContext());

        input.setLayoutParams(new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                LinearLayout.LayoutParams.MATCH_PARENT));

        input.setHint(hint);

        input.setHintTextColor(getContext().getResources().getColor(android.R.color.secondary_text_dark));

        input.setText(text);

        input.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));

        return input;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        internetConnectivityReceiver.end(getContext());
    }


}