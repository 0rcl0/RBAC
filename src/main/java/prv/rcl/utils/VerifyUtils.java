package prv.rcl.utils;

import java.util.regex.Pattern;

public class VerifyUtils {

    private static Pattern EMAIL_PATTERN = Pattern.compile("^([a-zA-Z\\d][\\w-]{2,})@(\\w{2,})\\.([a-z]{2,})(\\.[a-z]{2,})?$");

    public static boolean verifyEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
