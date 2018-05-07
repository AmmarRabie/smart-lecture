package cmp.sem.team8.smarlecture.common.data.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import cmp.sem.team8.smarlecture.common.data.DataService;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.GroupMessagesEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.SessionEntry;
import cmp.sem.team8.smarlecture.common.data.firebase.FirebaseContract.UserEntry;
import cmp.sem.team8.smarlecture.common.data.model.GroupInvitationModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupMessageModel;
import cmp.sem.team8.smarlecture.common.data.model.GroupModel;
import cmp.sem.team8.smarlecture.common.data.model.InvitedUserModel;
import cmp.sem.team8.smarlecture.common.data.model.MemberModel;
import cmp.sem.team8.smarlecture.common.data.model.NoteModel;
import cmp.sem.team8.smarlecture.common.data.model.QuestionModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionForUserModel;
import cmp.sem.team8.smarlecture.common.data.model.SessionModel;
import cmp.sem.team8.smarlecture.common.data.model.UserModel;

/**
 * Created by AmmarRabie on 21/04/2018.
 */

class Serializer {
    private static final String TAG = "Serializer";

    static GroupModel group(DataSnapshot groupSnapshot) {
        String[] requiredChildes = GroupEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, groupSnapshot)) return null;

        String groupId = groupSnapshot.getKey();
        String name = groupSnapshot.child(GroupEntry.KEY_NAME).getValue(String.class);
        String ownerId = groupSnapshot.child(GroupEntry.KEY_OWNER_ID).getValue(String.class);

        return new GroupModel(name, groupId, ownerId);
    }

    static SessionModel session(DataSnapshot sessionSnapshot) {
        String[] requiredChildes = SessionEntry.requiredChildes;
        if (!checkRequiredChildes(requiredChildes, sessionSnapshot)) return null;

        String sessionId = sessionSnapshot.getKey();
        String name = sessionSnapshot.child(SessionEntry.KEY_FOR_SESSION_NAME_).getValue(String.class);
        String forGroupId = sessionSnapshot.child(SessionEntry.KEY_FOR_GROUP_ID).getValue(String.class);
        String status = sessionSnapshot.child(SessionEntry.KEY_SESSION_STATUS).getValue(String.class);
        String attendanceStatus = sessionSnapshot.child(SessionEntry.KEY_ATTENDANCE_STATUS).getValue(String.class);


        SessionModel sessionModel = new SessionModel(sessionId, forGroupId,
                DataService.AttendanceStatus.fromString(attendanceStatus),
                DataService.SessionStatus.fromString(status),
                name);
        if (sessionSnapshot.hasChild(SessionEntry.KEY_SECRET))
            sessionModel.setSecret(sessionSnapshot.child(SessionEntry.KEY_SECRET).getValue(String.class));

        return sessionModel;
    }

    static InvitedUserModel invitedUser(DataSnapshot invitedUserRoot, DataSnapshot userRoot) {
        if (!checkRequiredChildes((String[]) null, invitedUserRoot)) return null;
        UserModel userModel = user(userRoot);
        return new InvitedUserModel(userModel, ((boolean) invitedUserRoot.child(GroupEntry.KEY_NAMES_LIST_IS_MEMBER).getValue()));
    }

    static SessionForUserModel sessionForUser(DataSnapshot userRoot, DataSnapshot sessionRoot, DataSnapshot groupRoot) {
        GroupModel groupModel = group(groupRoot);
        SessionModel sessionModel = session(sessionRoot);
        UserModel userModel = user(userRoot);
        return new SessionForUserModel(sessionModel, groupModel, userModel);
    }

    static UserModel user(DataSnapshot userRoot) {
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
            result.add(invitedUser(invitedUserRoot));
        return result;
    }*/

    static GroupInvitationModel groupInvitation(DataSnapshot groupSnapshot, DataSnapshot userSnapshot) {
        GroupModel groupModel = group(groupSnapshot);
        UserModel userModel = user(userSnapshot);
        if (groupModel == null || userModel == null) {
            Log.e(TAG, "groupInvitation: error while serializing group or user");
            return null;
        }
        return new GroupInvitationModel(
                groupModel.getId()
                , userModel.getId()
                , groupModel.getName()
                , userModel.getName()
        );
    }

    static MemberModel attendee(DataSnapshot attendeeSnapshot, DataSnapshot userSnapshot) {
        UserModel userModel = user(userSnapshot);
        boolean isAttend = ((boolean) attendeeSnapshot.child(SessionEntry.KEY_ATTEND).getValue());
        if (!attendeeSnapshot.child(SessionEntry.KEY_NOTES).exists())
            return new MemberModel(userModel, isAttend);
        ArrayList<NoteModel> notes = new ArrayList<>();
        for (DataSnapshot oneNoteSnapshot : attendeeSnapshot.child(SessionEntry.KEY_NOTES).getChildren())
            notes.add(note(oneNoteSnapshot));
        return new MemberModel(userModel, isAttend, notes);
    }

    static NoteModel note(DataSnapshot noteSnapshot) {
        String noteId = noteSnapshot.getKey();
        String noteText = noteSnapshot.getValue(String.class);

        return new NoteModel(noteId, noteText);
    }

    static ArrayList<NoteModel> serializeNotes(DataSnapshot notesSnapshot) {
        ArrayList<NoteModel> notes = new ArrayList<>();
        for (DataSnapshot oneNoteSnapshot : notesSnapshot.getChildren())
            notes.add(note(oneNoteSnapshot));
        return notes;
    }

    static QuestionModel question(DataSnapshot questionSnapshot, DataSnapshot ownerSnapshot) {
        String id = questionSnapshot.getKey();
        String text = questionSnapshot.child(SessionEntry.KEY_QUESTION_TEXT).getValue(String.class);
        UserModel owner = user(ownerSnapshot);
        return new QuestionModel(id, owner, text);
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

    public static ArrayList<GroupMessageModel> groupMessages(DataSnapshot groupSnapshot, DataSnapshot messagesSnapshot) {
        final GroupModel groupModel = group(groupSnapshot);
        ArrayList<GroupMessageModel> result = new ArrayList<>();
        for (DataSnapshot messageSnapshot : messagesSnapshot.getChildren()) {
            String messageId = messageSnapshot.getKey();
            String title = messageSnapshot.child(GroupMessagesEntry.KEY_TITLE).getValue(String.class);
            String body = messageSnapshot.child(GroupMessagesEntry.KEY_BODY).getValue(String.class);
            result.add(new GroupMessageModel(messageId, groupModel, body, title));
        }
        return result;
    }
}