package cmp.sem.team8.smarlecture.common.data.firebase;

/**
 * Created by AmmarRabie on 14/04/2018.
 */

final class FirebaseContract {
    public static final String KEY_THIS = "smart-lecture-cmp";

    private FirebaseContract() {
    }

    static final class GroupEntry {

        static final String KEY_THIS = "group";

        static final String KEY_OWNER_ID = "group_owner";
        static final String KEY_NAME = "name";
        static final String KEY_NAMES_LIST = "names_list";
        static final String KEY_NAMES_LIST_IS_MEMBER = "invite";
        static final String KEY_NAMES_LIST_GRADE = "grade";
        static final String KEY_SESSIONS = "sessions";
        static String[] keySet;
        static String[] requiredChildes;

        static {
            keySet = new String[]{KEY_OWNER_ID,
                    KEY_NAME,
                    KEY_NAMES_LIST,
                    KEY_SESSIONS, KEY_NAMES_LIST_GRADE, KEY_NAMES_LIST_IS_MEMBER};
        }

        static {
            requiredChildes = new String[]{KEY_OWNER_ID,
                    KEY_NAME,};
        }

        private GroupEntry() {
        }

    }


    static final class SessionEntry {
        static final String KEY_THIS = "session";
        static final String KEY_ATTENDANCE_STATUS = "attendance_status";
        static final String KEY_SECRET = "attendance_secret";
        static final String KEY_NAMES_LIST = "names_list";
        static final String KEY_SESSION_STATUS = "session_status";
        static final String KEY_FOR_GROUP_ID = "for_group";
        static final String KEY_FOR_SESSION_NAME_ = "for_name";
        static final String KEY_FOR_OBJECTIVES_LIST = "objectives";
        static final String KEY_ATTEND = "attend";
        static final String KEY_NOTES = "notes";
        static final String KEY_QUESTIONS = "questions";
        static final String KEY_QUESTION_OWNER = "owner_id";
        static final String KEY_QUESTION_TEXT = "text";


        static String[] keySet;
        static String[] requiredChildes;

        static {
            keySet = new String[]{
                    KEY_SECRET,
                    KEY_SESSION_STATUS,
                    KEY_ATTENDANCE_STATUS,
                    KEY_FOR_GROUP_ID,
                    KEY_NAMES_LIST,
                    KEY_FOR_SESSION_NAME_,
                    KEY_FOR_OBJECTIVES_LIST};

        }

        static {
            requiredChildes = new String[]{
                    KEY_SESSION_STATUS,
                    KEY_ATTENDANCE_STATUS,
                    KEY_FOR_GROUP_ID,
                    KEY_FOR_SESSION_NAME_};
        }

        private SessionEntry() {
        }

    }


    static final class ObjectiveEntry{
        private ObjectiveEntry() {
        }
        static final String KEY_AVERAGERATING="rating" ;
        static final String KEY_NUM_OF_USER_RATED="number_user_rated";
        static final String KEY_DESC="desc";
    }


    static final class UserEntry {
        static final String KEY_THIS = "user";


        static final String KEY_NAME = "name";
        static final String KEY_EMAIL = "email";
        static final String KEY_INVITATIONS = "followed-groups";
        static String[] keySet;
        static String[] requiredChildes;

        static {
            keySet = new String[]{
                    KEY_NAME,
                    KEY_EMAIL};
        }

        static {
            requiredChildes = new String[]{KEY_NAME,
                    KEY_EMAIL};
        }

        private UserEntry() {
        }
    }

    static final class GroupMessagesEntry {
        static final String KEY_THIS = "groupNs";

        static final String KEY_TITLE = "title";
        static final String KEY_BODY = "message";

        private GroupMessagesEntry() {
        }
    }

    static final class StorageEntry {
        static final String KEY_THIS = "gs://smart-lecture-cmp.appspot.com/";

        static final String FOLDER_PROFILE_IMAGES = "profile-images";
    }


}
