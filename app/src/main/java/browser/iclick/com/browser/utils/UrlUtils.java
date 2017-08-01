package browser.iclick.com.browser.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * Created by bym on 2017/6/26.
 */

public class UrlUtils {

    public static boolean isSearchQuery(String text) {
        return text.contains(" ");
    }

    public static String createSearchUrl(Context context, String searchTeam) {
        return null;
    }

    public static boolean isUrl(String url) {
        String trimmedUrl = url.trim();
        if (trimmedUrl.contains(" ")) {
            return false;
        }

        return trimmedUrl.contains(".") || trimmedUrl.contains(":");
    }

    public static String normalize(@NonNull String input) {
        String trimmedInput = input.trim();
        Uri uri = Uri.parse(trimmedInput);

        if (TextUtils.isEmpty(uri.getScheme())) {
            uri = Uri.parse("http://" + trimmedInput);
        }

        return uri.toString();
    }

}
