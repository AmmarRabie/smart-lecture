package cmp.sem.team8.smarlecture.common.data.firebase;

/**
 * Created by AmmarRabie on 14/04/2018.
 */

public final class FirebaseContract {
    public static final String KEY_THIS = "smart-lecture-cmp";

    private FirebaseContract() {
    }

    public static final class GroupEntry {

        public static final String KEY_THIS = "group";

        public static final String KEY_OWNER_ID = "group_owner";
        public static final String KEY_NAME = "name";
        public static final String KEY_NAMES_LIST = "names_list";
        public static final String KEY_NAMES_LIST_invite = "invite";
        public static final String KEY_NAMES_LIST_grade = "grade";
        public static final String KEY_SESSIONS = "sessions";
        public static String[] keySet;
        public static String[] requiredChildes;

        static {
            keySet = new String[]{KEY_OWNER_ID,
                    KEY_NAME,
                    KEY_NAMES_LIST,
                    KEY_SESSIONS,KEY_NAMES_LIST_grade,KEY_NAMES_LIST_invite};
        }

        static {
            requiredChildes = new String[]{KEY_OWNER_ID,
                    KEY_NAME,};
        }

        private GroupEntry() {
        }

    }


    public static final class SessionEntry {
        public static final String KEY_THIS = "session";
        public static final String KEY_ATTENDANCE_STATUS = "attendance_status";
        public static final String KEY_SECRET = "attendance_secret";
        public static final String KEY_NAMES_LIST = "names_list";
        public static final String KEY_SESSION_STATUS = "session_status";
        public static final String KEY_FOR_GROUP_ID = "for_group";
        public static final String KEY_FOR_SESSION_NAME_ = "for_name";
        public static final String KEY_FOR_OBJECTIVES_LIST = "objectives";
        public static final String KEY_ATTEND = "attend";
        public static final String KEY_NOTES = "notes";
        public static final String KEY_QUESTIONS = "questions";
        public static final String KEY_QUESTION_OWNER = "owner_id";
        public static final String KEY_QUESTION_TEXT = "text";


        public static String[] keySet;
        public static String[] requiredChildes;

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


    public static final class ObjectiveEntry{
        private ObjectiveEntry() {
        }
        public static final String KEY_AVERAGERATING="rating" ;
        public static final String KEY_NUM_OF_USER_RATED="number_user_rated";
        public static final String KEY_DESC="desc";
    }


    public static final class UserEntry {
        public static final String KEY_THIS = "user";


        public static final String KEY_NAME = "name";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_INVITATIONS = "followed-groups";
        public static final String KEY_PROFILE_IMAGE = "profile-image";
        public static String[] keySet;
        public static String[] requiredChildes;

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

    public static final class GroupMessagesEntry {
        public static final String KEY_THIS = "groupNs";

        public static final String KEY_TITLE = "title";
        public static final String KEY_BODY = "message";

        private GroupMessagesEntry() {
        }
    }

    static final class StorageEntry {
        static final String KEY_THIS = "gs://smart-lecture-cmp.appspot.com/";

        static final String FOLDER_PROFILE_IMAGES = "profile-images";
        static final String KEY_PROFILE_IMAGE_NAME = "main.png";
    }


}
