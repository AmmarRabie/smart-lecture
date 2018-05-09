package cmp.sem.team8.smarlecture.session.create.members;


import android.util.Log;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;

/**
 * Created by ramym on 3/17/2018.
 */

public class MembersPresenter implements MembersContract.Actions {

    private static final String TAG = "BeginAttendancePresente";

    private final String SESSION_ID;
    private ArrayList<MemberModel> members;
    private DataService.Listen membersListener;

    private MembersContract.Views mView;
    private DataService mDataSource;

    private boolean isAttendanceWork = false;
    private DataService.SessionStatus currSessionStatus;
    private DataService.AttendanceStatus currAttendanceStatus;

    public MembersPresenter(DataService dataSource, MembersContract.Views view, String sessionId) {
        mDataSource = dataSource;
        mView = view;
        SESSION_ID = sessionId;
        members = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mDataSource.getSessionById(SESSION_ID, new DataService.Get<SessionModel>() {
            @Override
            public void onDataFetched(SessionModel session) {
                // set a random secret if this session have no secret
                String secret = session.getSecret();
                if (secret == null) {
                    secret = generateRandomSecret();
                    mDataSource.setSessionSecret(SESSION_ID, secret, null);
                }
                DataService.AttendanceStatus attendanceStatus = session.getAttendanceStatus();
                DataService.SessionStatus sessionStatus = session.getSessionStatus();
                currSessionStatus = sessionStatus;
                currAttendanceStatus = attendanceStatus;
                mView.showSessionStatus(sessionStatus);
                isAttendanceWork = false;
                if (sessionStatus.equals(DataService.SessionStatus.OPEN)) {
                    mView.showSecret(secret);
                    switch (attendanceStatus) {
                        case CLOSED:
                            mView.hideBeginAttendanceButton(false);
                            break;
                        case NOT_ACTIVATED:
                            mView.showBeginAttendanceButton();
                            break;
                        case OPEN: // only happen when start is calling again to refresh the view, the timer thread is working with it smoothly
                            isAttendanceWork = true;
                            mView.hideBeginAttendanceButton(false);
                            break;
                    }
                    return;
                }
                // the session is closed, show only the members
                mView.hideBeginAttendanceButton(false);
                if (attendanceStatus.equals(DataService.AttendanceStatus.OPEN))
                    isAttendanceWork = true;
            }
        });
        if (membersListener != null) {
            mDataSource.forget(membersListener);
            membersListener = null;
            mView.clearMembersList();
            members.clear();
        }

        membersListener = mDataSource.ListenSessionMembers(SESSION_ID, new DataService.Listen<MemberModel>() {
            @Override
            public void onDataReceived(MemberModel changedMember) {
                Log.w(TAG, "onDataReceived() called with: changedMember = [" + changedMember + "]");
                MemberModel existingMember = getMember(changedMember.getId());
                if (existingMember == null) {
                    // new member
                    mView.addNewMember(changedMember);
                    members.add(changedMember);
                    return;
                }

                // don't add the below line because this function is also triggered if the note is deleted
                // if you update the notes here in onDataReceived the onDelete method will not find the deleted note to update it and remove the list
                // [TODO]: uncomment the below line and make necessary work considering the comment above
                // existingMember.setNotes(changedMember.getNotes());

                existingMember.setAttend(changedMember.isAttend());
                mView.updateMemberAttendance(changedMember.getId(), changedMember.isAttend());
            }
        });
    }

    @Override
    public void BeginAttendance() {
        if (isAttendanceWork) {
            mView.showErrorMessage("Already in progress");
            return;
        }
        mView.startAttendanceTimer(3);
        isAttendanceWork = true;
        currAttendanceStatus = DataService.AttendanceStatus.OPEN;

        mDataSource.setAttendanceStatus(SESSION_ID, DataService.AttendanceStatus.OPEN, null);
        mDataSource.setSessionSecret(SESSION_ID, mView.getSecret(), null);
    }

    @Override
    public void onAttendanceTimerEnd() {
        mDataSource.setAttendanceStatus(SESSION_ID, DataService.AttendanceStatus.CLOSED, null);
        isAttendanceWork = false;
        currAttendanceStatus = DataService.AttendanceStatus.CLOSED;
    }

    @Override
    public void onAttendanceMarkChanged(String memberId, boolean isAttend) {
        mDataSource.setMemberAttendance(SESSION_ID, memberId, isAttend, null);
        // listening will auto update the list
    }

    @Override
    public void addNote(final String memberId, String noteText) {
        Log.w(TAG, "addNote() called with: memberId = [" + memberId + "], noteText = [" + noteText + "]");
        if (noteText == null || noteText.isEmpty()) {
            mView.showErrorMessage("Note is empty");
            return;
        }
        mDataSource.addNote(SESSION_ID, memberId, noteText, new DataService.Insert<NoteModel>() {
            @Override
            public void onDataInserted(NoteModel newNote) {
                Log.w(TAG, "onDataInserted() called with: newNote = [" + newNote + "]");
                mView.onNoteAddedSuccess(memberId, newNote);
                // listener for attendance list will update our list automatically
//                getMember(memberId).getNotes().add(newNote);
            }
        });
    }

    @Override
    public void deleteNote(final String memberId, final String noteId) {
        Log.w(TAG, "deleteNote() called with: memberId = [" + memberId + "], noteId = [" + noteId + "]");
        mDataSource.deleteNote(SESSION_ID, memberId, noteId, new DataService.Delete() {
            @Override
            public void onDeleted() {
                Log.w(TAG, "onDeleted() called");
                MemberModel member = getMember(memberId);
                if (member == null) {
                    Log.e(TAG, "onDeleted: can't find a member with id " + memberId
                            + ", members list in now: " + members.toString());
                    return;
                }
                NoteModel note = getNote(member, noteId);
                if (note == null) {
                    Log.e(TAG, "onDeleted: can't find a note with id = " + noteId + " to the member " + member.getName());
                    return;
                }
                mView.onDeleteNoteSuccess(memberId, note);
                member.getNotes().remove(note);
            }

            @Override
            public void onError(String cause) {
                mView.showErrorMessage(cause);
            }
        });
    }

    /**
     * Helper method to get the MemberModel from its id from the list
     *
     * @param memberId member id caller search for
     * @return it returns {@link MemberModel} that have this id from the {@link #members} list if
     * it find it, null if it there is no model match this id
     */
    private MemberModel getMember(String memberId) {
        Log.w(TAG, "getMember() called with: memberId = [" + memberId + "]");
        for (int i = 0; i < members.size(); i++)
            if (members.get(i).getId().equals(memberId))
                return members.get(i);
        return null;
    }

    /**
     * Helper method to get the note model from the member model and note id, it just a shortcut
     * for looping over list to find the note
     *
     * @param member member this note is related to
     * @param noteId the id of the note caller search for
     * @return return the model if the id exists in the member notes list, null otherwise.
     */
    private NoteModel getNote(MemberModel member, String noteId) {
        Log.w(TAG, "getNote() called with: member = [" + member + "], noteId = [" + noteId + "]");
        for (int i = 0; i < member.getNotes().size(); i++)
            if (member.getNotes().get(i).getId().equals(noteId))
                return member.getNotes().get(i);
        return null;
    }

    private String generateRandomSecret() {
        Integer r1 = (int) (Math.random() * 10); // r1 if from 0->9
        Integer r2 = (int) (Math.random() * 10); // r2 if from 0->9
        Integer r3 = (int) (Math.random() * 10); // r3 if from 0->9
        Integer r4 = (int) (Math.random() * 10); // r4 if from 0->9
        return r1.toString() + r2.toString() + r3.toString() + r4.toString();
    }

    @Override
    public void onDestroy() {
        mDataSource.forget(membersListener);
        membersListener = null;
        if (isAttendanceWork) {
            mDataSource.setAttendanceStatus(SESSION_ID, DataService.AttendanceStatus.CLOSED, null);
            isAttendanceWork = false;
        }
    }

    @Override
    public void updateView(MembersContract.Views views) {
        mView = views;
        mView.setPresenter(this);
    }

    @Override
    public void setSessionStatus(final DataService.SessionStatus newStatus) {
        if (newStatus.equals(currSessionStatus))
            return;
        if (isAttendanceWork && !newStatus.equals(DataService.SessionStatus.OPEN)) {
            mView.showErrorMessage("Wait till attendance timer end");
            return;
        }
        if (currSessionStatus.equals(DataService.SessionStatus.NOT_ACTIVATED) &&  !newStatus.equals(DataService.SessionStatus.OPEN)){
            mView.showErrorMessage("The not activated session should be opened first");
            return;
        }
        if (currSessionStatus.equals(DataService.SessionStatus.OPEN) &&  !newStatus.equals(DataService.SessionStatus.CLOSED)){
            mView.showErrorMessage("opened session can only be closed");
            return;
        }
        if (currSessionStatus.equals(DataService.SessionStatus.CLOSED)){
            mView.showErrorMessage("You can't change the closed session status");
            return;
        }
        mDataSource.setSessionStatus(SESSION_ID, newStatus, new DataService.Insert<Void>() {
            @Override
            public void onDataInserted(Void feedback) {
                mView.showSessionStatus(newStatus);
                start();
            }
        });
    }

    @Override
    public DataService.AttendanceStatus getAttendanceStatus() {
        return currAttendanceStatus;
    }
}
