package cmp.sem.team8.smarlecture.common.data.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.AppDataSource;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

class FirebaseSerializer {
    private static final String TAG = "FirebaseSerializer";

    static GroupModel serializeGroup(DataSnapshot groupSnapshot) {
        String[] requiredChildes = GroupEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, groupSnapshot)) return null;

        String groupId = groupSnapshot.getKey();
        String name = groupSnapshot.child(GroupEntry.KEY_NAME).getValue(String.class);
        String ownerId = groupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class);

        return new GroupModel(name, groupId, ownerId);
    }

    static SessionModel serializeSession(DataSnapshot sessionSnapshot) {
        String[] requiredChildes = SessionEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, sessionSnapshot)) return null;

        String sessionId = sessionSnapshot.getKey();
        String name = sessionSnapshot.child(SessionEntry.KEY_FOR_SESSION_NAME_).getValue(String.class);
        String forGroupId = sessionSnapshot.child(SessionEntry.KEY_FOR_GROUP_ID).getValue(String.class);
        String status = sessionSnapshot.child(SessionEntry.KEY_SESSION_STATUS).getValue(String.class);
        String attendanceStatus = sessionSnapshot.child(SessionEntry.KEY_ATTENDANCE_STATUS).getValue(String.class);


        SessionModel sessionModel = new SessionModel(sessionId, forGroupId,
                AppDataSource.AttendanceStatus.fromString(attendanceStatus),
                AppDataSource.SessionStatus.fromString(status),
                name);
        if (sessionSnapshot.hasChild(SessionEntry.KEY_SECRET))
            sessionModel.setSecret(sessionSnapshot.child(SessionEntry.KEY_SECRET).getValue(String.class));

        return sessionModel;
    }

    static InvitedUserModel serializeInvitedUser(DataSnapshot invitedUserRoot, DataSnapshot userRoot) {
        if (!checkRequiredChildes((String[]) null, invitedUserRoot)) return null;
        UserModel userModel = serializeUser(userRoot);
        return new InvitedUserModel(userModel, ((boolean) invitedUserRoot.getValue()));
    }

    static SessionForUserModel serializeSessionForUser(DataSnapshot userRoot, DataSnapshot sessionRoot, DataSnapshot groupRoot) {
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

    static UserModel serializeUser(DataSnapshot userRoot) {
        String[] requiredChildes = UserEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, userRoot)) return null;

        String name = userRoot.child(UserEntry.KEY_NAME).getValue(String.class);
        String email = userRoot.child(UserEntry.KEY_EMAIL).getValue(String.class);
        return new UserModel(name, email, userRoot.getKey());
    }

/*    public static ArrayList<InvitedUserModel> serializeInvitedUserList(DataSnapshot listRoot) {
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
    }*/

    static GroupInvitationModel serializeGroupInvitation(DataSnapshot groupSnapshot, DataSnapshot userSnapshot) {
        GroupModel groupModel = serializeGroup(groupSnapshot);
        UserModel userModel = serializeUser(userSnapshot);
        if (groupModel == null || userModel == null) {
            Log.e(TAG, "serializeGroupInvitation: error while serializing group or user");
            return null;
        }
        return new GroupInvitationModel(
                groupModel.getId()
                , userModel.getId()
                , groupModel.getName()
                , userModel.getName()
        );
    }

    static MemberModel serializeAttendee(DataSnapshot attendeeSnapshot, DataSnapshot userSnapshot) {
        UserModel userModel = serializeUser(userSnapshot);
        boolean isAttend = ((boolean) attendeeSnapshot.child(SessionEntry.KEY_ATTEND).getValue());
        if (!attendeeSnapshot.child(SessionEntry.KEY_NOTES).exists())
            return new MemberModel(userModel, isAttend);
        ArrayList<NoteModel> notes = new ArrayList<>();
        for (DataSnapshot oneNoteSnapshot : attendeeSnapshot.child(SessionEntry.KEY_NOTES).getChildren())
            notes.add(serializeNote(oneNoteSnapshot));
        return new MemberModel(userModel, isAttend, notes);
    }

    static NoteModel serializeNote(DataSnapshot noteSnapshot) {
        String noteId = noteSnapshot.getKey();
        String noteText = noteSnapshot.getValue(String.class);

        return new NoteModel(noteId, noteText);
    }

    static ArrayList<NoteModel> serializeNotes(DataSnapshot notesSnapshot) {
        ArrayList<NoteModel> notes = new ArrayList<>();
        for (DataSnapshot oneNoteSnapshot : notesSnapshot.getChildren())
            notes.add(serializeNote(oneNoteSnapshot));
        return notes;
    }

    static ArrayList<String> getKeys(DataSnapshot dataSnapshot) {
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
