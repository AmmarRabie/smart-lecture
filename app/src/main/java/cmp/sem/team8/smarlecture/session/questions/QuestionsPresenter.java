package cmp.sem.team8.smarlecture.session.questions;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class QuestionsPresenter implements QuestionsContract.Actions {

    private static final String TAG = "QuestionsPresenter";

    private final String SESSION_ID;
    private final String USER_ID;
    private QuestionsContract.Views mView;
    private DataService mDataSource;
    private boolean isLecturer;

    private DataService.Get<ArrayList<QuestionModel>> getQuestionsCallback;

    public QuestionsPresenter(DataService dataSource, AuthService auhService, QuestionsContract.Views mView, String SESSION_ID, boolean isLecturer) {
        mDataSource = dataSource;
        this.mView = mView;
        this.SESSION_ID = SESSION_ID;
        this.USER_ID = auhService.getCurrentUser().getUserId();
this.isLecturer=isLecturer;
        getQuestionsCallback = new DataService.Get<ArrayList<QuestionModel>>() {
            @Override
            public void onDataFetched(ArrayList<QuestionModel> questions) {
                QuestionsPresenter.this.mView.showQuestions(questions);
            }
        };

        mView.setPresenter(this);

    }

    public void start() {
        mView.handleOfflineStates();
        mDataSource.getSessionQuestions(SESSION_ID, getQuestionsCallback);
        if(isLecturer)
            mView.hideQuestionTextBox();

    }

    @Override
    public void submitQuestion(String questionText) {
        if (questionText == null || questionText.isEmpty()) {
            mView.showOnErrorMessage("question is empty");
            return;
        }
        mDataSource.addQuestionToSession(SESSION_ID,USER_ID, questionText, new DataService.Insert<QuestionModel>() {
            @Override
            public void onDataInserted(QuestionModel questionInserted) {
                mView.addQuestion(questionInserted);
            }
        });
    }

    @Override
    public void refresh() {
        mDataSource.getSessionQuestions(SESSION_ID, getQuestionsCallback);
    }
}
