package android.text;

/**
 * 字符串工具包
 */
public class TextUtils {
    public static boolean isEmpty( CharSequence str) {
        return str == null || str.length() == 0;
    }
}
