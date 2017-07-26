package browser.iclick.com.browser.utils;

import android.util.Log;

/**
 * Created by bym on 2017/7/25.
 */

public class LogUtils {

    public static final boolean isDebug = true;

    public static void v(String tag, String content) {
        if(isDebug) {
            Log.v(tag, content);
        }
    }

    public static void i(String tag, String content) {
        if(isDebug) {
            Log.i(tag, content);
        }
    }


}
