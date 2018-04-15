package cmp.sem.team8.smarlecture.common.data;

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
        public static final String KEY_NAMES_LIST = "names_lise";
        public static final String KEY_SESSIONS = "sessions";


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

        public enum AttendanceStatus {
            NOT_ACTIVATED("not-activated"), OPEN("open"), CLOSED("closed");

            String value;

            AttendanceStatus(String val) {
                value = val;
            }


            @Override
            public String toString() {
                return value;
            }
        }

        public enum SessionStatus {
            NOT_ACTIVATED("not-activated"), OPEN("open"), CLOSED("closed");

            String value;

            SessionStatus(String val) {
                value = val;
            }


            @Override
            public String toString() {
                return value;
            }
        }

        private SessionEntry() {
        }

    }



    public static final class UserEntry {
        public static final String KEY_THIS = "user";


        public static final String KEY_NAME = "name";
        public static final String KEY_EMAIL = "email";

        private UserEntry() {
        }

        @Override
        public String toString() {
            return "asfnas";
        }
    }


}
