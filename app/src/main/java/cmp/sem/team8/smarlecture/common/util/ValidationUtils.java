package cmp.sem.team8.smarlecture.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* *
 *Created by Loai Ali on 5/2/2018.
 */

public class ValidationUtils {
    /**
     *
     * @param email :String to be verified
     * @return true if email is valid false otherwise
     */
    public static boolean emailValidator(String email) {

        Matcher matcher = Pattern.compile("^[A-Z._%+-]+[0-9]*+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(email);
        return (matcher.find());
    }

    public static boolean userNameValidator(String userName){
        Matcher matcher=Pattern.compile("[A-Z]",Pattern.CASE_INSENSITIVE).matcher(userName);
        return (matcher.find()&&(userName.length()<20)&&(userName.length()>=4));
    }


}

