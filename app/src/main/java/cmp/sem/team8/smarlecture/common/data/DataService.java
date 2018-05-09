package cmp.sem.team8.smarlecture.common.data;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.model.FileModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupMessageModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupStatisticsModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
import cmp.sem.team8.smarlecture.common.data.model.ObjectiveModel;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserGradeModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Main entry point for accessing app data.
 * <p>
 * This interface defines the interaction with our data and data models.
 * For example this interface can be implemented with firebase model (in our case) or later
 * implemented with stored procedures. This interface is like stored procedures to our data.
 * <p>
 * Methods parameters in this class follow that they receive required data to do the operation,
 * then response with one of five callbacks (
 * {@link Insert}, <p>
 * {@link Update}, <p>
 * {@link Delete}, <p>
 * {@link Get}, <p>
 * {@link Listen})
 */
public interface DataService {
    /**
     * Get the user by its id
     *
     * @param userId   the id of the user caller want to get
     * @param callback Send the data to the caller or tell him with errors
     */
    void getUser(String userId, Get<UserModel> callback);

    /**
     * @param Grade
     * @param userId
     * @param callback
     */
    void getUserGrade(int Grade, String userId, Get<UserGradeModel> callback);

    /**
     * Insert new user into the data source. Note that this method doesn't authenticate the user
     * Caller should handle authentication outside this method
     *
     * @param userModel user to insert him in the data source
     * @param callback  Send the feedback to the caller or tell him with errors
     */
    void insertUser(UserModel userModel, Insert<Void> callback);

    void updateGroupGrades(String groupId, ArrayList<String> ids, ArrayList<Integer> grade, final Update callback);

    /**
     * Change the user name of the user already exists in the system
     *
     * @param userId   the id of the user
     * @param newName  new name to update it in the data source
     * @param callback notify success or tell him with errors
     */
    void updateUserName(String userId, String newName, Update callback);

    /**
     * Change the profile image of the user already exists in the system
     *
     * @param userId        the id of the user
     * @param newImageBytes new image in bytes to update it in the data source
     * @param callback      notify success or tell him with errors
     */
    void updateUserProfileImage(String userId, byte[] newImageBytes, Update callback);

    /**
     * return all sessions that passed user is a member in their groups filtered by flags <B><I>one by one</B>
     *
     * @param userId        the user id you want to get sessions he is a member in
     * @param callback      return the data on this callback
     * @param withClosed    if false, the sessions returned will exclude closed ones
     * @param withOpened    if false, the sessions returned will exclude opened ones
     * @param withNotActive if false, the sessions returned will exclude not active ones
     */
    void getSessionsForUser(String userId, Get<SessionForUserModel> callback
            , boolean withClosed
            , boolean withOpened
            , boolean withNotActive);

    /**
     * return all sessions that passed user is a member in their groups <B><I>one by one</B>
     *
     * @param userId   the user id you want to get sessions he is a member in
     * @param callback return the data on this callback
     */
    void getSessionsForUser(String userId, Get<SessionForUserModel> callback); // for student

    /**
     * return all group invitations sent userId, doesn't return the groups he is actually in.
     *
     * @param userId   the user id you want to get invited groups.
     * @param callback return the data on this callback.
     */
    void getGroupInvitationsForUser(String userId, Get<GroupInvitationModel> callback);

    void getGroupAndItsSessionNameList(String groupId, Get<GroupStatisticsModel> callback);

    void getGroupGrade(String groupId, Get<ArrayList<UserGradeModel>> callback);

    /**
     * Invite a new user to the passed group by user email. Note that this function only invite
     * the user and don't make him a member.
     *
     * @param email    the email of invited user
     * @param groupId  identify the group caller want to add this user to
     * @param callback feedback return the user that is just invited or tell the error
     * @see #acceptFollowingGroup(String, String, Update) {@link #refuseFollowingGroup(String, String, Update)}
     */
    void inviteUserToGroup(String email, String groupId, Insert<UserModel> callback);

    /**
     * Accept the user into the passed group. after this method success the passed user becomes a
     * group member of the passed group. this function should put the new user into not activated
     * sessions of the group
     *
     * @param userId   user that accept the invitation
     * @param groupId  group that has been accepted
     * @param callback notify success or tell him with errors
     */
    void acceptFollowingGroup(String userId, String groupId, Update callback);

    /**
     * delete the user from the passed group. after this method success the passed user becomes no
     * more in this group (not invited - not a member).
     *
     * @param userId   user that refuse the invitation
     * @param groupId  group that has been refused
     * @param callback notify success or tell him with errors
     */
    void refuseFollowingGroup(String userId, String groupId, Update callback);

    /**
     * get session name and group owner for the session id to be viewed for the student when he joins a session
     *
     * @param sessionID the session to get its data
     * @param groupID   group id this session is related to
     * @param callback  return the data on this callback or tell the error
     */
    void getJoinedSessionInfo(String sessionID, String groupID, Get<SessionForUserModel> callback);


    /**
     * listen the changes of the session status on tha passed session
     *
     * @param sessionID the id of the session that will be listen to
     * @param callback  return that status every time it changes till it be forgotten
     * @return the callback passed to the function to easy {@link #forget(Listen)} it in the
     * proper time
     */
    Listen listenSessionStatus(String sessionID, Listen<SessionStatus> callback);


    /**
     * get the objectives of a session
     *
     * @param sessionID session id to get its objectives
     * @param callback  return the data on this callback
     */
    void getObjectives(String sessionID, Get<ArrayList<ObjectiveModel>> callback);

    //

    /**
     * update new average rating of objective
     *
     * @param sessionID           session id this objective related to
     * @param objectiveID         identify the id of this objective
     * @param newObjectiveRating  new objective rating to update it in the data source
     * @param newNumberUsersRated new count of the users rating this objective
     * @param callback            notify success or tell him with errors
     */
    void updateObjectivesRating(String sessionID, String objectiveID, Float newObjectiveRating, Integer newNumberUsersRated, Update callback);

    /**
     * Insert new objective in passed session
     *
     * @param sessionID      identify the session to insert this objective in
     * @param addedObjective description of the objective
     * @param isOffline      whether or not the device in in offline mode
     * @param callback       return the inserted objective or tell the error
     */
    void insertObjective(String sessionID, String addedObjective, boolean isOffline, Insert<ObjectiveModel> callback);

    /**
     * get the group members and invited members in the passed group
     *
     * @param groupId  identify the group to get the members of it
     * @param callback return the data on this callback or tell the error
     */
    void getGroupMembers(String groupId, Get<ArrayList<InvitedUserModel>> callback);

    /**
     * get session status of passed session
     *
     * @param sessionId identify the session
     * @param callback  return the data on this callback or tell the error
     * @see #listenSessionStatus(String, Listen)
     */
    void getSessionStatus(String sessionId, Get<SessionStatus> callback);

    /**
     * change or insert the passed status to passed session
     *
     * @param sessionId identify the session
     * @param status    new status to be put to the session status
     * @param callback  notify success or tell errors
     */
    void setSessionStatus(String sessionId, SessionStatus status, Insert<Void> callback);

    /**
     * change or insert the passed status to attendance status in passed session
     *
     * @param sessionId identify the session
     * @param status    new status to be put to the attendance status of the session
     * @param callback  notify success or tell errors
     */
    void setAttendanceStatus(String sessionId, AttendanceStatus status, Update callback);

    /**
     * change or insert the passed secret to passed session
     *
     * @param sessionId identify the session
     * @param secret    new secret to update the session with
     * @param callback  notify success or tell errors
     */
    void setSessionSecret(String sessionId, String secret, Update callback);

    /**
     * listen the changes of the members of passed session and return the data <B>One by one</B>
     *
     * @param sessionId the id of the session that will be listen to its members
     * @param callback  return any changed member every time it changes till it be forgotten
     * @return the callback passed to the function to easy {@link #forget(Listen)} it in the
     * proper time
     */
    Listen ListenSessionMembers(String sessionId, Listen<MemberModel> callback);

    /**
     * update the attendance of a session member
     *
     * @param sessionId identify the session
     * @param memberId  identify the user in this session to update his attendance
     * @param isAttend  if true, the passed member will be attend in the passed session
     * @param callback  notify success or tell errors
     */
    void setMemberAttendance(String sessionId, String memberId, boolean isAttend, Update callback);

    /**
     * Add a new note to a member in a given session
     *
     * @param sessionId identify the session that this member is in
     * @param memberId  identify the member this note will go to him
     * @param noteText  the note text
     * @param callback  return the inserted note back to the caller or tell him with errors
     */
    void addNote(String sessionId, String memberId, String noteText, Insert<NoteModel> callback);

    /**
     * delete a note of session member
     *
     * @param sessionId identify the session this member is in
     * @param memberId  identify the member that the note will be removed from
     * @param noteId    identify the note to be removed from member token notes
     * @param callback  whether or not the deletion is completed successfully
     */
    void deleteNote(String sessionId, String memberId, String noteId, Delete callback);

    /**
     * Update objective description
     *
     * @param objectiveID          identify the objective to be updated
     * @param sessionID            identify the session
     * @param objectiveDescription new description of the objective
     * @param isOffline            whether or not the device is in offline mode
     * @param callback             notify success or tell him with errors
     */
    void editObjective(String objectiveID, String sessionID, String objectiveDescription, boolean isOffline, Update callback);

    /**
     * delete a note of session member
     *
     * @param sesisonID   identify the session this objective is related to
     * @param objectiveID identify the objective to be removed from session objectives
     * @param callback    whether or not the deletion is completed successfully
     */
    void deleteObjective(String objectiveID, String sesisonID, boolean isOffline, Delete callback);

    /**
     * get all sessions of the passed group in session model representation
     *
     * @param groupId  identify the group to get its sessions
     * @param callback return the sessions of the group or tell the errors
     */
    void getGroupSessions(String groupId, Get<ArrayList<SessionModel>> callback);

    /**
     * insert new session into passed group
     *
     * @param groupId     identify the group to insert this session to it
     * @param sessionId   the new session id, this is passed to the function as it is randomly generated
     *                    depending on system business logic
     * @param sessionName the name of the session
     * @param callback    whether or not the insert is completed successfully
     */
    void addSession(String groupId, String sessionId, String sessionName, Insert<Void> callback);

    /**
     * Edit the name of the session
     *
     * @param sessionId   identify the session
     * @param sessionName the new name of the session
     * @param isOffline   whether or not the device is in offline mode
     * @param callback    notify success or tell him with errors
     */
    void editSession(String sessionId, String sessionName, boolean isOffline, Update callback);

    /**
     * delete a session by its id
     *
     * @param sessoinId identify the session
     * @param isOffline whether or not the device is in offline mode
     * @param callback  whether or not the deletion is completed successfully
     */
    void deleteSession(String sessoinId, boolean isOffline, Delete callback);

    /**
     * delete a group by its id
     *
     * @param groupId   identify the group to be deleted
     * @param isOffline whether or not the device is in offline mode
     * @param callback  whether or not the deletion is completed successfully
     */
    void deleteGroupById(String groupId, boolean isOffline, Delete callback);

    /**
     * insert new group in the data source
     *
     * @param userId    the id of the owner
     * @param groupName the group name
     * @param isOffline whether or not the device is in offline mode
     * @param callback  return the id of the inserted group or tell the errors
     */
    void addGroup(String userId, String groupName, boolean isOffline, Insert<String> callback);

    /**
     * Change the name of the group
     *
     * @param groupId   identify the group to change its name
     * @param groupName new name of the group
     * @param isOffline whether or not the device is in offline mode
     * @param callback  notify success or tell him with errors
     */
    void updateGroup(String groupId, String groupName, boolean isOffline, Update callback);

    /**
     * Get the group id given a session id in it
     *
     * @param sessionId the session id caller want to find its group id
     * @param callback  return the group id or tell the errors
     */
    void getGroupId(String sessionId, Get<String> callback);

    /**
     * get the group of the passed user (All groups this user is the owner of them)
     *
     * @param userId   the user id to get all its created groups
     * @param callback return the list of the groups or tell the errors
     */
    void getGroupsForUser(String userId, Get<ArrayList<GroupModel>> callback);

    /**
     * get a representation of group data to export it in any formats
     *
     * @param groupId  identify the group to get its data
     * @param callback return the group data or tell the errors
     */
    void getGroupInfoForExport(String groupId, Get<FileModel> callback);

    /**
     * get the session data by the session id
     *
     * @param sessionId identify the session to be retrieved
     * @param callback  return the data of the session or tell errors
     */
    void getSessionById(String sessionId, Get<SessionModel> callback);

    /**
     * listen the changes of the attendance status of tha passed session
     *
     * @param sessionId the id of the session that will be listen to its attendance status
     * @param callback  return that status every time it changes till it be forgotten
     * @return the callback passed to the function to easy {@link #forget(Listen)} it in the
     * proper time
     */
    Listen listenAttendanceStatus(String sessionId, Listen<AttendanceStatus> callback);

    /**
     * get a group messages
     *
     * @param groupId  identify the group to get its messages
     * @param callback return the messages of this group or tell the errors
     */
    void getGroupMessages(String groupId, Get<ArrayList<GroupMessageModel>> callback);

    /**
     * listen the changes of the questions of passed session and return the data <B>One by one</B>
     *
     * @param sessionId the id of the session that will be listen to its questions
     * @param callback  return any changed question every time it changes till it be forgotten
     * @return the callback passed to the function to easy {@link #forget(Listen)} it in the
     * proper time
     */
    Listen ListenSessionQuestions(String sessionId, Listen<QuestionModel> callback);

    /**
     * Insert new question into session
     *
     * @param sessionId identify the session that question will be inserted to
     * @param askerId   the owner of the question (asker)
     * @param text      the question text
     * @param callback  return the question that has just inserted or tell the errors
     */
    void addQuestionToSession(String sessionId, String askerId, String text, Insert<QuestionModel> callback);

    /**
     * send group message to all group members of the passed group
     *
     * @param groupId  identify the group the message will be sent to its members
     * @param message  the message text that will be sent
     * @param callback whether the insertion is success or not
     */
    void sendGroupNotification(String groupId, String message, Insert<Void> callback);

    /**
     * Remove notifying callbacks with changing. After this function is called by a given listener
     * this listener will not receive {@link Listen#onDataReceived}yhhhhh even data changed
     *
     * @param listener the listener passed to any listen function from {@link DataService}
     */
    void forget(Listen listener);

    /**
     * The attendance status between the data service and clients of it
     */
    enum AttendanceStatus {
        NOT_ACTIVATED("not-activated"), OPEN("open"), CLOSED("closed");

        private String value;

        AttendanceStatus(String val) {
            value = val;
        }

        public static AttendanceStatus fromString(String value) {
            for (AttendanceStatus status : AttendanceStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The session status between the data source and clients of it
     */
    enum SessionStatus {
        NOT_ACTIVATED("not-activated"), OPEN("open"), CLOSED("closed");

        String value;

        SessionStatus(String val) {
            value = val;
        }


        public static SessionStatus fromString(String value) {
            for (SessionStatus status : SessionStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * define the general callback of listening for a value changed
     * you should properly listen and forget the listening with {@link #forget(Listen)}
     *
     * @param <D> type of the data returned on data change.
     * @see #forget(Listen)
     */
    abstract class Listen<D> {
        private final int mListenFromChange;
        private final int mListenToChange;
        private int count = 0;

        /**
         * data changing meaning
         *
         * @param listenFromChange count of data change to call onDataReceived for the first time,
         *                         default value for this is 0 meaning that it will call
         *                         onDataChanged on first time it fetches the data and
         *                         after first change, ...etc depending on the
         *                         listenToChange value and calling to forget.
         * @param listenToChange   count of data changes to call onDataReceived for the last time,
         *                         default value for this is -1 meaning that it listen forever till
         *                         {@link #forget(Listen)} is called of this listener. passing 2 while
         *                         listenFromChange is 1 meaning listen for first change and second
         *                         change only (note: without calling for first time data fetched).
         */
        public Listen(int listenFromChange, int listenToChange) {
            mListenFromChange = listenFromChange;
            mListenToChange = listenToChange;
        }

        /**
         * instantiate a new listener with default listening value
         * from first fetching the data till the listener forgotten
         */
        public Listen() {
            this(0, -1);
        }

        public final int getListenFromChange() {
            return mListenFromChange;
        }

        public final int getListenToChange() {
            return mListenToChange;
        }

        public abstract void onDataReceived(D dataSnapshot);

        /**
         * should be called each time callback handler call onDataReceived
         */
        public final void increment() {
            count++;
        }

        public final int getCount() {
            return count;
        }

        public final boolean shouldListen() {
            if (getListenToChange() == -1)
                return count >= getListenFromChange();
            return count >= getListenFromChange() && count <= getListenToChange();
        }

    }

    /**
     * define the general callback of insertion operation
     *
     * @param <fb> type of data returned on feedback (after data is inserted)
     */
    abstract class Insert<fb> {
        public abstract void onDataInserted(fb feedback);

        public void onError(String cause) {
        }
    }

    /**
     * define the general callback of deletion operation
     */
    abstract class Delete {
        public abstract void onDeleted();

        public void onError(String cause) {
        }

    }

    /**
     * define the general callback of deletion operation
     *
     * @param <D> type of data returned after fetching success
     */
    abstract class Get<D> {
        public abstract void onDataFetched(D data);

        public void onDataNotAvailable() {
        }

        public void onError(String cause) {
        }
    }

    /**
     * define the general callback of update operation
     */
    abstract class Update {
        public abstract void onUpdateSuccess();

        public void onDataNotAvailable() {
        }

        public void onError(String cause) {
        }
    }
}
