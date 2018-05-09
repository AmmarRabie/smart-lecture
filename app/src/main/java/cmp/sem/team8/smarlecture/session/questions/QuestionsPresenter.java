package cmp.sem.team8.smarlecture.session.questions;

import cmp.sem.team8.smarlecture.common.auth.AuthService;
import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class QuestionsPresenter implements QuestionsContract.Actions {

    private static final String TAG = "QuestionsPresenter";
    private final String SESSION_ID;
    private final String USER_ID;
    private DataService.Listen questionListener;
    private QuestionsContract.Views mView;
    private DataService mDataSource;
    private boolean isLecturer;

    private DataService.Listen<QuestionModel> getQuestionsCallback;

    public QuestionsPresenter(DataService dataSource, AuthService auhService, QuestionsContract.Views mView, String SESSION_ID, boolean isLecturer) {
        mDataSource = dataSource;
        this.mView = mView;
        this.SESSION_ID = SESSION_ID;
        this.USER_ID = auhService.getCurrentUser().getUserId();
        this.isLecturer = isLecturer;
        getQuestionsCallback = new DataService.Listen<QuestionModel>() {
            @Override
            public void onDataReceived(QuestionModel dataSnapshot) {
                QuestionsPresenter.this.mView.addQuestion(dataSnapshot);
            }
        };

        // if session is closed alsom hide the question text box
        mDataSource.getSessionById(SESSION_ID, new DataService.Get<SessionModel>() {
            @Override
            public void onDataFetched(SessionModel data) {
                if (data.getSessionStatus()== DataService.SessionStatus.CLOSED)
                {
                    QuestionsPresenter.this.mView.hideQuestionTextBox();
                }
            }
        });

        mView.setPresenter(this);

    }

    public void start() {
        mView.handleOfflineStates();
        if (isLecturer)
            mView.hideQuestionTextBox();
        else
            mDataSource.getSessionStatus(SESSION_ID, new DataService.Get<DataService.SessionStatus>() {
                @Override
                public void onDataFetched(DataService.SessionStatus data) {
                    if(data.equals(DataService.SessionStatus.CLOSED)){
                        mView.hideQuestionTextBox();
                    }
                }
            });
        if (questionListener != null) {
            refresh();
            return;
        }
        questionListener = mDataSource.ListenSessionQuestions(SESSION_ID, getQuestionsCallback);
    }

    @Override
    public void submitQuestion(String questionText) {
        if (questionText == null || questionText.isEmpty()) {
            mView.showOnErrorMessage("question is empty");
            return;
        }
        mDataSource.addQuestionToSession(SESSION_ID, USER_ID, questionText, null);
    }

    @Override
    public void refresh() {
        mDataSource.forget(questionListener);
        mView.clearAllQuestions();
        questionListener = mDataSource.ListenSessionQuestions(SESSION_ID, getQuestionsCallback);
    }
}
