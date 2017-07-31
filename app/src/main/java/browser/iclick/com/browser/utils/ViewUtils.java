package browser.iclick.com.browser.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by bym on 2017/7/26.
 */

public class ViewUtils {

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }


    public static void updateAlphaIfViewExists(@Nullable Activity activity, @IdRes int id, float alpha) {
        if (activity == null) {
            return;
        }

        final View view = activity.findViewById(id);
        if (view == null) {
            return;
        }

        view.setAlpha(alpha);
    }

}
