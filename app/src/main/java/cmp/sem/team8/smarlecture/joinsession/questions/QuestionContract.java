package cmp.sem.team8.smarlecture.joinsession.questions;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public interface QuestionContract {
    interface Views extends IBaseView<Actions> {
        void showOnErrorMessage(String cause);

        void showQuestions(ArrayList<QuestionModel> questions);

        void handleOfflineStates();

        boolean getOfflineState();

        void addQuestion(QuestionModel questionInserted);
    }

    interface Actions extends IBasePresenter {
        void submitQuestion(String questionText);
        void refresh();
    }
}
