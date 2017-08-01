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

    public static String getLanguageTag(final Locale locale) {
        // If this were Java 7:
        // return locale.toLanguageTag();

        final String language = getLanguage(locale);
        final String country = locale.getCountry(); // Can be an empty string.
        if (country.equals("")) {
            return language;
        }
        return language + "-" + country;
    }

    public static String getLanguage(final Locale locale) {
        // Can, but should never be, an empty string.
        final String language = locale.getLanguage();

        // Modernize certain language codes.
        if (language.equals("iw")) {
            return "he";
        }

        if (language.equals("in")) {
            return "id";
        }

        if (language.equals("ji")) {
            return "yi";
        }

        return language;
    }

}
