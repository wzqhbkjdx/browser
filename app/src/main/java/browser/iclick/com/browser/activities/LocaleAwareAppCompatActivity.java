package browser.iclick.com.browser.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

/**
 * Created by bym on 2017/6/24.
 */

public abstract class LocaleAwareAppCompatActivity extends AppCompatActivity {

    private volatile Locale mLastLocale;

    public abstract void applyLocale();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
    }
}




















