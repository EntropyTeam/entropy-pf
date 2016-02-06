package frontend.auxiliares;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Denise
 */
public class TextUtils {
    
    public static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    
    public static boolean validarEmail(String emailStr) {
        Matcher matcher = EMAIL_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
