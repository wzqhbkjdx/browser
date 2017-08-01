package browser.iclick.com.browser.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Created by bym on 2017/8/1.
 */

public class IOUtils {
    public static JSONObject readAsset(Context context, String fileName) throws IOException {
        try (final BufferedReader reader =
                     new BufferedReader(new InputStreamReader(context.getAssets().open(fileName), StandardCharsets.UTF_8))) {
            final StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return new JSONObject(builder.toString());
        } catch (JSONException e) {
            throw new AssertionError("Corrupt JSON asset (" + fileName + ")", e);
        }
    }
}
