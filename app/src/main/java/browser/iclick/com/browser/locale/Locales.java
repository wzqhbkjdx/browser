package browser.iclick.com.browser.locale;

import android.content.Context;
import android.os.LocaleList;
import android.text.TextUtils;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by bym on 2017/6/24.
 */

public class Locales {

    public static void initializeLocale(Context context) {

    }


    public static Set<String> getCountriesInDefaultLocaleList() {
        final Set<String> countries = new LinkedHashSet<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            final LocaleList list = LocaleList.getDefault();
            for (int i = 0; i < list.size(); i++) {
                final String country = list.get(i).getCountry();
                if (!TextUtils.isEmpty(country)) {
                    countries.add(country.toLowerCase(Locale.US));
                }
            }
        } else {
            final String country = Locale.getDefault().getCountry();
            if (!TextUtils.isEmpty(country)) {
                countries.add(country.toLowerCase(Locale.US));
            }
        }

        return countries;
    }

}
