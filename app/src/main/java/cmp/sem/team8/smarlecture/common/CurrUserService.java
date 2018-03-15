package cmp.sem.team8.smarlecture.common;

/**
 * Created by AmmarRabie on 11/03/2018.
 */

public class CurrUserService {

    static String mName;
    static String mEmail;
    static CURR_USER_ROLE mRole;


    /**
     * enum to specify curr user role state. Use it in the session functionality part
     */
    public enum CURR_USER_ROLE
    {
        LECTURER,
        STUDENT,
        NONE,
    }

    public static String getName() {
        return mName;
    }

    public static void setName(String mName) {
        CurrUserService.mName = mName;
    }

    public static String getEmail() {
        return mEmail;
    }

    public static void setEmail(String mEmail) {
        CurrUserService.mEmail = mEmail;
    }

    public static CURR_USER_ROLE getRole() {
        return mRole;
    }

    public static void setRole(CURR_USER_ROLE mRole) {
        CurrUserService.mRole = mRole;
    }


}
