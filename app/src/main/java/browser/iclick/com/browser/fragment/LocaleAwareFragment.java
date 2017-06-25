package browser.iclick.com.browser.fragment;

import android.support.v4.app.Fragment;

import java.util.Locale;

/**
 * Created by bym on 2017/6/24.
 */

public abstract class LocaleAwareFragment extends Fragment {

    private Locale cachedLocale = null;

    public abstract void applyLocale();

    @Override
    public void onResume() {
        super.onResume();

        // TODO: 2017/6/24
    }
}
