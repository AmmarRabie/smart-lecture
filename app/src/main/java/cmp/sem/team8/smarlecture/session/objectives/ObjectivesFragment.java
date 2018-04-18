package cmp.sem.team8.smarlecture.session.objectives;

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

import cmp.sem.team8.smarlecture.model.ObjectiveModel;
import cmp.sem.team8.smarlecture.session.PagerAdapter;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class ObjectivesFragment extends android.support.v4.app.Fragment implements
        ObjectivesContract.Views,
        ObjectivesRecyclerAdapter.OnItemClickListener, InternetConnectivityReceiver.OnInternetConnectionChangeListener,
        PagerAdapter.FragmentLifeCycle{

    Animator spruceAnimator;

    private ObjectivesContract.Actions mPresenter;

    private FloatingActionButton mAddObjective;

    private View mOfflineView;

    private RecyclerView mObjectiveRecyclerView;

    private ObjectivesRecyclerAdapter mObjectivesAdapter;

    private ArrayList<ObjectiveModel> mObjectiveList;

    private View.OnClickListener mAddObjectiveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

            final EditText objectiveNameView = buildEditTextDialogView("Name", null);

            mBuilder.setView(objectiveNameView);

            mBuilder.setTitle(getString(R.string.dTitle_addObjective));

            mBuilder.setPositiveButton(getString(R.string.dAction_add), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    mPresenter.addObjective(objectiveNameView.getText().toString());

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

    public ObjectivesFragment() {
    }

    public static ObjectivesFragment newInstance() {
        return new ObjectivesFragment();
    }

    public void setPresenter(ObjectivesContract.Actions presenter) {

        mPresenter = presenter;

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.frag_objectives, container, false);

        setHasOptionsMenu(true);

        mAddObjective = root.findViewById(R.id.objectiveslistfrag_addobjective);

        mOfflineView = root.findViewById(R.id.offlineView);

        mObjectiveRecyclerView = root.findViewById(R.id.objectivelistfrag_list);

        mAddObjective.setOnClickListener(mAddObjectiveClickListener);

        mObjectiveList = new ArrayList<>();

        mObjectivesAdapter = new ObjectivesRecyclerAdapter(getContext(), mObjectiveList, this);

        mObjectiveRecyclerView.setAdapter(mObjectivesAdapter);

        mObjectiveRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

                super.onLayoutChildren(recycler, state);

                if (state.didStructureChange())

                    spruceAnimator = new Spruce.SpruceBuilder(mObjectiveRecyclerView)

                            .sortWith(new CorneredSort(150, false, CorneredSort.Corner.TOP_LEFT))

                            .animateWith(DefaultAnimations.fadeInAnimator(mObjectiveRecyclerView, 800)

                                    , ObjectAnimator.ofFloat(mObjectiveRecyclerView, "translationX", -mObjectiveRecyclerView.getWidth(), 0f).setDuration(800)
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
    public void onDeleteSuccess(String deletedObjectiveID) {

        int position = 0;

        while (!deletedObjectiveID.equals(mObjectiveList.get(position).getmObjectiveID())) {
            position++;
        }

        mObjectiveList.remove(position);

        mObjectivesAdapter.notifyDataSetChanged();

    }

    @Override
    public void onEditSuccess(String objectiveID, String newObjectiveDesription) {

        int position = 0;

        while (!objectiveID.equals(mObjectiveList.get(position).getmObjectiveID())) {
            position++;
        }

        mObjectiveList.get(position).setmObjectiveDescription(newObjectiveDesription);

        mObjectivesAdapter.notifyDataSetChanged();

    }

    @Override

    public void onAddSuccess(ObjectiveModel addedObjective) {

        mObjectiveList.add(addedObjective);

        mObjectivesAdapter.notifyDataSetChanged();

    }

    @Override
    public void showObjectivesList(ArrayList<ObjectiveModel> objectivesList) {

        if (mObjectiveList.equals(objectivesList))

            return;

        mObjectiveList.clear();

        mObjectiveList.addAll(objectivesList);

        mObjectivesAdapter.notifyDataSetChanged();

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
    public void onDeleteObjectiveClick(View view, int position) {
        mPresenter.deleteObjective(mObjectiveList.get(position).getmObjectiveID());

    }

    @Override
    public void onEditObjectiveClick(View view, int position) {

        final String objectiveId = mObjectiveList.get(position).getmObjectiveID();

        final String objectiveDescription = mObjectiveList.get(position).getmObjectiveDescription();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());

        final EditText newDescriptionView = buildEditTextDialogView(null, objectiveDescription);

        mBuilder.setView(newDescriptionView);

        mBuilder.setTitle("Edit objective description");

        mBuilder.setIcon(android.R.drawable.ic_menu_edit);

        mBuilder.setPositiveButton(getString(R.string.dAction_change), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String objectiveDescription = newDescriptionView.getText().toString();

                mPresenter.editObjective(objectiveId, objectiveDescription);

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

    @Override
    public void onPauseFragment() {
        super.onPause();
        internetConnectivityReceiver.end(getContext());

    }

    @Override
    public void onResumeFragment() {
        super.onResume();
        mPresenter.start();

    }
}
