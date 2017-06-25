package browser.iclick.com.browser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import browser.iclick.com.browser.activities.LocaleAwareAppCompatActivity;
import browser.iclick.com.browser.fragment.HomeFragment;
import browser.iclick.com.browser.utils.SafeIntent;
import browser.iclick.com.browser.web.BrowsingSession;
import browser.iclick.com.browser.web.WebViewProvider;

public class MainActivity extends LocaleAwareAppCompatActivity {

    @Override
    public void applyLocale() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: 2017/6/24
        //        if (Settings.getInstance(this).shouldUseSecureMode()) {
        //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        //        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_main);

        SafeIntent intent = new SafeIntent(getIntent());

        if((intent.getFlags() & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0 &&
                !BrowsingSession.getInstance().isActive()) {
            intent = new SafeIntent(new Intent(Intent.ACTION_MAIN));
            setIntent(intent.getUnsafe());
        }

        if(savedInstanceState == null) {
            WebViewProvider.performCleanup(this);

            showHomeScreen();

            if(Intent.ACTION_VIEW.equals(intent.getAction())) {
                final String url = intent.getDataString();

                // TODO: 2017/6/24
                // BrowsingSession.getInstance().loadCustomTabConfig(this, intent);

            }


        }


    }

    private void showHomeScreen() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag(HomeFragment.FRAGMENT_TAG) == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.container, HomeFragment.create(), HomeFragment.FRAGMENT_TAG)
                    .commit();
        }

    }
}

















