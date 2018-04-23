package cmp.sem.team8.smarlecture.common.data.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;
import cmp.sem.team8.smarlecture.common.data.model.AttendeeModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

public class FirebaseSerializer {
    private static final String TAG = "FirebaseSerializer";

    public static GroupModel serializeGroup(DataSnapshot groupSnapshot) {
        String[] requiredChildes = GroupEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, groupSnapshot)) return null;

        String groupId = groupSnapshot.getKey();
        String name = groupSnapshot.child(GroupEntry.KEY_NAME).getValue(String.class);
        String ownerId = groupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class);

        DataSnapshot sessionsSnapshot = groupSnapshot.child(GroupEntry.KEY_SESSIONS);
        ArrayList<String> sessionsIds = getKeys(sessionsSnapshot);

        DataSnapshot usersSnapshot = groupSnapshot.child(GroupEntry.KEY_NAMES_LIST);
        ArrayList<InvitedUserModel> usersList = serializeInvitedUserList(usersSnapshot);

        return new GroupModel(name, groupId, ownerId, usersList, sessionsIds);
    }

    public static SessionModel serializeSession(DataSnapshot sessionSnapshot) {
        String[] requiredChildes = SessionEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, sessionSnapshot)) return null;

        String sessionId = sessionSnapshot.getKey();
        String name = sessionSnapshot.child(SessionEntry.KEY_FOR_SESSION_NAME_).getValue(String.class);
        String forGroupId = sessionSnapshot.child(SessionEntry.KEY_FOR_GROUP_ID).getValue(String.class);
        String status = sessionSnapshot.child(SessionEntry.KEY_SESSION_STATUS).getValue(String.class);
        String attendanceStatus = sessionSnapshot.child(SessionEntry.KEY_ATTENDANCE_STATUS).getValue(String.class);


        SessionModel sessionModel = new SessionModel(sessionId, forGroupId,
                AppDataSource.AttendanceStatus.valueOf(attendanceStatus),
                AppDataSource.SessionStatus.valueOf(status),
                name);
        if (sessionSnapshot.hasChild(SessionEntry.KEY_SECRET))
            sessionModel.setSecret(sessionSnapshot.child(SessionEntry.KEY_SECRET).getValue(String.class));

        if (sessionSnapshot.hasChild(SessionEntry.KEY_NAMES_LIST))
            sessionModel.setAttendanceList(serializeAttendeeList(sessionSnapshot.child(SessionEntry.KEY_NAMES_LIST)));

        return sessionModel;
    }

    public static AttendeeModel serializeAttendee(DataSnapshot attendeeRoot) {
        if (!checkRequiredChildes((String[]) null, attendeeRoot)) return null;

        return new AttendeeModel(attendeeRoot.getKey(), ((boolean) attendeeRoot.getValue()));
    }

    public static InvitedUserModel serializeInvitedUser(DataSnapshot invitedUserRoot) {
        if (!checkRequiredChildes((String[]) null, invitedUserRoot)) return null;

        return new InvitedUserModel(invitedUserRoot.getKey(), ((boolean) invitedUserRoot.getValue()));
    }

    public static SessionForUserModel serializeSessionForUser(DataSnapshot userRoot, DataSnapshot sessionRoot, DataSnapshot groupRoot) {
        GroupModel groupModel = serializeGroup(groupRoot);
        SessionModel sessionModel = serializeSession(sessionRoot);
        UserModel userModel = serializeUser(userRoot);
        return new SessionForUserModel(
                sessionModel.getId()
                , sessionModel.getSessionStatus()
                , sessionModel.getAttendanceStatus()
                , sessionModel.getName()
                , groupModel.getId()
                , groupModel.getName()
                , userModel.getId()
                , userModel.getName());
    }


    public static UserModel serializeUser(DataSnapshot userRoot) {
        String[] requiredChildes = UserEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, userRoot)) return null;

        String name = userRoot.child(UserEntry.KEY_NAME).getValue(String.class);
        String email = userRoot.child(UserEntry.KEY_EMAIL).getValue(String.class);
        ArrayList<String> groupsInvitations = getKeys(userRoot.child(UserEntry.KEY_INVITATIONS));
        return new UserModel(userRoot.getKey(), email, userRoot.getKey(), groupsInvitations);
    }


    public static ArrayList<AttendeeModel> serializeAttendeeList(DataSnapshot listRoot) {
        if (listRoot == null || !listRoot.exists()) {
            Log.w(TAG, "serializeAttendeeList: listRoot does not exists or null");
            return null;
        }
        if (listRoot.getChildrenCount() == 0) {
            Log.w(TAG, "serializeAttendeeList: listRoot does not have children");
            return null;
        }
        ArrayList<AttendeeModel> result = new ArrayList<>();
        for (DataSnapshot attendeeRoot : listRoot.getChildren())
            result.add(serializeAttendee(attendeeRoot));
        return result;
    }

    public static ArrayList<InvitedUserModel> serializeInvitedUserList(DataSnapshot listRoot) {
        if (listRoot == null || !listRoot.exists()) {
            Log.w(TAG, "serializeInvitedUserList: listRoot does not exists or null");
            return null;
        }
        if (listRoot.getChildrenCount() == 0) {
            Log.w(TAG, "serializeInvitedUserList: listRoot does not have children");
            return null;
        }
        ArrayList<InvitedUserModel> result = new ArrayList<>();
        for (DataSnapshot invitedUserRoot : listRoot.getChildren())
            result.add(serializeInvitedUser(invitedUserRoot));
        return result;
    }


    private static ArrayList<String> getKeys(DataSnapshot dataSnapshot) {
        ArrayList<String> keys = new ArrayList<String>();
        for (DataSnapshot child : dataSnapshot.getChildren())
            keys.add(child.getKey());
        return keys;
    }

    private static boolean checkRequiredChildes(ArrayList<String> requiredChildes, DataSnapshot dataSnapshot) {
        return checkRequiredChildes((String[]) requiredChildes.toArray(), dataSnapshot);
    }

    private static boolean checkRequiredChildes(String[] requiredChildes, DataSnapshot dataSnapshot) {
        if (dataSnapshot == null || !dataSnapshot.exists()) {
            Log.w(TAG, "checkRequiredChildes: dataSnapshot is null or not exists");
            return false;
        }
        if (requiredChildes == null || requiredChildes.length == 0) return true;
        for (String key : requiredChildes)
            if (!dataSnapshot.hasChild(key)) {
                Log.w(TAG, "checkRequiredChildes: for " +
                        "dataSnapShot-> " + dataSnapshot + " doesn't have required child-> " + key);
                return false;
            }
        return true;
    }
}
