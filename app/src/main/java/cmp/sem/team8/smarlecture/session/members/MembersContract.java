package cmp.sem.team8.smarlecture.session.members;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;

/**
 * Created by ramym on 3/17/2018.
 */

public class MembersContract {

    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        /**
         * to show message error message
         */
        void showErrorMessage(String cause);

        /**
         * called from presenter when the session status is not activated it 
         */
        void showBeginAttendanceButton();

        void hideBeginAttendanceButton();

        /**
         * to show the timer of the attendance in the fragment;
         */
        void startAttendanceTimer(int minutes);


        /**
         * to show attendance secret code
         */
        void showSecret(String secret);

        String getSecret();

        void addNewMember(MemberModel newAttendee);

        void updateMemberAttendance(String id, boolean attend);

        void onNoteAddedSuccess(String memberId, NoteModel note);

        void onDeleteNoteSuccess(String memberId, NoteModel noteDeleted);
    }


    /**
     * Actions methods implemented by presenter
     */
    interface Actions extends IBasePresenter {

        /**
         * steps
         * 1- make attendance secrect
         * 2- put listener on session object in the database
         * if any new student mark himself as attendant
         * thi listView in the fragment will show his name
         */
        void BeginAttendance();

        void onAttendanceMarkChanged(String memberId, boolean attend);

        /**
         * mark attendance flag in the database as closed;
         */
        void onAttendanceTimerEnd();

        void onDestroy();

        void addNote(String memberIdShowing, String noteText);

        void deleteNote(String memberIdShowing, String noteId);
    }
}
