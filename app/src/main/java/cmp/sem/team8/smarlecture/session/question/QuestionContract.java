package cmp.sem.team8.smarlecture.session.question;

import java.util.ArrayList;
import java.util.HashMap;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.model.QuestionModel;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public interface QuestionContract {
    interface Views extends IBaseView<Actions> {
        void showOnErrorMessage(String cause);

        void onDeleteSuccess(String deletedQuestionID);

        void onEditSuccess(String QuestionID, String newQuestionDescription);

        void onAddSuccess(QuestionModel addedObjective);

        void showQuestionList(ArrayList<QuestionModel> QuestionList);

        void handleOfflineStates();

        boolean getOfflineState();

    }

    interface Actions extends IBasePresenter {

        void getQuestion();

        void addQuestion(String QuestionDescription);

        void editQuestion(String QuestionID, String QuestionDescription);

        void deleteQuestion(String QuestionID);


    }
}
