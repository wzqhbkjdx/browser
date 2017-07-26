package browser.iclick.com.browser.utils;

import android.os.Looper;

/**
 * Created by bym on 2017/7/26.
 */

public class ThreadUtils {

    private static final Thread uiThread = Looper.getMainLooper().getThread();

    public static void assertOnUiThread() {
        final Thread currentThread = Thread.currentThread();
        final long currentThreadId = currentThread.getId();
        final long expectedThreadId = uiThread.getId();

        if (currentThreadId == expectedThreadId) {
            return;
        }

        throw new IllegalThreadStateException("Expected UI thread, but running on " + currentThread.getName());
    }

}
