package cmp.sem.team8.smarlecture.session.create.members;

import cmp.sem.team8.smarlecture.IBasePresenter;
import cmp.sem.team8.smarlecture.IBaseView;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;

/**
 * Created by ramym on 3/17/2018.
 */

public interface MembersContract {

    /**
     * views methods implemented by fragment
     */
    interface Views extends IBaseView<Actions> {

        /**
         * to show message error message
         */
        void showErrorMessage(String cause);

        /**
         * called from presenter when the session status is not activated
         */
        void showBeginAttendanceButton();

        /**
         * called when the session status is open or closed
         */

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

        /**
         * called when the user wants to add a new member to the group
         * @param newAttendee the member information that will be added to this group
         */
        void addNewMember(MemberModel newAttendee);

        /**
         *this function  is called from the presenter after changing the state of his attendance
         * in this session
         * @param id the id of the student
         * @param attend the  state if true means he attended the session false otherwise
         */
        void updateMemberAttendance(String id, boolean attend);

        /**
         * called from the presenter when the addition of a note to a certain user is successful
         * @param memberId the id of the member
         * @param note the note description
         */

        void onNoteAddedSuccess(String memberId, NoteModel note);

        /**
         * called from the presenter when the deletion of a note is successful
         * @param memberId the id of the member
         * @param noteDeleted the deleted note
         */
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

        /**
         * called when the user clicks on the attendance mark
         * @param memberId
         * @param attend
         */
        void onAttendanceMarkChanged(String memberId, boolean attend);

        /**
         * mark attendance flag in the database as closed;
         */
        void onAttendanceTimerEnd();

        void onDestroy();

        /**
         * called from the fragment when the user clicks on add note
         * on succesfull addition the on onNoteAddedSuccess method is called
         * @param memberIdShowing the id of the member that this note is about him
         * @param noteText the text of the note
         */

        void addNote(String memberIdShowing, String noteText);

        /**
         * called from the presenter when the user clicks on delete note
         * on successful deletion the onNoteDeletedSuccess method is called
         * @param memberIdShowing the id of the user that the note is about him
         * @param noteId the id of the note
         */
        void deleteNote(String memberIdShowing, String noteId);
    }
}
