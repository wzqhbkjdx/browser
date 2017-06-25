package browser.iclick.com.browser.utils;

import android.content.Intent;
import android.util.Log;

import static com.adjust.sdk.Constants.LOGTAG;

/**
 * Created by bym on 2017/6/24.
 */

public class SafeIntent {

    private final Intent intent;

    public SafeIntent(Intent intent) {
        this.intent = intent;
    }

    public int getFlags() {
        return intent.getFlags();
    }


    public Intent getUnsafe() {
        return intent;
    }

    public String getAction() {
        return intent.getAction();
    }

    public String getDataString() {
        try {
            return intent.getDataString();
        } catch (OutOfMemoryError e) {
            Log.w(LOGTAG, "Couldn't get intent data string: OOM. Malformed?");
            return null;
        } catch (RuntimeException e) {
            Log.w(LOGTAG, "Couldn't get intent data string.", e);
            return null;
        }
    }

}
