package cmp.sem.team8.smarlecture.session.question;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract;
import cmp.sem.team8.smarlecture.model.QuestionModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class QuestionPresenter implements QuestionContract.Actions {

    private static final String TAG = "ObjectiveListPresenter";

    private QuestionContract.Views mView;

    private AppDataSource mDataSource;

    private final String SESSION_ID;

    private DatabaseReference mQuestionRef;

    public QuestionPresenter(QuestionContract.Views mView, String SESSION_ID,AppDataSource dataSource) {

        this.mView = mView;

        mDataSource=dataSource;

        this.SESSION_ID = SESSION_ID;

        mQuestionRef = null;

        if (SESSION_ID == null) {

            Log.e(TAG, "QuestionPresenter: Session passed as null");

            return;
        }

        mView.setPresenter(this);

    }

    public void start() {

        mView.handleOfflineStates();

        FirebaseDatabase.getInstance().getReference(FirebaseContract.SessionEntry.KEY_THIS).child(SESSION_ID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mQuestionRef = FirebaseDatabase.getInstance().getReference(FirebaseContract.SessionEntry.KEY_THIS).child(SESSION_ID).

                            child(FirebaseContract.SessionEntry.KEY_FOR_QUESTION_LIST);

                    getQuestion();

                } else {

                    Log.e(TAG, "onDataChange: the Question presenter is called with invalid Session id");

                    mView.showOnErrorMessage("Session doesn't exist");

                    mQuestionRef = null;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                mQuestionRef = null;

                mView.showOnErrorMessage(databaseError.getMessage());

            }
        });

    }

    @Override
    public void getQuestion() {

        mDataSource.getQuestion(SESSION_ID, new AppDataSource.Get<ArrayList<QuestionModel>>() {
            @Override
            public void onDataFetched(ArrayList<QuestionModel> data) {
                mView.showQuestionList(data);

            }

            @Override
            public void onError(String cause) {
                mView.showOnErrorMessage(cause);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showOnErrorMessage("Wrong Session ID");
            }
        });

       /* if (mObjectiveRef == null)

            return;

        mObjectiveRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot objectivesSnapshot) {

                ArrayList<ObjectiveModel> objectiveList = new ArrayList<>();

                for (DataSnapshot child : objectivesSnapshot.getChildren()) {

                    if (!objectivesSnapshot.exists())
                        continue;

                    String key = child.getKey();

                    String name = child.child(FirebaseContract.ObjectiveEntry.KEY_DESC).getValue(String.class);

                   // float averageRating=child.child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).getValue(float.class);

                    Float averageRating = child.child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).getValue(Float.class);

                   // int numberUsersRated=child.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).getValue(int.class);
                    Integer numberUsersRated = child.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).getValue(Integer.class);

                    ObjectiveModel thisObjective = new ObjectiveModel();

                    thisObjective.setmObjectiveDescription(name);

                    thisObjective.setmObjectiveID(key);

                    thisObjective.setmNumberofUsersRatedThisObjective(numberUsersRated);

                    thisObjective.setmObjectivesAverageRating(averageRating);

                    thisObjective.setmSessionID(SESSION_ID);

                    objectiveList.add(thisObjective);
                }

                mView.showObjectivesList(objectiveList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                mView.showOnErrorMessage(databaseError.getMessage());

            }
        });
*/

    }

    @Override
    public void addQuestion(final String QuestionDescription) {

        if (mQuestionRef == null) {

            Log.e(TAG, "addSQuestion: called without finding the Session");

            return;
        }

        if (QuestionDescription == null || QuestionDescription.isEmpty()) {

            mView.showOnErrorMessage("Question description can't be empty");

            return;
        }

        DatabaseReference newQuestion = mQuestionRef.push();

        final String key = newQuestion.getKey();

        final boolean isOffline = mView.getOfflineState();

        if (isOffline) {

            QuestionModel addedQuestion = new QuestionModel();

            addedQuestion.setmSessionID(SESSION_ID);

            addedQuestion.setmQuestionID(key);

            addedQuestion.setmQuestionDescription(QuestionDescription);

            //addedQuestion.setmQuestionAverageRating(0);

           // addedQuestion.setmNumberofUsersRatedThisObjective(0);

            mView.onAddSuccess(addedQuestion);
        }
        //newQuestion.child(FirebaseContract.ObjectiveEntry.KEY_NUM_OF_USER_RATED).setValue(0);

       // newObjective.child(FirebaseContract.ObjectiveEntry.KEY_AVERAGERATING).setValue(0);

        newQuestion.child(FirebaseContract.ObjectiveEntry.KEY_DESC).setValue(QuestionDescription).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    if (!isOffline) {

                        QuestionModel addedQuestion = new QuestionModel();

                        addedQuestion.setmSessionID(SESSION_ID);

                        addedQuestion.setmQuestionID(key);

                        addedQuestion.setmQuestionDescription(QuestionDescription);

                   //     addedQuestion.setmNumberofUsersRatedThisObjective(0);

                   //     addedQuestion.setmObjectivesAverageRating(0);


                        mView.onAddSuccess(addedQuestion);

                    }
                } else {

                    mView.showOnErrorMessage(task.getException().getMessage());
                }
            }
        });


    }

    @Override
    public void editQuestion(String QuestionID, String QuestionDescription) {

        if (mQuestionRef == null)

            return;


    }

    @Override
    public void deleteQuestion(String QuestionID) {

    }
}
