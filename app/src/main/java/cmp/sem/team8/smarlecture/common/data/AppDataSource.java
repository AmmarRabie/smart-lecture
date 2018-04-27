package cmp.sem.team8.smarlecture.common.data;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
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
public interface AppDataSource {

    void getUser(String userId, Get<UserModel> callback);

    void insertUser(UserModel userModel, Insert<Void> callback);

    void updateUserName(String userId, String newName, Update callback);

    void listenUser(String userId, Listen<UserModel> callback);



    /**
     * return all sessions that passed user is a member in their groups filtered by flags
     *
     * @param userId        the user id you want to get sessions he is a member in
     * @param callback      return the data on this callback
     * @param withClosed    if false, the sessions returned will exclude closed ones
     * @param withOpened    if false, the sessions returned will exclude opened ones
     * @param withNotActive if false, the sessions returned will exclude not active ones
     */
/*    void getSessionsForUser(String userId, Get<ArrayList<SessionForUserModel>> callback
            , boolean withClosed
            , boolean withOpened
            , boolean withNotActive);*/


    /**
     * return all sessions that passed user is a member in their groups
     *
     * @param userId   the user id you want to get sessions he is a member in
     * @param callback return the data on this callback
     */
//    void getSessionsForUser(String userId, Get<ArrayList<SessionForUserModel>> callback); // for student


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
     * @param userId the user id you want to get invited groups.
     * @param callback return the data on this callback.
     */
    void getGroupInvitationsForUser(String userId, Get<GroupInvitationModel> callback);

    void getSessionsOfGroup(String groupId, Get<ArrayList<SessionModel>> callback); // for lecturer

    void inviteUserToGroup(String email, String groupId, Insert<UserModel> callback); // add a new student

    void acceptFollowingGroup(String userId, String groupId, Update callback);

    void refuseFollowingGroup(String userId, String groupId, Update callback);

    void getUsersListOfGroup(String groupId, Get<ArrayList<UserModel>> callback);

    //get session name and group owner for the session id to be viewed for the student when he joins a session
    void getJoinedSessionInfo(String sessionID,String groupID,Get<SessionForUserModel> callback);

    void listenForsessionStatus(String sessionID,Listen<String> callback);

/*    //
    void getGroupById(String groupId, Get<GroupModel> callback);

    void getGroupSessions(String groupId, Get<ArrayList<SessionModel>> callback);

    void deleteGroupById(String groupId, Delete callback);

    void updateGroup(GroupModel updatingValues, Update callback);

    void updateGroupById(String groupId, String newGroupName, Update callback);


    //
    void deleteNamesList(String groupId, Delete callback); // not used

    void deleteNameOfNamesList(String groupId, String nameId, Delete callback);

    void updateNameOfNamesList(String groupId, String nameId, String newName, Update callback);

    void insertNameInNamesList(String groupId, String userId, Insert<String> callback);


    void getSessionById(String sessionId, Get<SessionModel> callback);

    void getSessionStatus(String sessionId, Get<SessionStatus> callback);

    void getAttendanceStatus(String sessionId, Get<AttendanceStatus> callback);


    //
    void updateSession(SessionModel sessionModel, Update callback);

    void updateSessionStatus(String sessionId, SessionStatus status, Update callback);

    void updateAttendanceStatus(String sessionId, AttendanceStatus status, Update callback);

    void updateSessionSecret(String sessionId, String secret, Update callback);*/

    /**
     * @param listener
     */
    void forget(Listen listener);

    /**
     * The attendance status between the data source and clients of it
     */
    enum AttendanceStatus {
        NOT_ACTIVATED("not-activated"), OPEN("open"), CLOSED("closed");

        private String value;

        AttendanceStatus(String val) {
            value = val;
        }


        @Override
        public String toString() {
            return value;
        }
        public static AttendanceStatus fromString(String value) {
            for (AttendanceStatus status : AttendanceStatus.values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            return null;
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
