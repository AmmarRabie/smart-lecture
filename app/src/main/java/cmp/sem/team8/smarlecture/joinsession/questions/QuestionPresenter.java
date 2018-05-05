package cmp.sem.team8.smarlecture.joinsession.questions;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class QuestionPresenter implements QuestionContract.Actions {

    private static final String TAG = "QuestionPresenter";

    private final String SESSION_ID;
    private final String USER_ID;
    private QuestionContract.Views mView;
    private AppDataSource mDataSource;

    private AppDataSource.Get<ArrayList<QuestionModel>> getQuestionsCallback;

    public QuestionPresenter(AppDataSource dataSource, AuthService auhService, QuestionContract.Views mView, String SESSION_ID) {
        mDataSource = dataSource;
        this.mView = mView;
        this.SESSION_ID = SESSION_ID;
        this.USER_ID = auhService.getCurrentUser().getUserId();

        getQuestionsCallback = new AppDataSource.Get<ArrayList<QuestionModel>>() {
            @Override
            public void onDataFetched(ArrayList<QuestionModel> questions) {
                QuestionPresenter.this.mView.showQuestions(questions);
            }
        };

        mView.setPresenter(this);
    }

    public void start() {
        mView.handleOfflineStates();
        mDataSource.getSessionQuestions(SESSION_ID, getQuestionsCallback);
    }

    @Override
    public void submitQuestion(String questionText) {
        if (questionText == null || questionText.isEmpty()) {
            mView.showOnErrorMessage("question is empty");
            return;
        }
        mDataSource.addQuestionToSession(SESSION_ID,USER_ID, questionText, new AppDataSource.Insert<QuestionModel>() {
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
